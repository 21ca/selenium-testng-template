package test.template.cases.demo;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.template.cases.base.NoBrowserBaseTestCase;

public class TestDataTest extends NoBrowserBaseTestCase {
	
	@Test
	public void testData() {
		Assert.assertTrue(data("test.name").equals("name1"));
		Assert.assertTrue(data("test.password").equals("pwd2")); 
	}
	
	@Test(dataProvider = "testdata")
	public void test(String name, String password) {
		//From BaseTestCase.properties
		Assert.assertTrue(name.equals("name1"));
		
		//From TestDataTest.properties, which overwrite the value from BaseTestCase.properties
		Assert.assertTrue(password.equals("pwd2")); 
	}

	@DataProvider(name = "testdata")
	public String[][] data() {
		return new String[][] {
			new String[] { data("test.name"), data("test.password") } 
			};
	}
}
