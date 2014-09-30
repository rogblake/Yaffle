package org.oatesonline.yaffle.entities;

import java.io.Serializable;

import org.json.simple.JSONAware;

public interface IDataObject extends Serializable, JSONAware {
	
//	public Long getId();
//	
//	public void setId(Long id);
	
	public abstract String toXMLString();
	
	public abstract String toJSONString();

}
