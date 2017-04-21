package test.template.cases;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;
import test.template.common.Settings;

public class BaiduSearchTestCase extends BaseTestCase {
	
	@BeforeTest
	public void setup() {
		Settings.setupEnvironment("qa");

	}
	@Test
	public void test() {
		getDriver().get("http://www.baidu.com");
	}
	
}
