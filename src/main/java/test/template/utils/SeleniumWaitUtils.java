package test.template.utils;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.template.common.Config;


public class SeleniumWaitUtils {
	private static final Logger log = LoggerFactory.getLogger(SeleniumUtils.class);

	private static int timeoutInSeconds = Config.getIntProperty("selenium.wait.timeout.seconds", 10);
	
    private static void waitForCondition(ExpectedCondition<?> condition) {
        waitForCondition(condition, timeoutInSeconds);
    }

    private static void waitForCondition(ExpectedCondition<?> condition, long timeoutInSeconds) {
        new WebDriverWait(Config.driver(), timeoutInSeconds).until(condition);
    }

    private static void waitForConditionIgnoring(ExpectedCondition<?> condition, Class<? extends Throwable> ignoring) {
        waitForConditionIgnoring(condition, ignoring, timeoutInSeconds);
    }

    private static void waitForConditionIgnoring(ExpectedCondition<?> condition, Class<? extends Throwable> ignoring,
            long timeoutInSeconds) {
        new WebDriverWait(Config.driver(), timeoutInSeconds).ignoring(ignoring).until(condition);
    }
}
