package org.oatesonline.yaffle.entities.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oatesonline.yaffle.entities.IPlayer;
import org.oatesonline.yaffle.entities.impl.DTOPreexistingEntityException;
import org.oatesonline.yaffle.entities.impl.Player;
import org.oatesonline.yaffle.entities.impl.Team;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;

public class DAOPlayer extends DAO<Player> {
	   Logger log = Logger.getLogger(DAOPlayer.class.getName());
	   
	   DAOPlayer(){
		   super();
	   }
	   
	   public IPlayer findPlayer(String email){
	        Query<Player> allPlayers = ofy.query(Player.class);
	        QueryResultIterator<Player> it = allPlayers.iterator();
	        IPlayer found = null;
	        while (it.hasNext()){
	        	Player p = it.next();
	        	if (p.getEmail().equals(email)){
	        		found = (IPlayer) p;
	        		break;
	        	}
	        }
	        if (found != null){
	        	return found;
	   		}
	   		return null;
	    }
	 
	    public IPlayer addPlayer(String name, String email, String nickname, String pwd){
	        IPlayer player = (IPlayer) ofy().find(Player.class,email);
	        if (player == null){
	            player = new Player(name, email, pwd);
	            player.setPassword(pwd);
	            player.setNickname(nickname);
	            ofy().put(player);
	            return player;
	        }
	        else {
	            return player;
	        }
	    }
	    
	    public IPlayer getPlayer(long id){
	    	 IPlayer player = (IPlayer) ofy().get(new Key<Player>(Player.class, id));
	    	 return player;
	    }
	    
	    public IPlayer getPlayer(String nickname, String pin){
	    	 Query<Player> query = ofy.query(Player.class).filter("nickname", nickname).filter("pwd", pin);
	    	 QueryResultIterator<Player> it = query.iterator();
	    	 IPlayer player = null;
	    	 while (it.hasNext()){
	    		 player = (IPlayer) it.next(); 
	    	 }
	    	 return player;
	    }
	    
	    /**
	     *  Adds the team to the Players Team collection. If a team from the league given by <code>leagueCode</code> is 
	     *  already part of the collection, then a <code>DTOPreexistingEntityException</code> will be thrown.
	     * @param team the team to Add to the players Teams.
	     * @param leagueCode The target league code containing the target Team. 
	     * @param player The player to which the Team is being added. 
	     * @throws DTOPreexistingEntityException
	     */
	    public void addTeamToPlayer(Team team, String leagueCode, Player player) throws DTOPreexistingEntityException{
	    	if (null != team){
	    		Team t = getTeam (player, leagueCode);
	    		//If a team is registered for the given league already we throw an exception
	    		if (!(team.equals(t))) {
	    			updatePlayersTeam(team, leagueCode, player);
	    		}
	    		//else throw new DTOPreexistingEntityException();
	    	}
	    }
	    
	    private Team getTeam(Player player, String leagueCode){
	    	if ((null != player) & (null != leagueCode)){
	    		List<Key<Team>> tks = player.getTeamKeys();
	    		if( tks != null){
	    			Iterator<Key<Team>> itk  = tks.iterator();
	    			Team t = null;
	    			while (itk.hasNext()){
	    				t = ofy().get(itk.next());
	    				if (null != t){	    		
    						if (leagueCode.equals(t.getLeagueCode())){
    							return t;
    						}
	    				}
	    			}
	    		}
	    	}
	    	return null;    	
	    }
	    
	    public void updatePlayersTeam(Team team, String leagueCode, Player player){
	    	if (null != player){
	    		Team t = getTeam(player, leagueCode);
	    		if (null != t){
	    			List<Key<Team>> tks = player.getTeamKeys();
	    			Iterator<Key<Team>> itks = tks.iterator();
	    			Team myTeam = null;
	    			DAOTeam daoT = DAOFactory.getDAOTeamInstance();
	    			Key<Team> kt = null;
	    			while (itks.hasNext()){
	    				kt = itks.next();
	    				myTeam = daoT.getTeam(kt);
	    				if (null != myTeam){
	    					if (myTeam.getLeagueCode().equalsIgnoreCase(leagueCode)){	    						
	    						boolean success = player.getTeamKeys().remove(kt);	    					}
	    				}
	    			}
	    		}
    			player.getTeamKeys().add( new Key<Team>(Team.class, team.getName()));
    			log.log(Level.FINE, team.getName() + " added to " + player.getName() + " teams in league " + leagueCode +".");
    			ofy().put(player);
	    	}
	    }
	    
	    public List<Player> getAllPlayers(){
	    	List<Player> ret = new ArrayList<Player>();
	    	Query<Player> players = ofy().query(Player.class);
	    	ret.addAll(players.list());
	    	return ret;
	    }
	    
	   /**
	    * Updates each individual players score on the leaderboard. Updates their position and their previous position
	    * @return
	    */
	    public Set<Player> updateLeaderboard(){
	    	List<Player> players = getAllPlayers();
	    	Iterator<Player> ip = players.iterator();
	    	while (ip.hasNext()){
	    		updateScores(ip.next());
	    	}
	    	//The return from getAllPlayers() is not guaranteed to be sorted, so we have to 
	    	//create a sorted leaderboard using a sorted collection (treeset)
	    	Set<Player> playerSet = new TreeSet<Player>();
	    	playerSet.addAll(players);
	    	//Add in the latest position
	    	Player p = null;
	    	Iterator<Player> ips = playerSet.iterator();
	    	short i = 1;
	    	while(ips.hasNext()){
	    
	    		p = ips.next();
	    		p.setPrev(p.getPos());
	    		p.setPos(i);
	    		i++;
	    	}
	    	ofy.put(playerSet);
	    	return playerSet; //Sorted 
	    }
	    
	    private void updateScores(Player p){
	    	if (null != p){
	    		resetTotals(p);
	    		Collection<Key<Team>> teamKeys = p.getTeamKeys();
	    		Iterator<Key<Team>> itk = teamKeys.iterator();
	    		Team t = null;
	    		int gd = 0;
	    		int pts = 0;
	    		int pld = 0;
	    		while (itk.hasNext()){
	    			t = ofy().get(itk.next());
	    			if (t.getLeagueCode().toLowerCase().startsWith("e")){
		    			gd = gd - t.getGD();
		    			pts = pts - t.getPts();	    				
	    			} else {	    				
	    				gd = gd + t.getGD();
	    				pts = pts+ t.getPts();
	    			}
	    			pld = pld + t.getPld();	
	    		}
	    		p.setPld((short)pld);
	    		p.setPts(pts);
	    		p.setGd(gd);
	    	}
	    }
	    
	    private void resetTotals(Player p){
	    	p.setGd(0);
	    	p.setPld((short)0);
	    	p.setPos((short)0);
	    	p.setPts(0);
	    }
	    /**
	     * {"id":233,"teams":[],"nickname":"Yabadabadoo!","email":"fred@bedrock.com","name":"Fred Flintstone","gd":0,"pts":0,"pos":0,"pld":0}
	     */
	    public Player fromJSON(String jsonPlayer){
	    	String[] lcodes = {"E1","E2","E3","E4","S1","S2","S3","S4"};
	    	Player ret = null;
			JSONParser parser=new JSONParser();
			Object obj = null;
			Map pData = null;
			try {
				 obj=parser.parse(jsonPlayer);
			} catch (ParseException e) {
				log.log(Level.FINEST, "Error Parsing Player JSON /n" + jsonPlayer);
				e.printStackTrace();
			} finally {
				if (null != obj){					
					pData = (Map) obj;
					String teamID = null;
					String teamURL = null;
					Team t = null;
					DAOTeam daoT = DAOFactory.getDAOTeamInstance();
					ret = new Player((String) pData.get("name"), (String) pData.get("email"), (String)pData.get("pin"));
					
					for (String lcode : lcodes){
						teamID = (String) pData.get(lcode);
						teamURL = createTeamURL(teamID);
						t = daoT.getTeamByUrl(teamURL);
						if (null != t){
							try {
								addTeamToPlayer(t, lcode, ret);
							} catch (DTOPreexistingEntityException e) {
								log.log(Level.SEVERE, "Team resource : <" + teamURL + "> already added. ");
								e.printStackTrace();
							}
						}
					}
					Long lPld = (Long) pData.get("pld");
					short sPld = (short) lPld.longValue();
					if (null!= ret){
						ret.setNickname((String) pData.get("nickname"));
					}
					ret.setPld(sPld);
					ret.setGd((int) ((Long) pData.get("pos")).longValue());
					ret.setPts((int) ((Long) pData.get("pts")).longValue());	
					
				}
			}
			return ret;
	    }
	    
		/**
		 * This method is coupled directly to Football-Data as a Data Provider
		 * 
		 * @param TeamID  the team ID per the data provided by api.football-data.org
		 * @return An URI of a resource providing data about the select team
		 */
		private String createTeamURL(String TeamID){
			return "http://api.football-data.org/alpha/teams/" + TeamID;
		}
		
	    public Map<String, Team> getPlayersTeams(Player p){
	    	Map<String, Team> ret = new TreeMap<String, Team>();
	    	List<Key<Team>> teamKeys = p.getTeamKeys();
	    	DAOTeam daoT = DAOFactory.getDAOTeamInstance();
			Iterator<Key<Team>> itLeague = teamKeys.iterator();
			Team t  = null;
			Key<Team> kTeam = null;
			String lc = "";
			while (itLeague.hasNext()){
				lc =daoT.getTeam(itLeague.next()).getLeagueCode();
				t  = daoT.getTeam(kTeam);
				if (null != t){
					ret.put(lc, t);
				}
			}
	    	return ret;
	    }
	    
	    /**
	     * re-calculates the running totals for the player, by adding up the aggregate totals for all 
	     * that players teams.<br/> 
	     * It does not update the Teams' positions in their respective leagues. That task is done externally 
	     * to this task..<br/> 
	     * For 2016  Teams with  'S' league Codes get added to players score.
	     * Teams with 'E' League Codes get subtracted from the Player's score
	     * 
	     * <B>This is not used</B>
	     * 
	     * @param p the target player whos running totals are to be updated
	     * @param teamMap a map of the players teams, keyed by league code
	     * @return boolean if the player was successfully updated.
	     */
	    public boolean updatePlayerTotals(Player p, Map<String, Team> teamMap){
	    	if ((null != p ) && (null != teamMap)){	    		
	    		Iterator<String> itLC = teamMap.keySet().iterator();
	    		Set<String> teamKeys = teamMap.keySet();
	    		//From scratch, recalculate the players subtotals for GoalDifference, Points and GamesPlayed
	    		int pGD = 0;
	    		int pPts = 0;
	    		short pPld = 0;
	    		Team t = null;
	    		Map<String, Team> addTeams = new HashMap<String, Team>(4);
	    		Map<String, Team> subtractTeams = new HashMap<String, Team>(4);
	    		String[] teamKeyStr = teamKeys.toArray(new String[teamMap.size()]);
	    		for (int i=0; i < teamKeyStr.length ; i++){
	    			if  (teamKeyStr[i].toLowerCase().contains("e")){
		    			pGD += t.getGD();
		    			pPts += t.getPts();	    				
	    			} else { //it starts with 'E' so we subtract
		    			pGD -= t.getGD();
		    			pPts -= t.getPts();	    				
	    			}
	    			//Games played gets added by default regardless if we are adding or subtracting points
	    			pPld += t.getPld();
	    		}
	    		p.setGd(pGD);
	    		p.setPts(pPts);
	    		p.setPld(pPld);
	    		//Persist the updated player
	    		ofy.put(p);
	    		return true;
	    	} else 
	    		return false;
	    }
}
