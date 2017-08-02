package test.template.cases.base;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import test.template.common.Config;
import test.template.utils.TakeScreenshotOnFailureListener;

@Listeners(TakeScreenshotOnFailureListener.class)
public abstract class BaseWebTestCase extends BaseTestCase {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	// A Test Suite may have many tests.
	// A Test may run many test methods in one or more classes.
	// Before a Test startup, setup the browser and environment.
	// When the Test finished, close the browser.
	@BeforeTest
	@Parameters({ "browser", "url" })
	public void setupDriver(@Optional String browser, @Optional String url) throws Exception {
		Config.setupWebDriver(browser, url);
		log.info("Setup web driver: " + browser);
	}
	
	@AfterTest
	public void tearDown() {
		super.tearDown();
		if (Config.driver() != null) {
			Config.driver().quit();	
		}
	}

	protected RemoteWebDriver driver() {
		return Config.driver();
	}
}
