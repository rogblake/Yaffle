	package org.oatesonline.yaffle.services.impl;

import java.util.logging.Logger;

import org.oatesonline.yaffle.entities.ILeague;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOLeague;
import org.oatesonline.yaffle.entities.impl.League;
import org.oatesonline.yaffle.entities.impl.Player;
import org.oatesonline.yaffle.services.ILeagueService;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public class LeagueService extends YaffleService implements ILeagueService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8805488746599645406L;
	Logger log = Logger.getLogger(LeagueService.class.getName());
	private String leagueCode;
	
	public LeagueService() {
		super();
		leagueCode = "";
	}

	
	@Get
	@Override
	public String getLeague(){
		//Player p = validateUser(null);
	    this.leagueCode = getRESTParamFromURL("leaguecode").toUpperCase();  
	   
	    setResponseHeaderValueFor("Access-Control-Allow-Origin"); // dodgy. Not for Production Code
	    
	    DAOLeague daoLeague = DAOFactory.getDAOLeagueInstance();
	    League l = daoLeague.getLeague(this.leagueCode, false);
	    if (null != l){	    	
	//	    	return l.toXMLString();
	    	return l.toJSONString();
	    } 
	    return ("Error");
	}

	@Post
	@Override
	public ILeague updateLeague() {
		this.leagueCode = getRequestHeaderValue("leaguecode");  
		System.out.println("We have received the league code <" + leagueCode + ">");
		return null;
	}
	

}
