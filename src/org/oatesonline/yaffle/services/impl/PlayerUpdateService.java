package org.oatesonline.yaffle.services.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.oatesonline.yaffle.entities.IPlayer;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOPlayer;
import org.oatesonline.yaffle.entities.impl.Player;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public class PlayerUpdateService extends YaffleService {
	
	Logger log = Logger.getLogger(PlayerUpdateService.class.getName());
	/**
	 * Updates a player by adding the team give by {teamId} into the players list of Teams or replacing the existing
	 * team for the same leagues
	 */
	@Get
	public String addTeam(){
		String ret = "";
		Player p = validateUser();
		DAOPlayer daoP = DAOFactory.getDAOPlayerInstance();
		if (null != p){
			String pIdStr = getRESTParamFromURL("playerid");
			if (null != pIdStr){
				Long pId = 0L;
				try {
					pId = Long.parseLong(pIdStr);
				} catch (NumberFormatException nfEx){
					ret = "<" + pIdStr + "> is not a valid numeric identifier";
					log.log(Level.WARNING, ret);
					getResponse().setStatus(Status.CLIENT_ERROR_EXPECTATION_FAILED);
				}
				if (pId.equals( p.getId())){
					ret = "You are not allowed to update another player's profile";
					getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				}
				
			}
			//ensure that the logged in player is making the call by comparing the 
			//player ID from the URL to p.getId()
			//if so
				//get the league code 
				// add the league code/team id to the player's collection of team ids
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
