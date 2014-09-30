package org.oatesonline.yaffle.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOPlayer;
import org.oatesonline.yaffle.entities.impl.Player;
import org.oatesonline.yaffle.entities.impl.Team;
import org.oatesonline.yaffle.services.ILeaderboardService;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class LeaderboardService extends ServerResource implements ILeaderboardService {
	
	/**
	 * 
	 * @return a JSON or XML String of the current Leaderboard
	 */
	@Get
	public String getLeaderboard(){
		String ret = "{ \"players\":";
		SortedSet<Player> lboard = new TreeSet<Player>();
		DAOPlayer daoP = DAOFactory.getDAOPlayerInstance();
		List<Player> plyrs = daoP.getAllPlayers();
		lboard.addAll(plyrs); //Sorts the players into descending order
		List<Player> pList = new ArrayList<Player>();
		pList.addAll(0, lboard);
		ret += JSONArray.toJSONString(pList);
		ret += "}";
		return ret;	
	}

	
	@Post("json"/* "application/json" */)
	public String updateLeaderboard(){
		StringBuffer ret = new StringBuffer ("");
		DAOPlayer daoP = DAOFactory.getDAOPlayerInstance();
		Set<Player> plyrs = daoP.updateLeaderboard();
//		List<Player> plyrs = daoP.getAllPlayers();
//		Iterator<Player> ip = plyrs.iterator();
//		Player p = null;
//		Map<String, Team> teamMap  = null;
//		//Iterate through all players updating their aggregate totals for the leaderboard.
//		while (ip.hasNext()){
//			p = ip.next();
//			teamMap = daoP.getPlayersTeams(p);
//			boolean updateSuccess = daoP.updatePlayerTotals(p,  teamMap);
//			ret.append(p.getNickname());
//			if (updateSuccess){
//				ret.append( " entry updated successfully.\n");
//			} else {
//				ret.append(" entry updated failed.\n");
//			}
//		}
//		return ret.toString();
		
		Iterator<Player> itp = plyrs.iterator();
		while (itp.hasNext()){
			ret.append(itp.next().toJSONString());
		}
		return ret.toString();
	}

	//don't need any of this. See DAOPlayer
//	private String updatePlayerStandings(List<Player> players){
//		TreeSet<Player> plyrs = new TreeSet<Player>(players);
//		Iterator<Player> itp = plyrs.iterator();
//		Player p ;
//		short i = 1;
//		StringBuffer ret = new StringBuffer("");
//		while (itp.hasNext()){
//			p = itp.next();
//    		//set the player's previous position to their current position since this is about to be udated
//    		p.setPrev(p.getPos());
//    		p.setPos(i);
//    		i++;
//    	
//		}
//		return ret.toString();
//	}
}
