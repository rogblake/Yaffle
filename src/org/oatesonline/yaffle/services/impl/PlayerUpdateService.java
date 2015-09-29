package org.oatesonline.yaffle.services.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.oatesonline.yaffle.entities.IPlayer;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOPlayer;
import org.oatesonline.yaffle.entities.dao.DAOTeam;
import org.oatesonline.yaffle.entities.impl.Player;
import org.oatesonline.yaffle.entities.impl.Team;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class PlayerUpdateService extends YaffleService {
	
	Logger log = Logger.getLogger(PlayerUpdateService.class.getName());
	/**
	 * Updates a player by adding the team give by {teamId} into the players list of Teams or replacing the existing
	 * team for the same leagues
	 */
	@Put
	public String addTeam(){
		String ret = "";
		DAOPlayer daoP = DAOFactory.getDAOPlayerInstance();
		DAOTeam daoT = DAOFactory.getDAOTeamInstance();
		String pIdStr = getRESTParamFromURL("playerId");
		String teamStr = getRESTParamFromURL("teamId");
		String leagueCode = getRESTParamFromURL("leagueCode");
		if (null != pIdStr){
			Player p = null;
			Long pId = 0L;
			try {
				pId = Long.parseLong(pIdStr);
				p = daoP.get(pId, Player.class);	
			} catch (NumberFormatException nfEx){
				ret = "<" + pIdStr + "> is not a valid numeric identifier";
				log.log(Level.WARNING, ret);
				getResponse().setStatus(Status.CLIENT_ERROR_EXPECTATION_FAILED);
			}catch (EntityNotFoundException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, "Cannot find Player with ID " + pIdStr);
			}
			if (null != p){
				String teamURL = createTeamURL(teamStr);
				Team t = daoT.getTeam(teamURL);
				if (null != t){					
					p.addTeam(leagueCode.toLowerCase(), t);
					ret = "<" + t.getName() + "> successfully added to league <" + leagueCode + "> for <" + pIdStr + "> ";
				} else {
					log.log(Level.SEVERE, "Cannot find Team called " + teamStr  + " for Player with ID:"+ pIdStr);
				}
			}
		}
		return ret;
	}
	/**
	 * 
	 */
	@Post("application/text")
	public String addPlayer(Representation rep){
		String ret = null;
		DAOPlayer daoP = DAOFactory.getDAOPlayerInstance();
		String jsonData = decodePostData(rep);
		Player p = null;
		if (jsonData != null){
			p = daoP.fromJSON(jsonData);
		} else {			
			getResponse().setStatus(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
		}
		if (p != null){
			daoP.add(p);
			getResponse().setStatus(Status.SUCCESS_CREATED);
			getResponse().getCookieSettings().set(YAFFLE_UID, p.getId().toString());
			IPlayer pl = daoP.findPlayer(p.getEmail());
			ret= ((Player) p).getId().toString();
		} else {	
			getResponse().setStatus(Status.CLIENT_ERROR_EXPECTATION_FAILED);
			ret = "Your player data could not be added due to an error.Please check the information and try again";
		}	 

		return ret;
	}
	
	/**
	 * 
	 * @return false if 
	 */
	private boolean callIsAllowed(){
		return false;
	}
	
	/**
	 * This method is coupled directly to Football-Data as a Data Provider
	 * 
	 * @param TeamID  the team ID per the data provided by api.football-data.org
	 * @return An URI of a resource providing data about the select team
	 */
	private String createTeamURL(String TeamID){
		return "http://api.football-data.org/alpha/teams/" + TeamID;
	}
	
}
