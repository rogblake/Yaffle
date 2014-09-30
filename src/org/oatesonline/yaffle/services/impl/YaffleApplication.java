package org.oatesonline.yaffle.services.impl;

import java.util.logging.Level;
import java.util.logging.Logger;




public class YaffleApplication  {

	Logger log = Logger.getLogger(YaffleApplication.class.getName());
	public YaffleApplication() {
//		super("org.oatesonline.yaffle.services.test");
		log.log(Level.INFO, ">>>>> Yaffle Application Initialized");
	}

}
