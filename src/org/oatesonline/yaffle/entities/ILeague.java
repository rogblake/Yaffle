package org.oatesonline.yaffle.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.oatesonline.yaffle.entities.impl.Team;

public interface ILeague {
	
	public String getLeagueCode();
	
	public String getName();
	
	public Set<com.googlecode.objectify.Key<Team>> getTeams();
	
	public void setLeagueCode(String lc);
	
	public void setName(String name);
	
	public void setTeams(Set<com.googlecode.objectify.Key<Team>> teams);
	
	public void addTeam(Team team);
	
	public String getJSONData();
	
	public void setJSONData(String jsonString);
	
	public Set<Team> getTeamObjs();
	
	public Date getLastUpdated();
	
	public int getMatchDay();
	
	public void setMatchDay(int m);
	
	public void setLastUpdated(Date lastUpdate);
}
