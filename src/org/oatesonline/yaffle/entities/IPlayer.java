package org.oatesonline.yaffle.entities;

import org.oatesonline.yaffle.entities.impl.Team;

import com.googlecode.objectify.Key;

public interface IPlayer {
	
	public String getName();
	
	public Long getId();
	
	public String getEmail();
	
	public String getNickname();
	
	public java.util.Map<String, Key<Team>> getTeamKeys();
	
	
	public void setEmail(String email);
	
	public void setName(String name);
	
	public void setNickname(String nickname);
	
	public void addTeam (String leagueCode, Team team);
	
	public void setPassword(String pwd);
	
}
