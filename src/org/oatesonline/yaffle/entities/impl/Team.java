package org.oatesonline.yaffle.entities.impl;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONObject;
import org.oatesonline.yaffle.entities.ITeam;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;


@Entity
@XmlRootElement
@Cached
public class Team extends DTOEntity implements ITeam,  Comparable<Team> {
	@Transient
	Logger log = Logger.getLogger("Team");
	
//	private Long Id;
	@Element
	private short pos;
	
	@Element
	private int pts;
	
	@Element
	private int gd;
	
	@Element
	private int gf;
	
	@Element
	private int ga;

	@Element
	private short pld;
	
	@Attribute(name="webpage", required=false)
	private String urlStr;
	
	@javax.persistence.Id
//	@Persistent
	@Indexed
	@Attribute(name="id", required=true)
	private String name;
	
	@Indexed
	@Attribute(name="league", required=true)
	private String leagueCode;
	
	public Team (){}
	
	private Team (String name ){
		this();
		this.name = name;		
	}
	
	@SuppressWarnings("unused")
	private Team (String name, String leaguecode){
		this(name);
		this.leagueCode = leaguecode;
	}
	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IDataObject#getId()
	 */
//	@Override
//	public Long getId() {
//		// TODO Auto-generated method stub
//		return Id;
//	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IDataObject#setId(java.lang.Long)
	 */
//	@Override
//	public void setId(Long id) {
//		this.Id = id;
//	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IDataObject#toXMLString()
	 */
	@Override
	public String toXMLString() {
		Serializer ser = new Persister();
		StringWriter sw = new StringWriter();
		try {
			ser.write(this, sw);
		} catch (Exception e) {
			log.log(Level.FINE, "Error writing XML for league <" + this.name + ">");
			e.printStackTrace();
		}
		return sw.toString();
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IDataObject#toJSONString()
	 */
	@Override
	public String toJSONString() {
		return toJSONObject().toJSONString();
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject(){
		JSONObject jObj = new JSONObject();
		jObj.put("leagueCode", this.leagueCode);
		jObj.put("name", this.name);
		jObj.put("pos", this.pos);
		jObj.put("pld", this.pld);
		jObj.put("gd", this.gd);
		jObj.put("pts", this.pts);
		jObj.put("urlString", this.urlStr);
		
		return jObj;
		
	}
	public String toString(){
		return toJSONString();
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.ITeam#compareTo(org.oatesonline.yaffle.entities.ITeam)
	 */
	@Override
	public int compareTo(Team otherTeam) {
		
//		if (this.pos > otherTeam.getPos()){
//			return 1;
//		}
//		if (this.pos < otherTeam.getPos()){
//			return -1;
//		}
		if (this.pts > otherTeam.getPts())
			return -1;
		if (this.pts < otherTeam.getPts())
			return 1;

		if (this.gd > otherTeam.getGD())
			return -1;
		if (this.gd < otherTeam.getGD())
			return 1;
	
		return this.name.compareTo(otherTeam.getName());
//		if (this.pos > otherTeam.getPos()){
//			return 1;
//		} else { 
//			return this.name.compareTo(otherTeam.name);
//		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getURL() {
		return this.urlStr;
	}

	@Override
	public short getPos() {
		return this.pos;
	}


	@Override
	public int getGD() {
		return this.gd;
	}

	@Override
	public short getPld() {
		return this.pld;
	}

	@Override
	public int getPts() {
		return this.pts;
	}

	@Override
	public String getLeagueCode(){
		return this.leagueCode;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;	
	}

	@Override
	public void setURL(String url) {
		this.urlStr = url;
	}

	@Override
	public void setPos(short pos) {
		this.pos = pos;	
	}

	@Override
	public void setPts(int pts) {
		this.pts = pts;	
	}

	@Override
	public void setGD(int gd) {
		this.gd = gd;
	}

	@Override
	public void setPld(short pld) {
		this.pld = pld;		
	}

	@Override
	public void setLeagueCode (String lc){
		this.leagueCode = lc;
	}
	
}