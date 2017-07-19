package test.template.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.template.common.Config;


public class SeleniumWaitUtils {
	private static final Logger log = LoggerFactory.getLogger(SeleniumUtils.class);

	private static int timeoutInSeconds = Config.getIntProperty("selenium.wait.timeout.seconds", 10);
	   

	public static final void waitForPageLoadComplete() {
		log.info("Wait for page load complete");
		ExpectedCondition<Boolean> loadComplete = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((RemoteWebDriver)driver).executeScript("return document.readyState").equals("complete");
			}
		};
		new WebDriverWait(Config.driver(), timeoutInSeconds).until(loadComplete);
	}

	public static final WebElement waitForElement(final By by) {
		return waitForCondition(Config.driver(), timeoutInSeconds, ExpectedConditions.presenceOfElementLocated(by));
	}
	
	public static final WebElement waitForElement(WebElement parent, final By by) {
		return waitForCondition(Config.driver(), timeoutInSeconds, 
				ExpectedConditions.presenceOfNestedElementLocatedBy(parent, by));
	}

	public static final WebElement waitForElementClickable(final By by) {
		return waitForCondition(Config.driver(), timeoutInSeconds, ExpectedConditions.elementToBeClickable(by));
	}
	
	public static final WebElement waitForElementClickable(WebElement parent, final By by) {
		WebElement element = waitForElement(parent, by);
		return waitForCondition(Config.driver(), timeoutInSeconds, 
				ExpectedConditions.elementToBeClickable(element));
	}


	public static final Alert waitForAlert() {
		return waitForCondition(Config.driver(), timeoutInSeconds, ExpectedConditions.alertIsPresent());
	}
	
	public static final <T> T waitForCondition(WebDriver driver,
			int timeoutInSeconds, ExpectedCondition<T> condition) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		return wait.until(condition);
	}
	
}
