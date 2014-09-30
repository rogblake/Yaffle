package org.oatesonline.yaffle.data;

import java.util.logging.Logger;

import org.oatesonline.yaffle.entities.impl.League;

public class BBCDataProvider extends DataProvider implements ILeagueDataProvider {



	/* (non-Javadoc)
	 * @see org.oatesonline.yaffle.data.ILeagueDataProvider#getLeague(java.lang.String)
	 */
	@Override
	public League getLeague(String leagueCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public BBCDataProvider() {
		this ("BBC_MOBILE");
	}

	protected BBCDataProvider(String providerKey) {
		super(providerKey);
		log = Logger.getLogger(BBCDataProvider.class.getName());
	}


}
