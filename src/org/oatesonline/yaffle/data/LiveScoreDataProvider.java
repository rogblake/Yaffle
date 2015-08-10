package org.oatesonline.yaffle.data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.oatesonline.yaffle.entities.impl.League;
import org.oatesonline.yaffle.entities.impl.Team;


public final class LiveScoreDataProvider extends DataProvider implements ILeagueDataProvider {

	protected static String provider_key = "LIVE_SCORES";
	
	Logger log = Logger.getLogger(LiveScoreDataProvider.class.getName());
	public LiveScoreDataProvider() {
		super(provider_key);
	}

	@Override
	public League getLeague(String leagueCode) throws LeagueURLNotFoundException, DataServiceCorruptionException{
		String leagueURLKey = leagueCode.toUpperCase() + "_URL";
		String rawData = getResource(leagueURLKey);
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
		return league;
	}
	
	private String[] preprocessTable(String data){
		String ret[] = null;           
		String[] temp = data.split("</?table>?");
		String leagueStr;
		if (temp.length > 8){
			leagueStr = temp[8];
			ret = leagueStr.split("</?tr>?");
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
	
	private Team createTeam(String teamStr, String leagueCode){
		Team ret = null;
		String teamData[] = teamStr.split("</?td>?");
		if ((null != teamData) && (teamData.length > 18)){
			ret = initializeTeam(teamData);
			ret.setLeagueCode(leagueCode);
		}
		return ret;
	}
	
	/*
	 * Index Entries are as follows:
	 * League Position =[1]
	 * Name = [3]
	 * Games Played = [5]
	 * Won = [7]
	 * Drawn = [9]
	 * Lost = [11]
	 * GF-GA = [13]
	 * GD = [15]
	 * Pts = [17]

	  class=BorderBottomRightF4Team><a class='standingb' href='standings.php?ttFK=48&country=2&oFK=831981'>Watford</a>
	  class=BorderBottomRightF4><a class='standing' href='standings.php?ttFK=48&country=2&oFK=831981'>2</a>
	 */
	private Team initializeTeam(String[] teamData){
		Team t = new Team();
		int pos = extractInt(teamData[1]);
		int pld = extractInt(teamData[5]);
		int gd = extractInt(teamData[15]);
		int pts = extractInt(teamData[17]);
		String tName = Utils.stripTD(Utils.stripAnchor(teamData[3]));
		t.setPos((short) pos);
		t.setGD(gd);
		t.setPld((short)pld);
		t.setPts(pts);
		t.setName(tName);
		return t;
	}
	
	
	
	int extractInt(String teamValue){
		int ret = 0;
		Pattern p = Pattern.compile("\\d");
//		teamValue = stripAnchor(teamValue);
		String[] temps = teamValue.split(">");
		String temp = "";
		if (temps.length >=2){
			temp = temps[temps.length-1].trim();
		}
		try{			
			if (!p.matcher(temp).matches()){
				temp = temp.replaceFirst("<\\/a", "");
			}
			ret = Integer.parseInt(temp);
		} catch (NumberFormatException nfEx){
			log.log(Level.FINER, "Could not extract integer from <" + teamValue + "> in " + provider_key);
		}
		return ret;
	}
}
