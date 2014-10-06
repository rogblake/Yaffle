package org.oatesonline.yaffle.services.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOPlayer;
import org.oatesonline.yaffle.entities.dao.DAOTeam;
import org.oatesonline.yaffle.entities.impl.DTOPreexistingEntityException;
import org.oatesonline.yaffle.entities.impl.Player;
import org.oatesonline.yaffle.entities.impl.Team;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;

public class TeamService extends YaffleService {
	/**
	 * REturns the teams associated with the player's ID 
	 * 
	 * @return A json object containing all Team data currently associated with that player
	 * @return 403 if the requested player teams is not the caller.
	 * @return 404 if there are no teams associated with the player
	 * @return  
	 */
	@Get
	public String getPlayersTeams(){
		StringBuffer ret = new StringBuffer();
		ret.append('{');
		Player player = validateUser();
		if (null != player){
			Collection<Key<Team>> kTeams = player.getTeamKeys().values();
			Key<Team> kTeam;
			DAOTeam daoT = DAOFactory.getDAOTeamInstance();
			for (Iterator<Key<Team>> iterator = kTeams.iterator(); iterator.hasNext();) {
				kTeam = (Key<Team>) iterator.next();
				Team t = daoT.getTeam(kTeam);
				if (null != t){
					ret.append(t.toJSONString());
					if (iterator.hasNext())
						ret.append(",\n");
				}	
			} 
		}
		ret.append('}');
		getResponse().setStatus(Status.SUCCESS_CREATED);
		return ret.toString();
	}
	
	/**
	 * Adds a team to the players list of teams. If the player already has a team in the list, it is replaced
	 * Or, a fault is returned if not permitted. 
	 * @return 200 if successful
	 * @return 403 if not permitted to add to this particular player's list of teams 
	 * This may because the update window is not open or the caller is not the target player
	 * 
	 */
	/*
	 * {
	 * 	teams:[
	 * 		  {
	 * 			"teamName":"Arsenal",
	 * 			"leaguecode":"E1"
	 * 		  },
	 * 		...
	 * 		}]
	 * }
	 */
	@Post("application/json")
	public String addTeamsToPlayer(Representation rep){
		String ret = "";
// 		Player p = validateUser();
		DAOPlayer daoP = DAOFactory.getDAOPlayerInstance();
		String pIdStr = getRESTParamFromURL("playerid");
		Player p = null;	
		p = Utils.retrievePlayer(pIdStr);
		boolean updateFailed = false;
		if (null != p){
			if (null != pIdStr){
				Long pId = 0L;
				String data = "";
				String jsonData = "";
				try {
					jsonData = decodePostData(rep);
					pId = Long.parseLong(pIdStr);
				} catch (NumberFormatException nfEx){
					ret = "<" + pIdStr + "> is not a valid numeric identifier";
					log.log(Level.WARNING, ret);
					getResponse().setStatus(Status.CLIENT_ERROR_EXPECTATION_FAILED);
				} 
				if (pId.equals( p.getId())){
					Map<String, Team> teamIds = parseTeams(jsonData, p);
					//ensure that the logged in player is making the call by comparing the 
					//player ID from the URL to p.getId()
					//if so
					//get the league code 
					// add the league code/team id to the player's collection of team ids
					Iterator<String> it = teamIds.keySet().iterator();
					String lc;
					Team t;
					while (it.hasNext()){	
						lc= it.next();
						t = teamIds.get(lc);
						if (null != t){							
							p.addTeam(lc, t);
							ret += "Added to\t"+ lc +"\t:" + t.getName();
						} else {
							ret += "FAILED to add team for\t"+ lc +"\t:";
							updateFailed = true;
						}
					}
					ret = "Player Team Selection Updated";
					getResponse().setStatus(Status.SUCCESS_OK);
				}
				else {
					ret = "You are not allowed to update another player's profile";
					getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				}
			} else {
				ret = "No Player data was recieved";
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			}
			if (updateFailed){
				getResponse().setStatus(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			}
		}	
		return ret;
	}
	
	private Map<String, Team> parseTeams(String jsonData, Player player){
		Map<String, Team> teams = new TreeMap<String, Team>();
		JSONParser parser=new JSONParser();
		JSONObject obj = null;
		JSONArray pData = null;
		DAOTeam daoTeam =DAOFactory.getDAOTeamInstance();
		DAOPlayer daoPlayer =DAOFactory.getDAOPlayerInstance();
		String tName = "";
		try {
			 obj=(JSONObject) parser.parse(jsonData);
			 pData = (JSONArray) obj.get("teams");
			 JSONObject teamData;
			 Team t;
			 if (null != pData){
				 Iterator it = pData.iterator();
				 while (it.hasNext()){
					 teamData = (JSONObject)it.next();
					 if (teamData != null){
						tName = teamData.get("teamName").toString();
						String lc = teamData.get("leaguecode").toString();
						if (null != lc) {
							lc = lc.toUpperCase();
							if (null != tName){
								t = daoTeam.getTeam(tName,lc);
								if ((null != t) && (t.getLeagueCode() != null)){
									if (lc.equals(t.getLeagueCode().toUpperCase())){
										daoPlayer.addTeamToPlayer(t,lc, player);			
									}
								}
							}
					 }
					 }
				 }
			 }
		} catch (ParseException e){
			String errMsg = "Error Parsing Player JSON";
			log.log(Level.FINEST, errMsg);
			e.printStackTrace();
//		} catch (EntityNotFoundException e) {
//			String errMsg = "Error Retrieving Team: " + tName + " from data store.";
//			log.log(Level.FINEST, errMsg);
//			e.printStackTrace();
		} catch (DTOPreexistingEntityException dtoEx) {
			String pEmail = "";
			if (null != player){
				pEmail = player.getEmail();
			}
			String errMsg = "Error Adding Team  <" + tName + "> for player <" + pEmail + ">.";
			log.log(Level.FINEST, errMsg);
			dtoEx.printStackTrace();
		}
		return teams;
	}
	
}
