package test.template.cases.demo.dataprovider;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;

public class ExcelDataProviderTest extends BaseTestCase{
	
	@DataProvider(name = "users")
	public Object[][] users() throws Exception {
		return dataProvider("user.xls", User.class, "xls");
	}

	@Test(dataProvider = "users")
	public void testUsers(User user) throws Exception {
		log.info("Test with: " + user);
	}
}
