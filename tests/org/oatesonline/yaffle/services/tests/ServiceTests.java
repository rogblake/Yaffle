package org.oatesonline.yaffle.services.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestCreateUser.class,
	TestGetPlayer.class
})
public class ServiceTests {

}
