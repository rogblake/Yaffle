package org.oatesonline.yaffle.data;

import java.util.ResourceBundle;

import org.oatesonline.yaffle.entities.impl.Team;

public class Utils {
	
	/**
	 * If the Data provider is known, this utility method returns the raw unprocessed data provided by the data provider, 
	 * for that league <br/>
	 * Use this in <code>ILeagueDataProvider</code> instances when acquiring raw data.
	 * @param leagueCode
	 * @return a string of unprocessed, raw data to be processed by the calling ILeagueDataProvder
	 * @throws LeagueURLNotFoundException 
	 */
	static String  getRawLeagueTableData (DataProvider dp, String leagueCode) throws LeagueURLNotFoundException{
		ResourceBundle rb = dp.getProviderBundle();
		String rawLeagueData = null;
		String path = null;
		if ((null != rb) && (dp.leagueCodes.contains(leagueCode.toLowerCase()))){
			path = rb.getString(leagueCode.toUpperCase()+ "_URL");
			if (null != path){
				
				rawLeagueData = dp.storeFile (dp.getCharData(path));
			}
		} else { 
			throw new LeagueURLNotFoundException(leagueCode);
		}
		return rawLeagueData;
		
	}
	/**
	 *  Input here looks something like this class='BorderBottomRightF4'>2
	 *  
	 * @param teamValue
	 * @return
	 */
	static String stripTD(String teamValue){
		String ret = null;
		if (null != teamValue){
			ret = teamValue.substring(teamValue.indexOf('>')+1).trim();
		}
		return ret;
	}
	
	static boolean isEmpty(String teamData){
		teamData = teamData.trim();
		if (null != teamData) return false;
		if (teamData.equals("")) return false;
		return true;
	}
	
	/**
	 * Removes html Anchor markup 
	 * @param teamValue a piece of markup containing some html a tags
	 * @return the plaintext string representation of the value
	 */
	static String stripAnchor(String teamValue){
		String ret = null;
		if (null != teamValue){
			Team t = new Team();
			String[] temps = teamValue.split("\\'>");
			if (temps.length >=2 ){
				ret = temps[1].replaceFirst("<\\/a>", "");
			}
		}
		return ret;
	}


	
}
