package org.oatesonline.yaffle.entities.dao;

public class DAOFactory {
	
	private static DAOTeam daoTeam;
	
	private static DAOLeague daoLeague;
	
	private static DAOPlayer daoPlayer;
	
	public static DAOLeague getDAOLeagueInstance(){
		if (null == daoLeague){
			daoLeague = new DAOLeague();
		}
		return daoLeague;
	}

	public static DAOTeam getDAOTeamInstance(){
		if (null == daoTeam){
			daoTeam = new DAOTeam();
		}
		return daoTeam;
	}

	public static DAOPlayer getDAOPlayerInstance(){
		if (null == daoPlayer){
			daoPlayer = new DAOPlayer();
		}
		return daoPlayer;
	}
}
