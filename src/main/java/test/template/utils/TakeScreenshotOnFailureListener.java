package test.template.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import test.template.common.Config;

public class TakeScreenshotOnFailureListener extends TestListenerAdapter {

	private static final Logger log = LoggerFactory.getLogger(TakeScreenshotOnFailureListener.class);
	
	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		log.info("Test Fail: " + tr);
		SeleniumUtils.takeScreenShot(Config.driver());
	}


}
