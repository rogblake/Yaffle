package org.oatesonline.yaffle.entities.impl;

import java.util.Date;

import javax.persistence.Id;

import org.oatesonline.yaffle.entities.IResult;
import org.oatesonline.yaffle.entities.ITeam;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class Result extends DTOEntity implements IResult {
	
	@Id 
	private Long Id;
	
	private short result;
	
	private int homeScore;
	
	private int awayScore;

	private Date date;
	
	private Key<Team> homeTeam;
	
	private Key<Team> awayTeam;
		
	private String urlStr;
	
 
	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IDataObject#getId()
	 */

	public Long getId() {
		// TODO Auto-generated method stub
		return Id;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IDataObject#setId(java.lang.Long)
	 */

	public void setId(Long id) {
		Id = id;
		
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IDataObject#toJSONString()
	 */
	@Override
	public String toJSONString() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#getDateTime()
	 */
	@Override
	public Date getDateTime() {
		// TODO Auto-generated method stub
		return date;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#getHomeTeam()
	 */
	@Override
	public ITeam getHomeTeam() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#getAwayTeam()
	 */
	@Override
	public ITeam getAwayTeam() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#getURL()
	 */
	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return urlStr;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#getHomeScore()
	 */
	@Override
	public int getHomeScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#getAwayScore()
	 */
	@Override
	public int getAwayScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#getResult()
	 */
	@Override
	public short getResult() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#setDateTime(java.util.Date)
	 */
	@Override
	public void setDateTime(Date date) {
		this.date = date;
		
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#setHomeTeam(org.oatesonline.yaffle.entities.ITeam)
	 */
	@Override
	public void setHomeTeam(ITeam homeTeam) {
			if (null != homeTeam && homeTeam instanceof Team){
				Team t = (Team) homeTeam;
		//		this.homeTeam = Key.create();
			}	
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#setAwayTeam(org.oatesonline.yaffle.entities.ITeam)
	 */
	@Override
	public void setAwayTeam(ITeam awayTeam) {
//		this.awayTeam = Key.create((Team) awayTeam);
		
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#setURL(java.lang.String)
	 */
	@Override
	public void setURL(String url) {
		this.urlStr = url;
		
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#setHomeScore(int)
	 */
	@Override
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
		
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#setAwayScore(int)
	 */
	@Override
	public void setAwayScore(int awayScore) {
		this.awayScore= awayScore;
		
	}

	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.entities.IResult#setResult(short)
	 */
	@Override
	public void setResult(short result) {
		this.result = result;
		
	}


}
