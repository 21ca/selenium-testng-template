package test.template.cases.demo;

import static org.testng.Assert.fail;
import org.testng.annotations.Test;
import test.template.cases.base.BaseWebTestCase;

public class BaiduFailTest extends BaseWebTestCase {

	@Test
	public void goodTest() {

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
		log.info("Test mstest");
		driver().get("https://mstest.active.com");
		log.info("Go to mstest OK");
		fail("I think it fails.");
	}
}
