package test.template.cases.demo;

import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;
import test.template.utils.SeleniumUtils;

public class BaiduSearchTestCase extends BaseTestCase {
	
	@Test()
	public void test() {
		driver().get("http://127.0.0.1/");
		SeleniumUtils.takeScreenShot();
	}

}
