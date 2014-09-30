package org.oatesonline.yaffle.entities;

import java.util.Date;

public interface IFixture {
	
	public Date getDateTime();
	
	public ITeam getHomeTeam();
	
	public ITeam getAwayTeam();
	
	public String getURL();
	
	public void setDateTime (Date date);
	
	public void setHomeTeam(ITeam homeTeam);
	
	public void setAwayTeam(ITeam awayTeam);
	
	public void setURL(String url);
	
	
}
