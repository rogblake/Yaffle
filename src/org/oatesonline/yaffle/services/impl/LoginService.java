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
import org.restlet.data.CookieSetting;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

public class LoginService extends YaffleService {
	
	

	Logger log = Logger.getLogger(LoginService.class.getName());

	
	@Post
	public void doOptions(){
		super.doOptions();
	}
	
	@Post
	public String login(Representation rep)
			throws ResourceException {
		DAOPlayer daoP = DAOFactory.getDAOPlayerInstance();
		String pin= null;
		String email= null;
		String jsonData = null;
		String ret = ""; 
		try {
			jsonData = rep.getText();
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
		Player p = null;
		if (jsonData != null){
			JSONParser parser=new JSONParser();
			Object obj = null;
			Map pData = null;
			try {
				 obj=parser.parse(jsonData);
				 pData = (Map) obj;
				 if (null != pData){
					 email = (String) pData.get("email");
					 pin  = (String) pData.get("password");
					 p = (Player) daoP.findPlayer(email);
					 if (null != p){
						 String password = p.getPassword();
						 if (password != null){
							 if (password.equals(pin)){
								 getResponse().setStatus(Status.SUCCESS_OK);
								 getResponse().getCookieSettings().set(YAFFLE_UID, p.getId().toString());
								 ret= p.toJSONString();
							 } 	 else {
								getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
								return "Login Failed";
							}	
						 }
					 }
				 }
			} catch(ParseException pEx){
					 log.log(Level.SEVERE, pEx.getMessage());
			}
		} else {			
			getResponse().setStatus(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
			ret = "No login data was received by the server";
		}
		return ret;
	}
	
	@Get
	public void logout(){
		Player p= validateUser();
		if (p != null){
			Series<CookieSetting> cs = getResponse().getCookieSettings();
			cs.removeFirst(YAFFLE_UID);
			cs.add(YAFFLE_UID, "");
		}
	}
}