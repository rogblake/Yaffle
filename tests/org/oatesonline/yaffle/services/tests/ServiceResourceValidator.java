package org.oatesonline.yaffle.services.tests;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ServiceResourceValidator {
	
	private static Logger log = Logger.getLogger(ServiceResourceValidator.class.getName());

	
	/*
	 * Calls the url with a get passing in headers
	 */
	public static int get(String url, Map<String, String> headers) {
		String urlString = url;
		int ret = 0;
		if (null != urlString){	

			Client client = new Client(new Context(), Protocol.HTTP);
			ClientResource clientResource = new ClientResource(urlString);
			if  (null != headers){
				Iterator<String> it = headers.keySet().iterator();
				String key = null;
				while (it.hasNext()){		
					key = it.next();
					clientResource.setAttribute( key, headers.get(key));
				}
			}
			clientResource.setNext(client);
			
			try {
				Representation r = clientResource.get();
				String charset = r.getCharacterSet().getName();
				log.log(Level.INFO, "CHARSET FOR INCOMING Request= " + charset);
				ret = clientResource.getResponse().getStatus().getCode();
			} catch (ResourceException e) {
				log.log(Level.SEVERE, "IO Exception encountered retrieving  from the following URL\n " + urlString);
				e.printStackTrace();
			} 
		}
		return ret;	
	}

}
