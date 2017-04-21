package test.template.utils;

import org.testng.ITestResult;
import org.testng.util.RetryAnalyzerCount;

/**
 * An implementation of IRetryAnalyzer that allows you to specify the maximum
 * number of times you want your test to be retried.
 */
public class TestngRetry extends RetryAnalyzerCount {
	
	public TestngRetry() {
		setCount(1);
	}

	@Override
	public boolean retryMethod(ITestResult result) {
		System.out.println("retryMethod");
		return !result.isSuccess();
	}

}
