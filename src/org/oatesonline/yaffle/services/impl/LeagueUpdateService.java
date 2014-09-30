	package org.oatesonline.yaffle.services.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.oatesonline.yaffle.data.DataServiceCorruptionException;
import org.oatesonline.yaffle.data.ILeagueDataProvider;
import org.oatesonline.yaffle.data.LeagueURLNotFoundException;
import org.oatesonline.yaffle.data.LiveScoreDataProvider;
import org.oatesonline.yaffle.entities.ILeague;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOLeague;
import org.oatesonline.yaffle.entities.impl.League;
import org.oatesonline.yaffle.services.ILeagueService;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class LeagueUpdateService extends ServerResource implements ILeagueService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8805488746599645406L;
	Logger log = Logger.getLogger(LeagueUpdateService.class.getName());
	private String leagueCode;
	
	public LeagueUpdateService() {
		super();
		leagueCode = "";
	}

	@Get
	@Override
	public String getLeague(){
		String lc = (String) getRequestAttributes().get("leaguecode");  
		if (null != lc){			
			this.leagueCode = lc.toUpperCase();
		}
		ILeagueDataProvider ildp =  new LiveScoreDataProvider();
		League league = null;
		try {
			 league = ildp.getLeague(this.leagueCode);
			DAOLeague daoLeague  = DAOFactory.getDAOLeagueInstance();
			daoLeague.createLeague(league);
			log.log(Level.FINE, "Created League " + this.leagueCode);
		} catch (LeagueURLNotFoundException e) {
			log.log(Level.SEVERE, "Exception encountered retrieving " + this.leagueCode + ". The league was not found.");
			e.printStackTrace();
		} catch (DataServiceCorruptionException e) {
			log.log(Level.SEVERE, "Exception encountered retrieving " + this.leagueCode + ". Data Service Corruption.");
			e.printStackTrace();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unknown Exception encountered retrieving " + this.leagueCode );
			e.printStackTrace();
		}
		if (null != league){
			return league.toXMLString();
		}
		return null;
	}

	@Post
	@Override
	public ILeague updateLeague() {
	    this.leagueCode = (String) getRequestAttributes().get("leaguecode");  
		System.out.println("We have received the league code <" + leagueCode + ">");
		return null;
	}
	

}
