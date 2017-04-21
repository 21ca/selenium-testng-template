package test.template.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;


public final class SeleniumUtils {
	public static String SCREENSHOT_PATH = "./screen_shots/";
	public static String DOWNLOAD_PATH = "./download/";
	private static final Logger log = LoggerFactory.getLogger(SeleniumUtils.class);

	public static final String takeScreenShot(RemoteWebDriver driver) {
		log.info("Capture the screen.");
		try {
			File scrFile = driver.getScreenshotAs(OutputType.FILE);
			String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
			log.info("Save screenshot as: " + fileName);
			FileUtils.copyFile(scrFile, new File(SCREENSHOT_PATH + fileName));
			return fileName;
		} catch (IOException e) {
			log.error("Fail to save the screenshot", e);
			return null;
		}
	}
	public static final void scrollToElement(WebDriver driver, WebElement element) {
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);
		((JavascriptExecutor)driver).executeScript("scrollBy(0, -window.screen.availHeight/2)");
	}
	
	public static final void scrollToTop(WebDriver driver) {
		((JavascriptExecutor)driver).executeScript("scrollTo(0, 0)");
	}
	
	public static final void waitForPageLoadComplete(WebDriver driver) {
		Predicate<WebDriver> loadComplete = new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver driver) {
				return ((RemoteWebDriver)driver).executeScript("return document.readyState").equals("complete");
			}
		};
		new WebDriverWait(driver, 60).until(loadComplete);
	}

	public static final WebElement waitForElement(WebDriver driver, final By by, int timeoutInSeconds) {
		return waitForCondition(driver, timeoutInSeconds, ExpectedConditions.presenceOfElementLocated(by));
	}

	public static final WebElement waitForElementClickable(WebDriver driver, final By by, int timeoutInSeconds) {
		return waitForCondition(driver, timeoutInSeconds,ExpectedConditions.elementToBeClickable(by));
	}

	public static final Alert waitForAlert(WebDriver driver,int timeoutInSeconds) {
		return waitForCondition(driver, timeoutInSeconds, ExpectedConditions.alertIsPresent());
	}
	
	public static final <T> T waitForCondition(WebDriver driver,
			int timeoutInSeconds, ExpectedCondition<T> condition) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		return wait.until(condition);
	}
	
	public static final boolean isWebElementExist(WebDriver driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
				
	public static final boolean isAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	public static final String getWebText(WebDriver driver, By by) {
		try {
			SeleniumUtils.waitForElement(driver, by, 20);
			return driver.findElement(by).getText();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public static final void sendKeys(WebDriver driver, By by, String value) {
		SeleniumUtils.waitForElement(driver, by, 20);
		driver.findElement(by).clear();
		driver.findElement(by).sendKeys(value);
	}
	
	public static final void click(WebDriver driver, By by){
		
		WebElement clickElement = driver.findElement(by);
		clickElement.click();
	}
	
	public static final void sleepInSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
		}
	}
	
	public static final boolean isTextPresent(WebDriver driver, String text) {
		SeleniumUtils.waitForElement(driver, By.xpath("*"), 20);
		System.out.println(text);
		return driver.findElement(By.xpath("*")).getText().contains(text);
	
		// return isWebElementExist(driver, By.xpath("//*[contains(text(),'" + text + "')]"));
		// return driver.getPageSource().contains(text);
	}
	
	public static final boolean switchToWindowByTitle(WebDriver driver, String windowTitle){  
		String currentHandle = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String h : handles) {
			if (!h.equals(currentHandle)) {
				driver.switchTo().window(h);
				if (driver.getTitle().contains(windowTitle)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static final boolean trySwitchToWindowByTitle(WebDriver driver, final String windowTitle, int timeOutInSeconds) {
		try {
			new WebDriverWait(driver, timeOutInSeconds).until(new Predicate<WebDriver>() {
				@Override
				public boolean apply(WebDriver driver) {
					return switchToWindowByTitle(driver, windowTitle);
				}
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	} 
			
    public static final boolean  switchToWindownByPageSource(WebDriver driver,String windowContent){
    	Set<String> allWindows = driver.getWindowHandles();
    	for (String windowId : allWindows) {
   		driver.switchTo().window(windowId);
    		if (driver.getPageSource().contains(windowContent)){
    			driver.switchTo().window(windowId);
    			System.out.println("Switch to window: " + windowId + " successfully!"); 
    			return true;
    		}    		   			
    	}
		return false;
    }
      
         
  /**
   *  When two windows are opened, switch to another window   
   * @param driver
   */
  public static final void switchToAnotherWindow(WebDriver driver) {
        final String parentWindowHandle = driver.getWindowHandle();
        new WebDriverWait(driver, 5, 100).until(new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                String[] windowHandles =
                    driver.getWindowHandles().toArray(new String[0]);
                if (windowHandles.length != 2) {
                    return false;
                }
                if (windowHandles[0].equals(parentWindowHandle)) {
                    driver.switchTo().window(windowHandles[1]);
                } else {
                    driver.switchTo().window(windowHandles[0]);
                }
                return true;
            }
        });
    }
    
    public static final WebElement findRadioButton(WebDriver driver,WebElement parent, int index){
    	SeleniumUtils.waitForElement(driver, By.cssSelector("input[type='radio']"), 20);
        return parent.findElements(By.cssSelector("input[type='radio']")).get(index);
    }
    
   /**
    *  handle blocking clicks, for example JavaScript: window.showModalDialog ()
    * @param driver
    * @param element
    * @param timeoutSeconds
    */
    public static final void clickWithTimeout(RemoteWebDriver driver, WebElement element, int timeoutSeconds) {
        driver.executeScript(
               "var el=arguments[0]; var timeout=arguments[1] * 1000; setTimeout(function() { el.click(); }, timeout);",
               element, timeoutSeconds);
    }
    
    public static final void selectCheckbox(WebElement parent,int index){
  
    	List<WebElement> checkBox= parent.findElements(By.cssSelector("input[type='checkbox']"));
    	if(!checkBox.get(index).isSelected()){
    	checkBox.get(index).click();
    	SeleniumUtils.sleepInSeconds(4);
    	//List<WebElement> box= parent.findElements(By.cssSelector("input[type='checkbox']"));
    	/**log.info("Select the CheckBox - " + checkBox.get(index).getAttribute("id") + ":" 
    	+ checkBox.get(index).isSelected()); **/
    	}
   }


	public static final void unselectCheckbox(WebElement parent,int index){
		List<WebElement> checkBox= parent.findElements(By.cssSelector("input[type='checkbox']"));
		if(checkBox.get(index).isSelected()){
			checkBox.get(index).click();
			SeleniumUtils.sleepInSeconds(2);
			/**List<WebElement> box= parent.findElements(By.cssSelector("input[type='checkbox']"));
			log.info("Select the CheckBox - " + box.get(index).getAttribute("id") + ":" 
					+ box.get(index).isSelected());		**/
		}
	}	
	
	public static final void dismissAlert(WebDriver driver){
		
		SeleniumUtils.waitForAlert(driver, 20);

		Alert alert = driver.switchTo().alert();
		log.info(alert.getText());
		alert.dismiss();
		SeleniumUtils.sleepInSeconds(10);
		log.info("Dismiss Alter.");	
	}
	
	public  static final void acceptAlert(WebDriver driver){
		
		SeleniumUtils.waitForAlert(driver, 20);

		Alert alert = driver.switchTo().alert();
		log.info(alert.getText());
		alert.accept();
		SeleniumUtils.sleepInSeconds(10);
		log.info("Accept Alter.");	
	}
	
    public static final void selectRadioButton(WebDriver driver,WebElement parent,int index){
    	SeleniumUtils.waitForElement(driver, By.cssSelector("input[type='radio']"), 20);
    	List<WebElement> radioButton= parent.findElements(By.cssSelector("input[type='radio']"));
    	if(!radioButton.get(index).isSelected()){
    		radioButton.get(index).click();
    		SeleniumUtils.sleepInSeconds(5);
    	}
	}
    
    /**
     * Example: getTableTextsByRowIndex(table, 2) returns {11, 22, 33}
     * 1	2	3
     * 11	22	33
     * 111	222	333
     * 
     * @param table
     * @param rowIndex Start from 1.
     * @return
     */
	public static final List<String> getTableTextsByRowIndex(WebElement table, int rowIndex) {
    	if (!table.getTagName().equalsIgnoreCase("table")) {
    		throw new IllegalArgumentException("Table web element is required");
    	}
    	
    	List<WebElement> elements = table.findElements(By.cssSelector("tr:nth-child(" + rowIndex + ") td"));
    	log.info(elements.size()+":");
    	List<String> result = new ArrayList<String>(elements.size());
    	for (WebElement e : elements) {
    		result.add(StringUtils.trim(e.getText()));
    	}
    	System.out.println(Arrays.asList(result));
    	return result;
    }
	
	/**
     * Example: getTableTextsByColumnIndex(table, 2) returns {2, 22, 22}
     * 1	2	3
     * 11	22	33
     * 111	222	333
     * 
     * @param tablert5
     * @return
     */
	public static final List<String> getTableTextsByColumnIndex(WebElement table, int columnIndex) {
    	if (!table.getTagName().equalsIgnoreCase("table")) {
    		throw new IllegalArgumentException("Table web element is required");
    	}
    	
    	List<WebElement> elements = table.findElements(By.cssSelector("tr td:nth-child(" + columnIndex + ")"));
    	List<String> result = new ArrayList<String>(elements.size());
    	for (WebElement e : elements) {
    		result.add(StringUtils.trim(e.getText()));
    	}
    	return result;
    	//Assert.assertEquals(SeleniumUtils.getTableTextsByColumnIndex(table , 2), Arrays.asList("2", "22", "222"));
    }
	
	/**
	 * Example: getRowTextsByColumnText(table, 2, '22') returns row {11, 22, 33}
     * 1	2	3
     * 11	22	33
     * 111	222	333
	 * 
	 * @param table
	 * @param columnIndex Start from 1
	 * @param searchText
	 * @return
	 */
	public static final List<String> getTableRowByColumnText(WebElement table, int columnIndex, String searchText) {
		if (!table.getTagName().equalsIgnoreCase("table")) {
    		throw new IllegalArgumentException("Table web element is required");
    	}

		WebElement rowElement = table.findElement(By.xpath("//tr//td[position()=" + columnIndex + " and contains(*|text(), '" + searchText + "')]/.."));
    	List<WebElement> elements = rowElement.findElements(By.tagName("td"));
    	List<String> result = new ArrayList<String>(elements.size());
    	for (WebElement e : elements) {
    		result.add(StringUtils.trim(e.getText()));
    	}
    	return result;
	}
	
	public static final WebElement findElementByPartialText(SearchContext context, String tag, String... partialTexts) {
		String xpath = "//" + tag + "[1=1";
		for (String text : partialTexts) {
			xpath += " and contains(text(), '" + text + "')";
		}
		xpath += "]";
		return context.findElement(By.xpath(xpath));
	}
		
	public static final WebElement getParent(WebElement element) {
		return element.findElement(By.xpath(".."));
	}
	
	public static final WebElement getAncestorByTagName(WebElement element, String tagName) {
		element = getParent(element);
		while (!element.getTagName().equalsIgnoreCase(tagName)){
			if (element.getTagName().equalsIgnoreCase("body") || element.getTagName().equalsIgnoreCase("html")) {
				return null;
			}
			element = getParent(element);
		}
		return element;
	}
	
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
			WebElement form = getAncestorByTagName(button, "form");
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
