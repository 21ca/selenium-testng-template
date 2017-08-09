package test.template.cases.demo;
import static org.testng.Assert.fail;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.template.utils.TestngRetry;

public class RetryTest {

	@Test(dataProvider="users", retryAnalyzer = TestngRetry.class)
	public void test(String u) {
		System.out.println("test" + u);
		if (u.equals("b") || u.equals("c")) 
			fail();
	}
	
	@DataProvider(name = "users")
	public String[][] users() throws Exception {
		return new String[][] {
				new String[] {"a"},
				new String[] {"b"},
				new String[] {"c"},
				new String[] {"d"}
		};
	}
	
	@Test(retryAnalyzer = TestngRetry.class)
	public void test1() {
		fail();
	}
	

	@Test(retryAnalyzer = TestngRetry.class)
	public void test3() {
	}
	
	@Test(retryAnalyzer = TestngRetry.class)
	public void test2() {
		fail();
	}
	

	@Test(retryAnalyzer = TestngRetry.class)
	public void test4() {
	}

}
