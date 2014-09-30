package org.oatesonline.yaffle.data;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.oatesonline.yaffle.entities.impl.League;

public class XScoresDataProvider extends DataProvider implements ILeagueDataProvider {

	Logger log = Logger.getLogger(XScoresDataProvider.class.getName());
	public XScoresDataProvider() {
		super((String) "XSCORES_MOBILE");
	}

	@Override
	public League getLeague(String leagueCode) throws LeagueURLNotFoundException, DataServiceCorruptionException{
		String leagueURL = leagueCode.toUpperCase() + "_URL";
		String rawData = getResource(leagueURL);
		League leagueTable = parseLeagueData(rawData);
		return leagueTable;
	}
	
	private League parseLeagueData(String data){
		
		log.log(Level.FINER, "Entering league parser");
		return null;
	}
	

}
