package test.template.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import test.template.common.Config;


public final class SeleniumUtils {
	public static String SCREENSHOT_PATH = "./screen_shots/";
	public static String DOWNLOAD_PATH = "./download/";
	private static final Logger log = LoggerFactory.getLogger(SeleniumUtils.class);

	public static final String takeScreenShot() {
		log.info("Capture the screen.");
		try {
			File scrFile = Config.driver().getScreenshotAs(OutputType.FILE);
			String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
			log.info("Save screenshot as: " + fileName);
			FileUtils.copyFile(scrFile, new File(SCREENSHOT_PATH + fileName));
			return fileName;
		} catch (IOException e) {
			log.error("Fail to save the screenshot", e);
			return null;
		}
	}
	
	public static final void scrollToTop() {
		((JavascriptExecutor)Config.driver()).executeScript("scrollTo(0, 0)");
	}
	
	public static final String getWebText(By by) {
		try {
			SeleniumWaitUtils.waitForElement(by);
			return Config.driver().findElement(by).getText();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public static final void sendKeys(By by, String value) {
		SeleniumWaitUtils.waitForElement(by);
		Config.driver().findElement(by).clear();
		Config.driver().findElement(by).sendKeys(value);
	}
	
	public static final void click(By by){
		SeleniumWaitUtils.waitForElement(by);
		WebElement clickElement = Config.driver().findElement(by);
		clickElement.click();
	}
	
	public static final void sleepInSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
		}
	}
	
	public static final boolean isTextPresent(String text) {
		SeleniumWaitUtils.waitForElement(By.xpath("*"));
		System.out.println(text);
		return Config.driver().findElement(By.xpath("*")).getText().contains(text);
	}
	
	public static final boolean switchToWindowByTitle(String windowTitle){  
		String currentHandle = Config.driver().getWindowHandle();
		Set<String> handles = Config.driver().getWindowHandles();
		for (String h : handles) {
			if (!h.equals(currentHandle)) {
				Config.driver().switchTo().window(h);
				if (Config.driver().getTitle().contains(windowTitle)) {
					return true;
				}
			}
		}
		return false;
	}
				
    public static final boolean switchToWindownByPageSource(String windowContent){
    	Set<String> allWindows = Config.driver().getWindowHandles();
    	for (String windowId : allWindows) {
    		Config.driver().switchTo().window(windowId);
    		if (Config.driver().getPageSource().contains(windowContent)){
    			Config.driver().switchTo().window(windowId);
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
  public static final void switchToAnotherWindow() {
        final String parentWindowHandle = Config.driver().getWindowHandle();
        new WebDriverWait(Config.driver(), 5, 100).until(new Function<WebDriver, Boolean>() {
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
    
    public static final WebElement findRadioButton(WebElement parent, int index){
    	SeleniumWaitUtils.waitForElement(By.cssSelector("input[type='radio']"));
        return parent.findElements(By.cssSelector("input[type='radio']")).get(index);
    }
    
   /**
    *  handle blocking clicks, for example JavaScript: window.showModalDialog ()
    * @param driver
    * @param element
    * @param timeoutSeconds
    */
    public static final void clickWithTimeout(WebElement element, int timeoutSeconds) {
        Config.driver().executeScript(
               "var el=arguments[0]; var timeout=arguments[1] * 1000; setTimeout(function() { el.click(); }, timeout);",
               element, timeoutSeconds);
    }
    
    public static final void selectCheckbox(WebElement parent, int index){
      	List<WebElement> checkBox= parent.findElements(By.cssSelector("input[type='checkbox']"));
    	if(!checkBox.get(index).isSelected()){
	    	checkBox.get(index).click();
    	}
   }


	public static final void unselectCheckbox(WebElement parent, int index){
		List<WebElement> checkBox= parent.findElements(By.cssSelector("input[type='checkbox']"));
		if(checkBox.get(index).isSelected()){
			checkBox.get(index).click();
			SeleniumUtils.sleepInSeconds(2);
		}
	}	
	
	public static final void dismissAlert(){
		SeleniumWaitUtils.waitForAlert();
		Alert alert = Config.driver().switchTo().alert();
		log.info("Alert: " + alert.getText());
		alert.dismiss();
		log.info("Dismiss Alter");	
	}
	
	public  static final void acceptAlert(WebDriver driver){
		SeleniumWaitUtils.waitForAlert();
		Alert alert = driver.switchTo().alert();
		log.info("Alert: " + alert.getText());
		alert.accept();
		log.info("Accept Alter");	
	}
	
    public static final void selectRadioButton(WebElement parent,int index){
    	SeleniumWaitUtils.waitForElement(By.cssSelector("input[type='radio']"));
    	List<WebElement> radioButton= parent.findElements(By.cssSelector("input[type='radio']"));
    	if(!radioButton.get(index).isSelected()){
    		radioButton.get(index).click();
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
}
