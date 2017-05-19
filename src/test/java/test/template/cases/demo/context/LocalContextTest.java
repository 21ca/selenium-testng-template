package test.template.cases.demo.context;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.template.context.LocalContext;

public class LocalContextTest {
	
	@Test
	public void testPut() {
		LocalContext.put("name", "Test");
	}
	
	@Test(dependsOnMethods = "testPut")
	public void testGet() {
		Assert.assertEquals(LocalContext.get("name"), "Test");
	}
	
	@Test(dependsOnMethods = "testGet")
	public void testRemove() {
		LocalContext.remove("name");
	}
	
	@Test(dependsOnMethods = "testRemove")
	public void testGetAfterRemove() {
		Assert.assertEquals(LocalContext.get("name"), null);
	}
}
