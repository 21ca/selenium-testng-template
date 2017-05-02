package test.template.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.util.RetryAnalyzerCount;

/**
 * An implementation of IRetryAnalyzer that allows you to specify the maximum
 * number of times you want your test to be retried.
 */
public final class TestngRetry extends RetryAnalyzerCount {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public TestngRetry() {
		setCount(1);
	}

	@Override
	public boolean retryMethod(ITestResult result) {
		log.info("Retry...");
		return !result.isSuccess();
	}

}
