package test.template.cases.demo.dataprovider;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.template.cases.base.NoBrowserBaseTestCase;

public class JsonDataProviderTest extends NoBrowserBaseTestCase{
	
	@DataProvider(name = "users")
	public Object[][] users() throws Exception {
		return dataProvider("user.json", User.class, "json");
	}

	@Test(dataProvider = "users")
	public void testUsers(User user) throws Exception {
		log.info("Test with: " + user);
	}
}
