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
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class PlayerUpdateService extends YaffleService {
	
	Logger log = Logger.getLogger(PlayerUpdateService.class.getName());
	/**
	 * Updates a player by adding the team give by {teamId} into the players list of Teams or replacing the existing
	 * team for the same leagues
	 */
	@Get
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
				Team t = daoT.getTeam(teamStr);
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
	@Post("application/json")
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
			//read the player back from the db (with it's generated ID)
			IPlayer pl = daoP.findPlayer(p.getEmail());
			//return the complete player back to the user
			if (null != pl){					
				ret =  ((Player) pl).toJSONString();
			} else {
				ret = ((Player) p).toJSONString();
			}
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
}
