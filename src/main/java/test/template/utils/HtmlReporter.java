package test.template.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlReporter implements IReporter {
	private static final Logger log = LoggerFactory.getLogger(HtmlReporter.class);
	private static final String FAILED_REPORT_FILE = "failed.html";
	private static final String REPORT_TEMPLATE = "/report_template.html";

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		StringBuilder sb = new StringBuilder();
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> results = suite.getResults();
			for (Map.Entry<String, ISuiteResult> result : results.entrySet()) {
				String report = buildTestReport(result.getValue().getTestContext());
				sb.append(report);
			}
		}
		try {
			FileUtils.writeToFile(new File(FAILED_REPORT_FILE), sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String buildTestReport(ITestContext test) {
		IResultMap failedTests = test.getFailedTests();
		for (ITestResult r : failedTests.getAllResults()) {
			r.setAttribute("exception", buildException(r.getThrowable()));
		}
		return buildReport(test);
	}

	private String buildException(Throwable t) {
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		return writer.toString();
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
