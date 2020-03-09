package test.template.cases.demo;

import org.testng.annotations.Test;

import test.template.cases.base.BaseWebTestCase;
import test.template.utils.SeleniumUtils;

public class BaiduSearchTestCase extends BaseWebTestCase {
	
	@Test
	public void test() {
		System.out.println(driver());
		driver().get("http://www.baidu.com/");
		SeleniumUtils.takeScreenShot();
	}

}
