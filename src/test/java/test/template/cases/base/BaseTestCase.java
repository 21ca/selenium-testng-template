package test.template.cases.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import test.template.common.Browser;
import test.template.common.Settings;
import test.template.utils.TakeScreenshotOnFailureListener;

@Listeners(TakeScreenshotOnFailureListener.class)
public class BaseTestCase {
	private Properties testDataProps = new Properties();
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	// A Test Suite may have many tests.
	// A Test may run many test methods in one or more classes.
	// Before a Test startup, setup the browser and environment.
	// When the Test finished, close the browser.
	@BeforeTest
	@Parameters({ "browser", "url", "environment" })
	public void setup(@Optional String browser, @Optional String url, @Optional String environment) throws Exception {
		loadTestData();
	
		if (browser != null) {
			if (url != null) { // Remote browser
				if (browser.equalsIgnoreCase("ie")) {
					Browser.setupRemoteWebDriver(url, DesiredCapabilities.internetExplorer());
				} else if (browser.equalsIgnoreCase("firefox")) {
					Browser.setupRemoteWebDriver(url, DesiredCapabilities.firefox());
				} else if (browser.equalsIgnoreCase("chrome")) {
					Browser.setupRemoteWebDriver(url, DesiredCapabilities.chrome());
				} else {
					log.error("Browser not support: " + browser);
				}
			} else { // Local browser
				if (browser.equalsIgnoreCase("ie")) {
					Browser.setupInternetExplorer();
				} else if (browser.equalsIgnoreCase("firefox")) {
					Browser.setupFireFox();
				} else if (browser.equalsIgnoreCase("chrome")) {
					Browser.setupChrome();
				} else {
					log.error("Browser not support: " + browser);
				}
			}
		}
		
		Settings.setupEnvironment(environment);
		log.info("Environment of Automation test case running:" + Settings.getEnvironment());

		System.out.println("@BeforeTest");
	}

	private void loadTestData() throws IOException {
		Class<?> clazz = getClass();
		List<Class<?>> classes = new ArrayList<>();
		do {
			classes.add(clazz);
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class);
		
		Collections.reverse(classes);
		for (Class<?> c : classes) {
			InputStream is = c.getResourceAsStream(c.getSimpleName() + ".properties");
			if (is != null) {
				testDataProps.load(is);
				is.close();
			}
		}
	}

	@AfterTest
	public void tearDown() {
		System.out.println("@@AfterTest");
		Browser.quit();
	}

	protected RemoteWebDriver getDriver() {
		return Browser.getDriver();
	}
	
	protected String data(String key) {
		return testDataProps.getProperty(key);
	}
}
