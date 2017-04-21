package test.template.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

public class Browser {
	private static ThreadLocal<RemoteWebDriver> drivers = new ThreadLocal<RemoteWebDriver>();

	public static RemoteWebDriver getDriver() {
		return drivers.get();
	}

	public static void setupInternetExplorer() {
		InternetExplorerDriverManager.getInstance().useTaobaoMirror().setup();
		drivers.set(new InternetExplorerDriver());
		drivers.get().manage().window().maximize();
	}

	public static void setupFireFox() {
		FirefoxDriverManager.getInstance().useTaobaoMirror().version("0.14").setup();

		drivers.set(new FirefoxDriver());
		drivers.get().manage().window().maximize();
	}

	public static void setupChrome() {
		ChromeDriverManager.getInstance().useTaobaoMirror().setup();
		drivers.set(new ChromeDriver());
	}

	public static void setupRemoteWebDriver(String url, DesiredCapabilities capabilities) throws MalformedURLException {
		drivers.set(new RemoteWebDriver(new URL(url + "/wd/hub"), capabilities));
	}
	
	public static void close() {
		drivers.get().close();
	}
	
	public static void quit() {
		drivers.get().quit();
	}
}
