package test.template.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DownloadUtils {
	public static String DOWNLOAD_PATH = "./download/";

	public static final String download(RemoteWebDriver driver, String url) {
		String folder = DOWNLOAD_PATH + RandomStringUtils.randomAlphabetic(10);
		new File(folder).mkdirs();

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie", getCookie(driver));
		byte[] data = HttpUtils.get(url, headers);
		try {
			String filename;
			String contentDisposition = headers.get("Content-Disposition");
			if (StringUtils.contains(contentDisposition, "=")) {
				filename = contentDisposition.substring(contentDisposition.indexOf("=") + 1);
			} else {
				filename = new URL(url).getPath();
				if (filename.contains("/")) {
					filename = filename.substring(filename.lastIndexOf("/") + 1);
				}
			}
			IOUtils.write(data, new FileOutputStream(folder + "/" + filename));
			return folder + "/" + filename;
		} catch (Exception e) {
			throw new RuntimeException("Download failed!", e);
		}
	}
	
	public static final String download(RemoteWebDriver driver, WebElement button) {
		String folder = DOWNLOAD_PATH + RandomStringUtils.randomAlphabetic(10);
		new File(folder).mkdirs();
		if (button.getTagName().equalsIgnoreCase("a")) {
			String url = button.getAttribute("href");
			return download(driver, url);
		} else {
			WebElement form = SeleniumUtils.getAncestorByTagName(button, "form");
			if (form != null) {
				String url = form.getAttribute("action");
				if (StringUtils.isEmpty(url)) {
					url = driver.getCurrentUrl();
				}
				List<WebElement> inputElements = form.findElements(By.tagName("input"));
				StringBuilder sb = new StringBuilder();
				for (WebElement input : inputElements) {
					sb.append(input.getAttribute("name") + "=" + urlEncode(input.getAttribute("value")) + "&");
				}
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Cookie", getCookie(driver));
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				byte[] data = HttpUtils.post(url, sb.toString(), headers);
				String contentDisposition = headers.get("Content-Disposition");
				String filename = "untitled";
				if (StringUtils.contains(contentDisposition, "=")) {
					filename = contentDisposition.substring(contentDisposition.indexOf("=") + 1);
				}
				
				try {
					IOUtils.write(data, new FileOutputStream(folder + "/" + filename));
					return folder + "/" + filename;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		throw new RuntimeException("Download failed!");
	}
	
	private static String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
	
	private static String getCookie(RemoteWebDriver driver) {
		StringBuilder sb = new StringBuilder();
		for (Cookie cookie : driver.manage().getCookies()) {
			sb.append(cookie.getName() + "=" + cookie.getValue() + "; ");
		}
		return sb.toString();
	}
}
