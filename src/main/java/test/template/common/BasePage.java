package test.template.common;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base page
 * 
 */
public abstract class BasePage {
	protected RemoteWebDriver driver;
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	public BasePage(RemoteWebDriver driver) {
		this.driver = driver;
	}
}
