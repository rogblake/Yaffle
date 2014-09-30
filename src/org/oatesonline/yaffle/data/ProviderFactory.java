package org.oatesonline.yaffle.data;

import java.util.logging.Logger;

public class ProviderFactory {
	
	private static IDataProvider currentDp = null;
	
	private static Logger log = Logger.getLogger(ProviderFactory.class.getName());
	
	public static IDataProvider getDataProvider(String providerKey){
		if (null == currentDp){
			currentDp = initialize(providerKey);
		}
		return currentDp;
	}
	
	private ProviderFactory(){
		
	}

	
	private static IDataProvider initialize(String pKey){
		IDataProvider ret = null;
		switch(pKey.hashCode()){
//			case("".hashCode()):{
//				
//			}
//			case("".hashCode()):{
//				
		}
		
		return ret;
	}
}
	

