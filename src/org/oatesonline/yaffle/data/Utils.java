package org.oatesonline.yaffle.data;

import java.util.ResourceBundle;

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
	
	
}
