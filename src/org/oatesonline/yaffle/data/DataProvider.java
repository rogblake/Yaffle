package org.oatesonline.yaffle.data;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public abstract class DataProvider {
	

	{
		PROPS = java.util.ResourceBundle.getBundle(THIS_PACKAGE + ".DataProviders");
		
	}
	private static final String THIS_PACKAGE = "org.oatesonline.yaffle.data";
	private static final long serialVersionUID = 1767482222580589902L;
	private static final String TARGET_URL="TARGET_URL";
	private static final String SERVICE_ACCESS_PROPS="ServiceAccess";
	
	protected static ResourceBundle PROPS = java.util.ResourceBundle.getBundle(THIS_PACKAGE + ".DataProviders");
	protected Vector<String> leagueCodes ;
	protected static String provider_key = "FOOTBALL_DATA"; // Add default Data Provider here.
//	protected static String provider_key = "XSCORES_MOBILE"; // Add default Data Provider here.

	
	private String targetURL;

	protected Logger log = Logger.getLogger(DataProvider.class.getName());
	
/** Used to store the active Service Provider key, that is used to locate the target Service Provider's data from file.
 *
 */
	private String dataProvider;
	
	private ResourceBundle dataProviderBundle; 
	
	/**
	 * The complete set of keys loaded at boot time. Used to validate service providers instantiated by subclases.
	 */
	protected static Vector<String> providerKeys;
	
	/* 
	 * called at boot to populate the list of service providers. 
	 */
	{
		providerKeys = new Vector<String>();
		Enumeration<String> enumKeys = PROPS.getKeys();
		if (null != enumKeys){
			do{
				providerKeys.add(enumKeys.nextElement());
			} while (enumKeys.hasMoreElements());
		}
	}
	
	protected DataProvider (){
		//set default Service Provider
		dataProvider = providerKeys.get(3);
		setProviderBundle(dataProvider);
		log.log(Level.CONFIG, "Data Provider defaulting to " + dataProvider);
		init();
	}
	
	protected DataProvider (String providerKey){
		this();
		if (providerKeys.contains(providerKey)){
			String dpStr = PROPS.getString(providerKey);
			dataProviderBundle = PropertyResourceBundle.getBundle(THIS_PACKAGE + "." + dpStr);
			
			if (null != dataProviderBundle){
				this.targetURL = dataProviderBundle.getString(TARGET_URL);
				log.log(Level.CONFIG, "Data Provider not set." );
			} else {
				log.log(Level.SEVERE, "Desired Data Provider not found. Setting to default - " + dataProvider);
			}
			setProviderBundle(providerKey);
			init();
		}
		//else we are stuck with the defaults
	}
	
	/**
	 * Use this in <code>ILeagueDataProvider</code> instances when acquiring raw data.
	 * @param leagueCode
	 * @return a string of unprocessed, raw data to be processed by the calling ILeagueDataProvder
	 * @throws LeagueURLNotFoundException 
	 */
	protected String  getRawLeagueTableData (String leagueCode) throws LeagueURLNotFoundException{
		
		String rawLeagueData = null;
		String path = getLeagueURL (leagueCode);
			if (null != path){
				
				rawLeagueData = storeFile (getCharData(path));
			}
		
		return rawLeagueData;
		
	}
	
	protected String getLeagueURL (String leagueCode){
		String ret = dataProviderBundle.getString(leagueCode.toUpperCase()+ "_URL");
		return ret;
	}

	private void init(){
		targetURL = dataProviderBundle.getString(TARGET_URL);
		//initialize the league codes  vector
		leagueCodes = new Vector<String>();

		leagueCodes.add("e1");
		leagueCodes.add("e2");
		leagueCodes.add("e3");
		leagueCodes.add("e4");
		leagueCodes.add("s1");
		leagueCodes.add("s2");
		leagueCodes.add("s3");
		leagueCodes.add("s4");
	}
	
	/**
	 * @return the dataProvider
	 */
	protected String getDataProvider() {
		return dataProvider;
	}
	/**
	 * @param serviceProvider the dataProvider to set
	 */
	protected void setServiceProvider(String serviceProvider) {
		if (providerKeys.contains(serviceProvider)){			
			this.dataProvider = serviceProvider;
			setProviderBundle(serviceProvider);
		}
	}
	
	

	
	protected ResourceBundle getProviderBundle(){
		return this.dataProviderBundle;
	}
	private void setProviderBundle(String spKey){
		ResourceBundle rb = null;
		if (null !=spKey){
			String sp = PROPS.getString(spKey);
			rb = java.util.ResourceBundle.getBundle(THIS_PACKAGE + "." + sp);
		}
		this.dataProviderBundle =rb;	
	}
	

	
	///////// Below is old redundant Code ///////////
	/**
	 * 
	 * @return a Buffered input stream containing the contents of the page 
	 */
	protected BufferedInputStream getCharData(String urlString){
		BufferedInputStream buf = null;
		log.log(Level.FINEST, "Attempting to read from [" + urlString + "]");
		try{
			URL url = new URL(urlString);
			URLConnection con = url.openConnection();
			buf = new BufferedInputStream( con.getInputStream(), 40000);
		} catch (java.net.MalformedURLException muEx) {
			muEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return buf;
	}
	
	protected String storeFile (BufferedInputStream bis){

		StringBuffer sb = new StringBuffer();
		try{
			int iRead = bis.read();
			while(iRead != -1) {		
				iRead = bis.read();
				sb.append((char) iRead);			
			}
			log.finest( "Web Page Read: " + sb.length() + " bytes");
		}catch (IOException ioEx){
				ioEx.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 *  Each service provider contains a fixed set of keys that map to league and possibly result
	 *  data 
	 * @param key  a key mapping to the target league
	 * @return a raw document containing all data for the target league.
	 */
	protected String getResource(String key) {
		String urlString = dataProviderBundle.getString(key);
		String dp = dataProviderBundle.getString("PROVIDER_NAME");
		String ret = null;
		if (null != urlString){	

			Client client = new Client(new Context(), Protocol.HTTP);
			ClientResource clientResource = new ClientResource(urlString);
			clientResource.setNext(client);
			
			try {
				Representation r = clientResource.get();
				String charset = r.getCharacterSet().getName();
				log.log(Level.INFO, "CHARSET FOR INCOMING Request= " + charset);
				ret = r.getText();
			} catch (ResourceException e) {
				log.log(Level.SEVERE, "Exception encountered retrieving " + key + " from " + "  " + dataProvider + " on the following URL\n " + urlString);
				e.printStackTrace();
			} catch (IOException e) {				
				log.log(Level.SEVERE, "IO Exception encountered retrieving " + key + " from " + dataProviderBundle.getString("PROVIDER_NAME") + " on the following URL\n " + urlString);
				e.printStackTrace();
			}
//			BufferedInputStream bis = getCharData (urlString);
//			if (null != bis){
//				return storeFile (bis);
//			}
		}
		return ret;
	}
	
	
	////ABove is old Code ///////////////
	
}
