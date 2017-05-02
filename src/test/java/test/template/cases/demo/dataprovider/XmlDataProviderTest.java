package test.template.cases.demo.dataprovider;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.template.cases.base.NoBrowserBaseTestCase;

public class XmlDataProviderTest extends NoBrowserBaseTestCase{
	
	@DataProvider(name = "users")
	public Object[][] users() throws Exception {
		return dataProvider("user.xml", User.class, "xml");
	}

	@Test(dataProvider = "users")
	public void testUsers(User user) throws Exception {
		log.info("Test with: " + user);
	}
}
