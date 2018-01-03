package test.template.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * An implementation of IRetryAnalyzer that allows you to retry once
 */
public final class TestngRetryOnce implements IRetryAnalyzer {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private List<Object[]> paramsHasRetried = new ArrayList<>();
	
	@Override
	public synchronized boolean retry(ITestResult result) {
		//Failed and parameters has not retry.
		if (!result.isSuccess() && !hasRetried(result.getParameters())) {
			log.info("Retry method [" + result.getMethod().getMethodName() + "] with parameters:"
					+ Arrays.toString(result.getParameters()));
			result.setStatus(ITestResult.SKIP);
			paramsHasRetried.add(result.getParameters());
			return true;
		}
		return false;
	}

	private boolean hasRetried(Object[] parameters) {
		for (Object[] p : paramsHasRetried) {
			if (Arrays.equals(p, parameters)) {
				return true;
			}
		}
		return false;
	}

}

