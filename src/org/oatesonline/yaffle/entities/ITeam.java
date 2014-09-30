package org.oatesonline.yaffle.entities;



public interface ITeam  {
	

//	public Long getId();
	
	public String getName();
	
	public String getURL();
	
	public short getPos();
	
	public int getPts();
	
	public int getGD();
	
	public short getPld();
	
//	public void setId(Long id);
	
	public void setName(String name);
	
	public void setURL(String url);
	
	public void setPos (short pos);
	
	public void setPts(int pts);
	
	public void setGD(int gd);
	
	public void setPld(short pld);
	
	public void setLeagueCode(String lc);
	
	public String getLeagueCode();
	
}
