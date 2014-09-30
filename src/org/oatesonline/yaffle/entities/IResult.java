package org.oatesonline.yaffle.entities;

import java.util.Date;

public interface IResult {
	
	public Date getDateTime();
	
	public ITeam getHomeTeam();
	
	public ITeam getAwayTeam();
	
	public String getURL();
	
	public int getHomeScore();
	
	public int getAwayScore();
	
	//0=draw, 1=home win, 2= away win
	public short getResult();
	
	public void setDateTime (Date date);
	
	public void setHomeTeam(ITeam homeTeam);
	
	public void setAwayTeam(ITeam awayTeam);
	
	public void setURL(String url);
	
	public void setHomeScore(int homeScore);
	
	public void setAwayScore(int awayScore);
	
	//0=draw, 1=home win, 2= away win
	public void setResult(short result);

}
