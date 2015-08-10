package org.oatesonline.yaffle.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.oatesonline.yaffle.entities.impl.League;
import org.oatesonline.yaffle.entities.impl.Team;

public class XScoresDataProvider extends DataProvider implements ILeagueDataProvider {

	Logger log = Logger.getLogger(XScoresDataProvider.class.getName());
	public XScoresDataProvider() {
		super((String) "XSCORES_MOBILE");
	}

	@Override
	public League getLeague(String leagueCode) throws LeagueURLNotFoundException, DataServiceCorruptionException{
		String leagueURL = leagueCode.toUpperCase() + "_URL";
		String rawData = getResource(leagueURL);
		League leagueTable = parseLeagueData(rawData, leagueCode);
		return leagueTable;
	}
	
	private League parseLeagueData(String data, String leagueCode){
		log.log(Level.FINER, "Entering league parser");
		String[] rawLeagueTable = preprocessTable(data);
		League league = new League(leagueCode);
		league.setName(getProviderBundle().getString(leagueCode.toUpperCase() + "_NAME"));
		Set<Team> teams  = processTable(rawLeagueTable, leagueCode);
		if (null != teams){
			Iterator<Team> it = teams.iterator();
			while (it.hasNext()){
				league.addTeam(it.next());
			}
		}
		return league;	}
	
	private String[] preprocessTable(String data){
		String ret[] = null;  
		String temp1 = data.split("</div>")[2];
		String[] temp = temp1.split("</?tr>?");
		String leagueStr;
		ArrayList<String> tList = new ArrayList<String>();
		if (temp.length > 3){
			for (int i=1; i< temp.length; i++){
				
			//Every 2nd entry is just whitespace/newlines so we eliminate it from the return
				if (!temp[i].startsWith("\n")){
					tList.add(temp[i]);
				}
			}
			String[] strArr = {"",""};
			ret = tList.toArray(strArr);
		}
		return ret;
	}
	
	private Set<Team> processTable(String[] teamData, String leagueCode){
		Set<Team>  teams = new TreeSet<Team>();
		if ((null != teamData) && (teamData.length >2)){
			for (int i=2; i< teamData.length; i++){
				if (!Utils.isEmpty(teamData[i])){
					Team t = createTeam(teamData[i], leagueCode);
					if (null != t){
						teams.add(t);
					}
				}
			}
		}
		return teams;
	}
	/*
	 *  class="ltrow">
        <td class="ltcol1">
            2
        </td>
        <td class="ltcol2">
            Anderlecht
        </td>
        <td class="ltcol3">
            25
        </td>
        <td class="ltcol4">
            47
        </td>
	 */
	private Team createTeam(String teamData, String leagueCode){
		String tName ="";
		short tPts = 0;
		short tPld = 0;
		teamData = teamData.replace("<td class=\"ltcol", "");
		return null;
	}

}
