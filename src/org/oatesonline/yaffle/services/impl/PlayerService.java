package org.oatesonline.yaffle.services.impl;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOPlayer;
import org.oatesonline.yaffle.entities.impl.Player;
import org.oatesonline.yaffle.services.IPlayerService;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import com.googlecode.objectify.Key;


public class PlayerService extends YaffleService implements IPlayerService {

	Logger log = Logger.getLogger(PlayerService.class.getName());
	
	/**
	 * 
	 * @return A JSON String representation of the logged in player, along with all his/her teams.
	 * @throws ResourceException
	 */
	/* (non-Javadoc)
	 * @see org.restlet.resource.ServerResource#get()
	 */
	@Get
	public String getPlayer() throws ResourceException {
		//initializePlayers();
		//This is incorrect behaviour.
		// The method should determine if {playerid} is present in the URL
		// If so it should retreive that player, 
		// If not it should default to the coookie
		String playerID = getRESTParamFromURL("playerId");
		String plyrStr = null;
		Player player = validateUser(playerID);
		if (null != player){
//			plyrStr = player.toXMLString();
			plyrStr = player.toJSONString();
		}
		return plyrStr;
	}


	/* 
	 * Use this to create an 'empty' player 
	 * {
	 * 	"email":"jim@abc.com",
	 * 	"pin":"12334",
	 * 	"name":Jim Smith" //Optional
	 * }
	 */
	@Post("application/json")
	public String addPlayer(Representation entity)
			throws ResourceException {
		DAOPlayer  daoP= DAOFactory.getDAOPlayerInstance();
		Player ret = null;

		String jsonData = decodePostData(entity);
		JSONParser parser=new JSONParser();
		Object obj = null;
		Map pData = null;
		try {
			 obj=parser.parse(jsonData);
			 pData = (Map) obj;
			 if (null != pData){
				 String name =(String) pData.get((String) "name");
				 String nickname = (String) pData.get((String) "nickname");
				 //The following two fields cannot be null
				 String email =(String) pData.get((String) "email");
				 String pin =(String) pData.get((String) "pin");
				 if ((null != email) && (null != pin)){
					 
					 ret = new Player (name,email, pin );
					 
					 if (null != ret){
						 ret.setNickname(nickname);
						 Key<Player> kPlayer = daoP.add(ret);
						 if (null != kPlayer){
							 Long id = kPlayer.getId();
							ret = (Player)  daoP.getPlayer(new Long(id));
						 }
					 }
				 }
			 }
		} catch (ParseException e) {
			String errMsg = "Error Parsing Player JSON";
			log.log(Level.FINEST, errMsg);
			e.printStackTrace();
			getResponse().setStatus(Status.CLIENT_ERROR_FAILED_DEPENDENCY, errMsg);
		} 
		
		if (null != ret){		
			getResponse().setStatus(Status.SUCCESS_CREATED);
			return ret.toJSONString();
		}
		return "";
	}
	
	/* Used for creating test data 
	 * 
	 */
	private void initializePlayers(){
		DAOPlayer  daoP= DAOFactory.getDAOPlayerInstance();
		daoP.addPlayer("Betty Rubble", "betty@bedrock.com", "BettyBoo", "fred");
		daoP.addPlayer("Barney Rubble", "barneyy@bedrock.com", "Hey Hey Hey", "fred");
		daoP.addPlayer("Dino", "dino@bedrock.com", "Dino Dawg", "fred");
		daoP.addPlayer("Fred Flintstone", "fred@bedrock.com", "Yabadabadoo!", "fred");
		daoP.addPlayer("Wilma Flintstone", "wilma@bedrock.com", "Leopardskin Lena", "fred");

	}
}
