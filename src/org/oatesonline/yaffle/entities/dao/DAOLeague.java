package org.oatesonline.yaffle.entities.dao;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Transient;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.oatesonline.yaffle.entities.impl.League;
import org.oatesonline.yaffle.entities.impl.Team;

import com.googlecode.objectify.Key;

public class DAOLeague extends DAO<League> implements MessageBodyReader<League> {
	
	@Transient
	Logger log = Logger.getLogger(DAOLeague.class.getName());
	
	DAOLeague() {
		super();
	}

	public boolean createLeague(League l){
		boolean ret = false;
		if (null != l){			
			ofy.put(l);
			add(l);
			if (l.getName() != null){
				log.log(Level.FINE, "League  <"+ " XXX " + "> has been successfully saved to the data store.");			
				ret = true && updateLeagueTeams(l);
			}		
			Set<Team> teams = l.getTeamObjs();
			Iterator<Team> it = teams.iterator();
			DAOTeam dt = DAOFactory.getDAOTeamInstance();
			while (it.hasNext()){
				dt.add(it.next());
			}
		}
		return ret;
	}
	
	public boolean updateLeague(League l){
		
		return createLeague(l);
	}
	
	private boolean updateLeagueTeams(League league){
		Set<Team> teams = league.getTeamObjs();
		DAOTeam dt = DAOFactory.getDAOTeamInstance();
		boolean ret = false;
		if ((null != teams) && (teams.size()>1)){
			Iterator<Team> it = teams.iterator();
			while(it.hasNext()){
				dt.createTeam(it.next());
			}
			ret = true;
		}
		return ret;
	}
	/**
	 *  if lazy is true, this will lazily returns the league, and team keys of it's associated teams. 
	 *  if false, then it will populate the <code>teamObjs</code> collection with corresponding teams, sorted
	 *  in league order. 
	 * @param leagueCode
	 * @return
	 */
	public League getLeague(String leagueCode, boolean lazy){
		League ret = null;
		ret =ofy.get(new Key<League>(League.class, leagueCode));
		Iterable<Team> teams = ofy.query(Team.class).filter("leagueCode", leagueCode);
		TreeSet<Team> t = new TreeSet<Team>();
		Iterator<Team> it = teams.iterator();
		while (it.hasNext()){
			t.add(it.next());
		}
		if(t != null){			
			ret.setTeamObjs(t);
		}
		return ret;
	}
	
	public League getLeague (Key<League> leagueKey, boolean lazy){
		League ret = null;
		ret = ofy.get(leagueKey);
		if (null != ret){
			
		}

		return ret;
	}
	private Set<Team> getTeamsFromKeys (Set<Key<Team>> teamKeys){
		Set<Team> ret = null;
		if ((null != teamKeys) && (teamKeys.size() >0)){
			ret = new TreeSet<Team>();
			Iterator<Key<Team>> ikt = teamKeys.iterator();
			Team team = null;
			while (ikt.hasNext()){
				team = ofy.get(ikt.next());
				if (null != team){
					ret.add(team);
				}
			}
		}
		return ret;
	}
	
	@Override
	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2,
			MediaType arg3) {
		
		return false;
	}

	/**
	 * Use this to read League data in XML format, and convert it to a FCO.  <br/>
	 * Don't use this to read XML from data providers.
	 */
	@Override
	public League readFrom(Class<League> arg0, Type arg1, Annotation[] arg2,
			MediaType arg3, MultivaluedMap<String, String> arg4,
			InputStream entityStream) throws IOException, WebApplicationException {

		 try {
		        JAXBContext jaxbContext = JAXBContext.newInstance(League.class);
		        League league = (League) jaxbContext.createUnmarshaller()
		            .unmarshal(entityStream);
		        return league;
		    } catch (JAXBException jaxbException) {
//		        log.log(Level.SEVERE, "XML Parsing Error parsing League");
		    }
		    return null;
	}
	
	
	/**
	 * 
	 * { teams [], "name":"Spanish Primera Division","leagueCode":"e2"}
	 * @param jsonLeague
	 * @return
	 */
	public League FromJSON(String jsonLeague){
		League ret = null;
		
		return ret;
	}
}
