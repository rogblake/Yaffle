package org.oatesonline.yaffle.entities.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Id;

import org.oatesonline.yaffle.entities.IFixture;
import org.oatesonline.yaffle.entities.ITeam;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;

@Entity
@Cached
public class Fixture extends DTOEntity implements IFixture {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7545849840056449412L;

	@Id
	private Long id;
	
	private Calendar dateTime;
	
	
//	@Override
//	public Long getId() {
//		// TODO Auto-generated method stub
//		return id;
//	}
//
//	@Override
//	public void setId(Long id) {
//		this.id = id;
//		
//	}


	@Override
	public String toJSONString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDateTime() {
		// TODO Auto-generated method stub
		Calendar c = new GregorianCalendar();
		return c.getTime();
	}

	@Override
	public Team getHomeTeam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Team getAwayTeam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDateTime(Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHomeTeam(ITeam homeTeam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAwayTeam(ITeam awayTeam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setURL(String url) {
		// TODO Auto-generated method stub
		
	}



}
