package test.template.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

/**
 * Define Global settings
 *
 */
public final class Config {
	private static final String DEFAULT_BROWSER = "chrome";

	private static Logger log = LoggerFactory.getLogger(Config.class);

	private static ThreadLocal<Environment> environments = new ThreadLocal<Environment>();
	private static ThreadLocal<RemoteWebDriver> drivers = new ThreadLocal<RemoteWebDriver>();

	public synchronized static String getProperty(String key) {
		return environments.get().getProperty(key);
	}
	
	public synchronized static int getIntProperty(String key, int defaultValue) {
		String value = getProperty(key);
		return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
	}
	
	public synchronized static int getIntProperty(String key) {
		return getIntProperty(key, 0);
	}
	
	public synchronized static String[] getArrayProperty(String key) {
		return environments.get().getProperty(key).split(",|;");
	}
	
	public synchronized static boolean getBoolean(String key) {
		return "true".equals(getProperty(key));
	}

	public static void setupEnvironment(String env) {
		Environment environment = Environment.of(env);
		if (environment == null){
			environment = Environment.DEFAULT;
			log.error("Invalid Environment, set it to default");
		}
		environments.set(environment);
	}
	
	public static Environment environment() {
		return environments.get();
	}
	
	public static RemoteWebDriver driver() {
		return drivers.get();
	}

	private static void setupRemoteWebDriver(String url, DesiredCapabilities capabilities) throws MalformedURLException {
		drivers.set(new RemoteWebDriver(new URL(url + "/wd/hub"), capabilities));
	}
	
	public static void setupWebDriver(String browser, String url) throws Exception {
		if (StringUtils.isEmpty(browser)) {
			browser = DEFAULT_BROWSER;
		}

		if (url != null) { // Remote browser
			if (browser.equalsIgnoreCase("ie")) {
				setupRemoteWebDriver(url, DesiredCapabilities.internetExplorer());
			} else if (browser.equalsIgnoreCase("firefox")) {
				setupRemoteWebDriver(url, DesiredCapabilities.firefox());
			} else if (browser.equalsIgnoreCase("chrome")) {
				setupRemoteWebDriver(url, DesiredCapabilities.chrome());
			} else {
				log.error("Browser not support: " + browser);
			}
		} else { // Local browser
			RemoteWebDriver driver = null;
			if (browser.equalsIgnoreCase("ie")) {
				InternetExplorerDriverManager.getInstance().useTaobaoMirror().setup();
				driver = new InternetExplorerDriver();
			} else if (browser.equalsIgnoreCase("firefox")) {
				FirefoxDriverManager.getInstance().useTaobaoMirror().version("0.14").setup();
				driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("chrome")) {
				ChromeDriverManager.getInstance().useTaobaoMirror().setup();
				driver = new ChromeDriver();
			} else {
				throw new IllegalArgumentException("Browser not support: " + browser);
			}
			drivers.set(driver);
			drivers.get().manage().window().maximize();
		}
	
	}
}
