package org.oatesonline.yaffle.services.impl;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.oatesonline.yaffle.entities.impl.Player;
import org.restlet.data.Cookie;
import org.restlet.data.Status;
import org.restlet.data.Header;
import org.restlet.engine.header.HeaderUtils;
import org.restlet.representation.Representation;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

public class YaffleService extends ServerResource {
	
	Logger log = Logger.getLogger(YaffleService.class.getName());
	
	protected static final ResourceBundle serviceProps = ResourceBundle.getBundle("org/oatesonline/yaffle/services/ServiceProps");
	protected final static String YAFFLE_UID = serviceProps.getString("COOKIE_YAFFLE_UID");

	private static final String RESTLET_HEADERS= "org.restlet.http.headers";
	private static final String DEBUG = serviceProps.getString("IS_DEBUG");

	public static boolean IS_DEBUG = true;

	{
		if ("true".equals(DEBUG.toLowerCase())){
			IS_DEBUG=true;
		} else {
			IS_DEBUG=false;
		}
	}
	
	@Options
	public void doOptions() {
	    setResponseHeaderValueFor("Access-Control-Allow-Origin"); 
	    setResponseHeaderValueFor("Access-Control-Allow-Methods");
	    setResponseHeaderValueFor("Access-Control-Allow-Headers"); 
	    setResponseHeaderValueFor("Access-Control-Allow-Credentials"); 
	    setResponseHeaderValueFor("Access-Control-Max-Age"); 
	}	
	
	protected void addCORSSupport(){
	    setResponseHeaderValueFor("Access-Control-Allow-Origin");
	}
	
	protected String getRequestHeaderValue(String headerKey){
		String ret = "";
	    Series<Header> requestHeaders = (Series<Header>) getRequest().getAttributes().get(RESTLET_HEADERS); 
	    if (requestHeaders != null) { 
	    	ret = requestHeaders.getFirst(headerKey).getValue(); 
	    } 		
		return ret;
	}
	
	//adds a header to the response by looking up it up from the ServiceProperties file
	protected void setResponseHeaderValueFor(String headerKey){
		String ret = "";
	    Series<Header> responseHeaders = (Series<Header>) getResponse().getAttributes().get(RESTLET_HEADERS); 
	    if (responseHeaders != null) { 
	    	responseHeaders.add(headerKey, getResponseHeaderValueFor(headerKey)); 
	    } 
	}
	protected Player validateUser(String playerID){	
		String pID;
		if (IS_DEBUG && (playerID != null)){
			Player player = Utils.retrievePlayer(playerID);
			if (null!= player){
				return player;
			}
		}
		Cookie yuid = getRequest().getCookies().getFirst(serviceProps.getString("COOKIE_YAFFLE_UID"));
		if (null != yuid){
			pID = yuid.getValue();
			Player player = Utils.retrievePlayer(pID);
			if (player == null){
				getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				return null;
			} else {
				getResponse().setStatus(Status.SUCCESS_OK);
				return player;
			}
		}
		return null;
	}
	
	// Gets the header value from the service properties file for the given headerKey.
	protected String getResponseHeaderValueFor(String headerKey) {
		String ret = getPropertyValue(headerKey);
		return ret;
	}
	
	protected String getPropertyValue(String propKey){
		String ret = serviceProps.getString(propKey);
		return ret;
	}
	/**
	 * 
	 * Returns the value given by a URL slug, as defined in the Restlet Routing config for the app
	 * E.g. /my/{playerId}/{teamId}/
	 * 
	 * @param  The name of the variable passed in through the URL Slug
	 * @return The value of the URL slug parameter
	 */
	protected String getRESTParamFromURL(String param){
		String ret = null;
		ret = (String) getRequest().getAttributes().get(param);
		return ret;
	}
	
	/**
	 * 
	 * @param player
	 * @param playerId
	 * @param teamUpdateWindow If set to true, then the check will include to check if the action is team update window is open or not. If set to false, this test is ignored
	 * If the result of the test is false, then this method returns false.
	 * @param subUpdateWindow If set to true, this method checks that there is a valid substitution window open. If  
	 * @return a boolean indicating whether or not the action should be allowed to be executed or not
	 */
	protected boolean isActionAuthorized(Player player, Long playerId, boolean teamUpdateWindow, boolean subUpdateWindow){
		String msg = "";
		boolean p = false;
		if (null == player) {
			p= false;
			msg="Unknown User";
		} else if (null == playerId) {
			p = false;
			msg="User is not Logged In";
		} else {
			p = player.getId().equals(playerId);
		}
		boolean tu = true;//TODO
		boolean su = true;//TODO
		
		boolean ret =  p && tu && su;
		if (!ret){			
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED, msg);
		} else {			
			getResponse().setStatus(Status.SUCCESS_OK, msg);
		}
		return ret;
	}
	
	private Series<Header> getResponseHeaders(){
		Series<Header> responseHeaders = (Series<Header>) getResponse().getAttributes().get(RESTLET_HEADERS); 
		if (null == responseHeaders){
			HeaderUtils.addResponseHeaders(getResponse(), new Series<Header>(null));
		}
		return responseHeaders;
	}
	
/**
 * @return the decoded, and 'fixed' POST payload data as a decoded string.
 * It also strips out the trailing '=' char that seems to be present in POST data
 */
	protected String decodePostData(Representation rep){
		String payloadData = null;
		try{
			String data = rep.getText();
			//POST weirdness workaround.  '=' is appended to JSON data. Need to strip it out.
			if (data.endsWith("=")){
				data=data.substring(0, data.length()-1);
			}
			payloadData = java.net.URLDecoder.decode(data, "UTF-8");
		} catch (IOException ioEx) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE, "Invalid Team data recevied for Player ");
		}
		return payloadData;
	}
}
