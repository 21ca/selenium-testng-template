package test.template.cases;

import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;

public class BaiduSearchTestCase extends BaseTestCase {
	
	@Test
	public void test() {
		getDriver().get("http://www.baidu.com");
	}
	
}
