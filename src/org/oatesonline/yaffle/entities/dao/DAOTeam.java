package org.oatesonline.yaffle.entities.dao;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Transient;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oatesonline.yaffle.entities.impl.Team;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.Key;


public class DAOTeam extends DAO<Team> {
	
	@Transient
	Logger log = Logger.getLogger(DAOTeam.class.getName());
	DAOTeam(){
		super();
	}
	
	public boolean createTeam(Team t){
		if (null != t){			
//			Key<Team> k = add(t);
			ofy.put(t);
			if (t.getName()!= null){
//				if ( k != null){
//					//log.log(Level.FINE, "Team  <"+ t.getName() + "> has been successfully saved to the data store.");
//					return true;
//				}
			}
		}
		return true;
	}
	
	public boolean updateTeam(Team t){
		return createTeam(t);
	}
	
	public Team getTeam (String name){
		Team t = ofy.query(Team.class).filter("name" , name).get();
//		Team t = getByProperty("name", name);
		return t;
	}
	
	public Team getTeam(String name, String leagueCode){
		return getTeam(name);
	}
	
	public Team getTeamById(String teamKeyStr) throws EntityNotFoundException{
		Key<Team> kt = Key.create(teamKeyStr);
		Team t = getTeam(kt);
		return t;
	}
	
	public Team getTeam(Key<Team> teamKey){
		Team ret = null;
		if (null != teamKey){
			ret = ofy.get(teamKey);
			if (null == ret){
				log.log(Level.FINEST, " A Team with corresponding key <" + teamKey.getString() + "> has been retrieved from the data store");
			}
		} 
//		 try{
//			 ret = get(teamKey.getId());
//		 } catch (EntityNotFoundException enfEx){
//			 enfEx.printStackTrace();		 
//		 }
		 return ret;
	}
	
	/**
	 * {"name":"Blackburn","gd":-3,"leagueCode":"e2","urlString":null,"pts":1,"pld":3,"pos":22},
	 * 
	 * @param jsonTeam
	 * @return
	 */

	public Team fromJSON(String jsonTeam){
		Team ret = null;
		JSONParser parser=new JSONParser();
		Object obj = null;
		Map tData = null;
		try {
			 obj=parser.parse(jsonTeam);
		} catch (ParseException e) {
			log.log(Level.FINEST, "Error Parsing Team JSON /n" + jsonTeam);
			e.printStackTrace();
		} finally {
			if (null != obj){
				ret = new Team();
				tData = (Map) obj;
				ret.setName((String)tData.get("name"));
				ret.setLeagueCode((String)tData.get("leagueCode"));
				ret.setURL((String) tData.get("urlString"));
				ret.setPld((Short) tData.get("pos"));
				ret.setGD((Integer) tData.get("pos"));
				ret.setPts((Integer) tData.get("pts"));
			}
		}
		return ret;
	}

}
