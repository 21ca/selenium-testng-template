package test.template.cases;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;

public class TestDataTest extends BaseTestCase {

	@Test(dataProvider = "ase")
	public void test(String s) {
		System.out.println("Test with " + s);
	}
	
	@DataProvider(name = "ase")
	public String[][] data() {
		return new String[][] {
			new String[] { data("mykey") },
			new String[] { data("mykey0") }};
	}
}
