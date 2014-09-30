package org.oatesonline.yaffle.services.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOPlayer;
import org.oatesonline.yaffle.entities.impl.Player;

public class Utils {
	static Logger log = Logger.getLogger(Utils.class.getName());

	static void addCORSSupport() {
		
	}
	
	static Player retrievePlayer(String idStr){
		Long id = 0L;
		try {
			id  = Long.parseLong(idStr);
		} catch  (NumberFormatException nfEx){
			log.log(Level.FINER, "Invalid ID provided");
			return null;
		}
		DAOPlayer daoP = DAOFactory.getDAOPlayerInstance();
		Player player = (Player)daoP.getPlayer(id);
		return player;
	}
}
