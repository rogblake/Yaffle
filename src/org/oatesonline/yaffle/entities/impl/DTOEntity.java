package org.oatesonline.yaffle.entities.impl;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.datanucleus.store.types.sco.simple.GregorianCalendar;
import org.oatesonline.yaffle.entities.IDataObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
/**
 * Provides common representation functions to Data transfer objects (Entities)
 * 
 */
@SuppressWarnings("unchecked")
public abstract class DTOEntity implements IDataObject {

//	@Transient
	private Logger log = Logger.getLogger(DTOEntity.class.getName());
	@Override
	public String toXMLString() {
		Serializer ser = new Persister();
		StringWriter sw = new StringWriter();
		try {
			ser.write(this, sw);
		} catch (Exception e) {
//			log.log(Level.FINE, "Error writing XML for entitily <" + this.getClass().getName() + ">");
			e.printStackTrace();
		}
		return sw.toString();

	}

	@Override
	public abstract String toJSONString();// {
		// It would be good to implement this at this level using reflection and the persistence annotations to determine 
		//which String to encode, but this is out of scope for now. 
	//	return null;
	//}
	
	/**
	 *  Converts a IS8601 compliant String to a Java Calendar Object
	 * @param dateStr A valid ISO 8601 Formatted string
	 * @return A Calendar object representative of the <code>dateStr</code> String
	 */
	protected Calendar toCalendar(String dateStr){
		
		Calendar c = GregorianCalendar.getInstance();
		Date date = null;
		try{
			 date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateStr);
		} catch (ParseException pEx){
			log.log(Level.SEVERE,"Last update date not parsable for <" + dateStr + ">");
		}
		c.setTime(date);
		return c;
	}
	
	protected String fromCalendar (Calendar cal){
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a EEE, MMM d, ''yy");
		String ret = "";
		if (null != cal){	
			ret =  sdf.format(cal.getTime());
		}
		return ret;
	}

}
