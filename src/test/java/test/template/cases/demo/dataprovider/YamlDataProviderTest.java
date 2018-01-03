package test.template.cases.demo.dataprovider;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;

public class YamlDataProviderTest extends BaseTestCase {

	@DataProvider(name = "array")
	public Object[][] array() throws Exception {
		return dataProvider("array.yml", String.class, "yaml");
	}
	
	@DataProvider(name = "users")
	public Object[][] users() throws Exception {
		return dataProvider("user.yml", User.class, "yaml");
	}
	
	@DataProvider(name = "map")
	public Object[][] map() throws Exception {
		return dataProvider("map.yml", Map.class, "yaml");
	}


	@Test(dataProvider = "array")
	public void testArray(String value) throws Exception {
		log.info("Test with: " + value);
	}
	
	@Test(dataProvider = "users")
	public void testUsers(User user) throws Exception {
		log.info("Test with: " + user);
	}
	
	@Test(dataProvider = "map")
	public void testMap(Map<String, String> user) throws Exception {
		log.info("Test with: " + user);
	}
}
