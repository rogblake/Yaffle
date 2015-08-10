package org.oatesonline.yaffle.services.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.oatesonline.yaffle.services.tests.Constants;

public class TestGetPlayer {
	
	private String url = "";

	@Before
	public void setUp() throws Exception {
		url = Constants.BASE_URL + "/league/";
	}

	@Test
	public void test() {
		int responseCode = ServiceResourceValidator.get(url,  null);
		fail("Not yet implemented");
	}

}
