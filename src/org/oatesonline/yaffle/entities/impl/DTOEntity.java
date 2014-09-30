package org.oatesonline.yaffle.entities.impl;

import java.io.StringWriter;

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
//	private Logger log = Logger.getLogger(DTOEntity.class.getName());
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

}
