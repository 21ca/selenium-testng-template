package test.template.cases.demo.dataprovider;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;
import test.template.utils.TestngRetry;

public class JsonDataProviderTest extends BaseTestCase{
	
	@DataProvider(name = "users")
	public Object[][] users() throws Exception {
		return dataProvider("user.json", User.class, "json");
	}

	@Test(dataProvider = "users", retryAnalyzer = TestngRetry.class)
	public void testUsers(User user) throws Exception {
		Assert.assertTrue(user.getPassword().equals("Hello"));
		log.info("Test with: " + user);
	}
}
