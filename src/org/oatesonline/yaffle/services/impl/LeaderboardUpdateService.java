package org.oatesonline.yaffle.services.impl;

import java.util.Iterator;
import java.util.Set;

import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOPlayer;
import org.oatesonline.yaffle.entities.impl.Player;
import org.oatesonline.yaffle.services.ILeaderboardService;
import org.restlet.resource.Get;

public class LeaderboardUpdateService extends YaffleService implements
		ILeaderboardService {
	
	@Get
	@Override
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
