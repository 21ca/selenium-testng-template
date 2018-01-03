package test.template.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import freemarker.template.Configuration;
import freemarker.template.Template;
import net.coobird.thumbnailator.Thumbnails;
import test.template.common.Config;

public class HtmlReporter extends TestListenerAdapter implements IReporter {
	private static final Logger log = LoggerFactory.getLogger(HtmlReporter.class);
	private static final String FAILED_REPORT_FILE = "report.html";
	private static final String REPORT_TEMPLATE = "/report_template.html";

	private Set<ITestResult> results = new HashSet<>();
	
	@Override
	public void onTestStart(ITestResult result) {
		super.onTestStart(result);
		MTLogBuffer.clear();
	}
		
	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		if (results.contains(tr)) {
			return;
		}
		results.add(tr);
		
		log.info("Test Fail: " + tr.getMethod().getMethodName());
		
		takeScreenshot(tr);
		setLog(tr);
	}
	
	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		if (results.contains(tr)) {
			return;
		}
		results.add(tr);
		
		takeScreenshot(tr);
		setLog(tr);
	}

	private void setLog(ITestResult tr) {
		StringWriter exception = new StringWriter();
		if (tr.getThrowable() != null) {
			tr.getThrowable().printStackTrace(new PrintWriter(exception));
		}
		tr.setAttribute("log", MTLogBuffer.getStringLog() + exception);
	}

	private void takeScreenshot(ITestResult tr) {
		if (Config.driver() != null) {
			String file = SeleniumUtils.takeScreenShot();
			String imageFile = SeleniumUtils.SCREENSHOT_PATH + file;
			String thumbFile = SeleniumUtils.SCREENSHOT_PATH + "thumb_" + file;
			buildThumbImage(imageFile, thumbFile);
			tr.setAttribute("screenshot", imageFile);
			tr.setAttribute("thumbScreenshot", thumbFile);
		}
	}

	private void buildThumbImage(String imageFile, String thumbFile) {
		try {
			Thumbnails.of(imageFile).size(200, 200).toFile(thumbFile);
		} catch (IOException e) {
			log.info("Create thumbnail image failed.", e);
		};
	}

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		StringBuilder sb = new StringBuilder();
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> results = suite.getResults();
			for (Map.Entry<String, ISuiteResult> result : results.entrySet()) {
				String report = buildReport(result.getValue().getTestContext());
				sb.append(report);
			}
		}
		try {
			FileUtils.writeToFile(new File(FAILED_REPORT_FILE), sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String buildReport(ITestContext test) {
		try {
			InputStream is = HtmlReporter.class.getResourceAsStream(REPORT_TEMPLATE);
			Configuration cfg = new Configuration(Configuration.getVersion());
			Template template = new Template("Report", new InputStreamReader(is), cfg);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("context", test);
			StringWriter writer = new StringWriter();
			template.process(parameters, writer);
			return writer.toString();
		} catch (Exception e) {
			log.error("Build Report error", e);
			return "Exception: " + e.getMessage();
		}
	}
	
}
