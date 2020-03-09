package test.template.cases.demo;

import static org.testng.Assert.fail;
import org.testng.annotations.Test;
import test.template.cases.base.BaseWebTestCase;

public class BaiduFailTest extends BaseWebTestCase {

	@Test
	public void goodTest() {
		log.info("Test baidu good");
	}

	@Test
	public void failTest1() {
		log.info("Test baidu");
		driver().get("http://www.baidu.com");
		log.info("Go to baidu OK");
		System.out.println(1 / 0);
	}

	@Test
	public void failTest2() {
		log.info("Test baidu");
		driver().get("https://www.baidu.com");
		log.info("Go to baidu OK");
		fail("I think it fails.");
	}
}
