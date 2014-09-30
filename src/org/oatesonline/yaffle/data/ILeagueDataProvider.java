package org.oatesonline.yaffle.data;

import org.oatesonline.yaffle.entities.impl.League;



public interface ILeagueDataProvider  {
	
	public League getLeague(String leagueCode) throws LeagueURLNotFoundException, DataServiceCorruptionException;
	

}
