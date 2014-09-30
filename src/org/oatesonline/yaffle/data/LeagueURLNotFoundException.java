package org.oatesonline.yaffle.data;

public class LeagueURLNotFoundException extends Exception {
	
	private String leagueCode;

	
	public LeagueURLNotFoundException(String leagueCode) {
		super();
		this.leagueCode = leagueCode;
	}


	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return "Failed to  load league remote league resource URL with <" + leagueCode + "> leagueCode.";
	}
	
	

}
