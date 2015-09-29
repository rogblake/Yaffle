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
		Iterator<Player> itp = plyrs.iterator();
		while (itp.hasNext()){
			ret.append(itp.next().toJSONString());
		}
		return ret.toString();
	}
}
