package org.oatesonline.yaffle.data;

import java.net.URL;

public interface IDataProvider {
	
	/**
	 * The Root URL of the data provider 
	 * @return
	 */
	public URL getURL();
	
	public String getAttributionString();
	
	public String getName();

}
