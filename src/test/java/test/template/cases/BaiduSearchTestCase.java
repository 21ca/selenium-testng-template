package test.template.cases;

import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;
import test.template.utils.SeleniumUtils;

public class BaiduSearchTestCase extends BaseTestCase {
	
	@Test
	public void test() {
		getDriver().get("http://www.baidu.com");
		SeleniumUtils.takeScreenShot(getDriver());

	}
	
}
