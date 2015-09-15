package org.oatesonline.yaffle.data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oatesonline.yaffle.entities.impl.League;
import org.oatesonline.yaffle.entities.impl.Team;

import com.google.appengine.api.log.LogService.LogLevel;


public class FootballDataProvider extends DataProvider implements
		ILeagueDataProvider {

	@Override
	public League getLeague(String leagueCode)
			throws LeagueURLNotFoundException, DataServiceCorruptionException {
		League ret = null;
		String key = leagueCode+ "_URL";
		String leagueStr = getResource(key);
		leagueStr.trim();
		if (null != leagueStr){
			ret = createLeague(leagueStr, leagueCode);
		}
		
		return ret;
	}
	
	private JSONParser parser ;
	private ContainerFactory containerFactory;
	
	public FootballDataProvider(){
		parser = new JSONParser();
		containerFactory = new ContainerFactory(){
		    public List creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		  };

	}

	

	
	/**
	 * Inbound JSON looks as follows
	 {
    "_links": {
        "self": "http://api.football-data.org/alpha/soccerseasons/354/leagueTable/?matchday=25",
        "soccerseason": "http://api.football-data.org/alpha/soccerseasons/354"
    },
    "leagueCaption": "Premier League 2014/15",
    "matchday": 25,
    "standing": [
        {
            "_links": {
                "team": {
                    "href": "http://api.football-data.org/alpha/teams/61"
                }
            },
            "position": 1,
            "teamName": "Chelsea FC",
            "playedGames": 25,
            "points": 59,
            "goals": 55,
            "goalsAgainst": 21,
            "goalDifference": 34
        },
        {
            "_links": {
                "team": {
                    "href": "http://api.football-data.org/alpha/teams/65"
                }
            },
            "position": 2,
            "teamName": "Manchester City FC",
            "playedGames": 25,
            "points": 52,
            "goals": 51,
            "goalsAgainst": 25,
            "goalDifference": 26
        },
        {
            "_links": {
                "team": {
                    "href": "http://api.football-data.org/alpha/teams/66"
                }
            },
            "position": 3,
            "teamName": "Manchester United FC",
            "playedGames": 25,
            "points": 47,
            "goals": 43,
            "goalsAgainst": 24,
            "goalDifference": 19
        }
    ]
}
	 * 
	 * @param leagueStr
	 * @param leagueCode
	 * @return
	 */
	
	private League createLeague(String leagueJson, String leagueCode){
		League ret = new League(leagueCode);
		String leagueStr = leagueJson.toString();
		  try {
			Map jsonMap = (Map)parser.parse(leagueStr, containerFactory);
			Team t = null;
			if (jsonMap != null){
				Map links = (Map) jsonMap.get("_links");
				ret.setName((String) jsonMap.get("leagueCaption"));
				ret.setJSONData( links.toString());
				List teamData =  (List) jsonMap.get("standing");
				if (null != teamData){
					Iterator it = teamData.iterator();
					while (it.hasNext()){	
						t = createTeam((Map)it.next(), leagueCode);
						if (null != t){
							ret.addTeam(t);
						}
					}
				}
			}	
		} catch (ParseException e) {
			log.log(Level.SEVERE, e.getLocalizedMessage());
			e.printStackTrace();
		}

		return ret;
	}
	
	/*
	 *  {
            "_links": {
                "team": {
                    "href": "http://api.football-data.org/alpha/teams/66"
                }
            },
            "position": 3,
            "teamName": "Manchester United FC",
            "playedGames": 25,
            "points": 47,
            "goals": 43,
            "goalsAgainst": 24,
            "goalDifference": 19
        }
	 */
	private Team createTeam (Map teamObj, String leagueCode){
		Team ret =  new Team();
		if (null != teamObj){
			ret.setPos(Short.parseShort( ((teamObj.get("position"))).toString())); 
			ret.setGD(Short.parseShort (teamObj.get("goalDifference").toString())); 
			ret.setPld(Short.parseShort( teamObj.get("playedGames").toString())); 
			ret.setPts(Short.parseShort(teamObj.get("points").toString()));
			ret.setName((String) teamObj.get("teamName"));
			ret.setLeagueCode(leagueCode);
			String teamUrl =(String) ((Map) ((Map) teamObj.get("_links")).get("team")).get("href");
			ret.setURL(teamUrl);	
		}
		return ret;
	}
}
