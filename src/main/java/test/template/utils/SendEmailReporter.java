package test.template.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.mail.HtmlEmail;
import org.testng.ISuite;
import org.testng.reporters.EmailableReporter2;
import org.testng.xml.XmlSuite;

import test.template.common.Settings;

public class SendEmailReporter extends EmailableReporter2 {
	
	private StringWriter buffer = new StringWriter();
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		super.generateReport(xmlSuites, suites, outputDirectory);
		String content = buffer.getBuffer().toString();
		try {
			sendEmail(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected PrintWriter createWriter(String outdir) throws IOException {
		return new PrintWriter(buffer);
	}

	private void sendEmail(String content) throws Exception {
		System.out.println("Send Email:");
		if (!Settings.getBoolean("email.enabled")) {
			return;
		}
		
		HtmlEmail email = new HtmlEmail();
		for (String receipt : Settings.getArrayProperty("report.email.recipients")) {
			email.addTo(receipt);
		}
		email.setFrom(Settings.getProperty("report.email.from"));
		email.setSubject(Settings.getProperty("report.email.subject"));
		email.setHtmlMsg(content);
		email.setHostName(Settings.getProperty("report.email.host"));
		email.setAuthentication(Settings.getProperty("report.email.username"), 
				Settings.getProperty("report.email.password"));
		email.send();
	}
	
}
