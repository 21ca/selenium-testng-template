package test.template.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * An implementation of IRetryAnalyzer that allows you to specify the maximum
 * number of times you want your test to be retried.
 */
public final class TestngRetry implements IRetryAnalyzer {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private Map<Integer, Integer> retryCounts = new HashMap<>();
	private int retryCount = 2;
	
	@Override
	public synchronized boolean retry(ITestResult result) {
		//Failed and parameters has not retry.
		if (!result.isSuccess() && getRetriedCount(result) < retryCount) {
			log.info("Retry method [" + result.getMethod().getMethodName() + "] with parameters:"
					+ Arrays.toString(result.getParameters()));
			result.setStatus(ITestResult.SKIP);
			increaseRetryCount(result);
			return true;
		}
		return false;
	}

	private void increaseRetryCount(ITestResult result) {
		int key = getKey(result);
		retryCounts.put(key, getRetriedCount(result) + 1);
	}

	private int getKey(ITestResult result) {
		Object[] params = result.getParameters();
		if (params == null || params.length == 0) {
			return 0;
		} else {
			int key = result.getMethod().hashCode();
			for (Object param : params) {
				key += param.hashCode();
			}
			return key;
		}
	}

	private int getRetriedCount(ITestResult result) {
		int key = getKey(result);
		Integer count = retryCounts.get(key);
		return count != null ? count : 0;
	}
}
