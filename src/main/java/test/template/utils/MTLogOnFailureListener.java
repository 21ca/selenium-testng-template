package test.template.utils;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class MTLogOnFailureListener extends TestListenerAdapter {
	
	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		tr.setAttribute("log", MTLogBuffer.getStringLog());
		tr.setAttribute("exception", tr.getThrowable());
		MTLogBuffer.clear();
	}
}
