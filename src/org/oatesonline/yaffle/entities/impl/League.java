package org.oatesonline.yaffle.entities.impl;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONObject;
import org.oatesonline.yaffle.entities.IDataObject;
import org.oatesonline.yaffle.entities.ILeague;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOTeam;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Indexed;

@Entity
@XmlRootElement
@Cached
public class League extends DTOEntity implements ILeague, IDataObject {

	
	private static final long serialVersionUID = -4530243745840519840L;
	@Transient
	Logger log = Logger.getLogger(League.class.getName());
	
	@javax.persistence.Id
	@Indexed
	@Attribute(name="league", required=true)
	private String leagueCode;
	
	@Attribute(name="name", required=true)
	private String name;
	
	@Attribute(name="lastUpdated", required=true)
	private Date lastUpdated;
	
	@Attribute(name="matchday")
	private int matchday;
	
	@Transient
	private Set<Key<Team>> teams;
	
	/**
	 * used internally to store optional data
	 */
	private String jsonData;
	
	/**
	 * We don't store Team Objects in the DB but they are useful to have, particularly when Serializing the leagueteamObjs
	 */
	@Transient
	@ElementList(name="teams")
	private Set<Team> teamObjs;
	
	private League(){
		teams = new TreeSet<Key<Team>>();
		teamObjs = new TreeSet<Team>();
		jsonData = "";
		lastUpdated = new Date();
	}
	
	public League(String lc){
		this();
		this.leagueCode = lc;
	}
	
	
//	@Override
//	public Long getId() {
//		
//		return this.Id;
//	}

//	@Override
//	public void setId(Long id) {
//		this.Id = id;
//		
//	}

	@Override
	public String toXMLString() {
		Serializer ser = new Persister();
		StringWriter sw = new StringWriter();
		try {
			ser.write(this, sw);
		} catch (Exception e) {
			log.log(Level.FINE, "Error writing XML for Team <" + this.name + ">");
			e.printStackTrace();
		}
		return sw.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject jObj = new JSONObject();
		DAOTeam daoT = DAOFactory.getDAOTeamInstance();
		jObj.put("name", this.name);
		jObj.put("matchday", this.matchday);
		jObj.put("leagueCode", this.leagueCode);
		List<JSONObject> teamList = new LinkedList<JSONObject>();
		//convert team to a JSONObject and add to teamList
		Iterator<Team> it = teamObjs.iterator();
		Team t  = null;
		while (it.hasNext()){
			t = it.next();
			if (null != t){
				log.log(Level.CONFIG, t.toJSONString());
				System.out.print(t.toJSONString());
				teamList.add(t.toJSONObject());
			}
		}

		jObj.put("teams", teamList);
		return jObj.toJSONString();
	}

	@Override
	public String getLeagueCode() {
		return leagueCode;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<Key<Team>> getTeams() {
		return this.teams;
	}

	public void setTeamObjs(Set<Team> teams){
		this.teamObjs = teams;
	}
	@Override
	public void setLeagueCode(String lc) {
		this.leagueCode = lc;
		
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public void setTeams(Set<Key<Team>> teams) {
		this.teams = teams;
		
	}

	@Override
	public void addTeam(Team team) {
		if (null != team){			
			Key<Team> kTeam = new Key<Team>(Team.class,team.getName());
			this.teams.add( kTeam);
			this.teamObjs.add((Team)team);
		}
	}

	@Override
	public Set<Team> getTeamObjs(){
		return this.teamObjs;
	}

	@Override
	public String getJSONData() {
		return this.jsonData;
	}

	@Override
	public void setJSONData(String jsonString) {
		this.jsonData=jsonString;
		
	}

	@Override
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	@Override
	public void setLastUpdated(Date lastUpdate) {
		this.lastUpdated = lastUpdate;
	}

	@Override
	public int getMatchDay() {
		
		return matchday;
	}

	@Override
	public void setMatchDay(int m) {
		matchday = m;
	}

}
