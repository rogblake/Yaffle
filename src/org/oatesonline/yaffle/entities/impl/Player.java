package org.oatesonline.yaffle.entities.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONObject;
import org.oatesonline.yaffle.entities.IPlayer;
import org.oatesonline.yaffle.entities.dao.DAOFactory;
import org.oatesonline.yaffle.entities.dao.DAOTeam;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Serialized;

@Entity
@XmlRootElement
@Cached
public class Player  extends DTOEntity implements IPlayer , Comparable{

//	Logger log = Logger.getLogger(Player.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6893007501145539201L;
		
	@Id
	@Element
	private Long id;
	
	@Attribute(name="name")
	private String name;
	

	@Element
	@Indexed
	private String email;
	
	@Attribute(name="nickname")
	private String nickname;
	
	private String pwd;
	
	@Element
	private short pld;
	
	@Element
	private short pos;
	
	@Element
	//Previous position
	private short prev;
	
	/**
	 * @return the prev
	 */
	public short getPrev() {
		return prev;
	}
	/**
	 * @param prev the prev to set
	 */
	public void setPrev(short prev) {
		this.prev = prev;
	}

	@Element
	private int gd;
	
	@Element
	private int pts;
	
	
//	@ElementList
//	private java.util.List<Key<Team>> teamKeys; 

	@Serialized
	private java.util.Map<String, Key<Team>> teamKeys;
	
	/**
	 * @return the pld
	 */
	public short getPld() {
		return pld;
	}

/**
	 * @param pld the pld to set
	 */
	public void setPld(short pld) {
		this.pld = pld;
	}
	/**
	 * @return the pos
	 */
	public short getPos() {
		return pos;
	}
	/**
	 * @param pos the pos to set
	 */
	public void setPos(short pos) {
		this.pos = pos;
	}
	/**
	 * @return the gd
	 */
	public int getGd() {
		return gd;
	}
	/**
	 * @param gd the gd to set
	 */
	public void setGd(int gd) {
		this.gd = gd;
	}
	/**
	 * @return the pts
	 */
	public int getPts() {
		return pts;
	}
	/**
	 * @param pts the pts to set
	 */
	public void setPts(int pts) {
		this.pts = pts;
	}
	
	private Player(){
		this.name = "";
		this.email = "";
		this.nickname = "";
		this.id = null;
		this.gd=0;
		this.pld=0;
		this.pts=0;
		this.pos=0;
		teamKeys = new TreeMap<String, Key<Team>>();
		
	}
	
	public Player(String name, String email, String pin){
		this();
		this.name = name ;
		this.email = email;
		this.pwd = pin;
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		 this.id = id;
	}

	@Override
	public String toXMLString() {
		Serializer ser = new Persister();
		StringWriter sw = new StringWriter();
		try {
			ser.write(this, sw);
		} catch (Exception e) {
//			log.log(Level.FINE, "Error writing XML for league <" + this.name + ">");
			e.printStackTrace();
		}
		return sw.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject jObj = new JSONObject();
		DAOTeam daoT = DAOFactory.getDAOTeamInstance();
		jObj.put("name", this.name);
		jObj.put("email", this.email);
		jObj.put("id", this.id);
		jObj.put("nickname", this.nickname);
		jObj.put("pld", this.pld);
		jObj.put("pos", this.pos);
		jObj.put("gd", this.gd);
		jObj.put("pts", this.pts);
		List<Team> teams = new LinkedList<Team>();
		Iterator<String> itLeague = teamKeys.keySet().iterator();
		Team t  = null;
		Key<Team> kTeam = null;
		while (itLeague.hasNext()){
			kTeam = teamKeys.get(itLeague.next());
			t  = daoT.getTeam(kTeam);
			if (null != t){
				teams.add(t);
			}
		}
		jObj.put("teams", teams);
		return jObj.toJSONString();
	}

	@Override
	public String getName() {
		
		return this.name;
	}

	@Override
	public String getEmail() {

		return this.email;
	}

	@Override
	public String getNickname() {
		
		return this.nickname;
	}

	@Override
	public Map<String,Key<Team>> getTeamKeys() {
		return teamKeys;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setPassword(String pwd){
		this.pwd = pwd;
	}
	
	public String getPassword(){
		return pwd;
	}
	
	public void addTeam(String leagueCode, Team team) {
		if (team != null){
			Key<Team> kTeam = new Key<Team>(Team.class, team.getName());
			//TODO test leagueCode is valid. Throw exception otherwise.
			this.teamKeys.put(leagueCode, kTeam);
		}
	}
	@Override
	public int compareTo(Object o) {
		
		if (o instanceof Player){
			Player p = (Player) o;
			if (this.pts > p.pts){
				return -1;
			}
			if (this.pts < p.pts){
				return 1;
			}
			
			if (this.gd > p.gd){
				return -1;
			}   
			if (this.gd < p.gd){
				return 1;
			} 
			
			if (this.pld < p.pld){
				return 1;
			}
			if (this.pld > p.pld){
				return -1;
			}
			return this.nickname.compareToIgnoreCase(p.nickname);
		}	
		return 0;
	}


}
