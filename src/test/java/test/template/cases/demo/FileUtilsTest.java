package test.template.cases.demo;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.template.utils.FileUtils;

public class FileUtilsTest {
	public static class User {
		private String name;
		private String password;
		
		public User(){}
		
		public User(String name, String password) {
			this.name = name;
			this.password = password;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	@Test
	public void testJson() {
		User user = new User("abc", "pwd");
		String json = FileUtils.toJson(user);
		System.out.println("JSON String: " + json);
		
		User user2 = FileUtils.fromJson(json, User.class);
		Assert.assertEquals(user.name, user2.name);
		Assert.assertEquals(user.password, user2.password);
	}
	
	@Test
	public void testYmal() {
		User user = new User("abc", "pwd");
		String ymal = FileUtils.toYaml(user);
		System.out.println("Ymal String: " + ymal);

		User user2 = FileUtils.fromYaml(ymal, User.class);
		Assert.assertEquals(user.name, user2.name);
		Assert.assertEquals(user.password, user2.password);
	}
	
	@Test
	public void testXml() {
		User user = new User("abc", "pwd");
		String xml = FileUtils.toXml(user);
		System.out.println("XML String: " + xml);
		
		User user2 = FileUtils.fromXml(xml, User.class);
		Assert.assertEquals(user.name, user2.name);
		Assert.assertEquals(user.password, user2.password);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadYmal() throws Exception {
		File file = new File(FileUtilsTest.class.getResource("user.yml").toURI());;
		List<User> users = FileUtils.readFromYaml(file, List.class);
		Assert.assertEquals(users.size(), 2);
		Assert.assertEquals(users.get(0).name, "name1");
		Assert.assertEquals(users.get(0).password, "pwd1");
		Assert.assertEquals(users.get(1).name, "name2");
		Assert.assertEquals(users.get(1).password, "pwd2");
	}

	@Test
	public void testReadExcel() throws Exception {
		List<String[]> data = FileUtils.readExcel(
				new File(FileUtilsTest.class.getResource("test.xls").toURI()), 0);
		System.out.println("Read xls");
		for (String[] d : data) {
			System.out.println(Arrays.toString(d));
		}
		
		data = FileUtils.readExcel(new File(FileUtilsTest.class.getResource("test.xlsx").toURI()), 0);
		System.out.println("Read xlsx");
		for (String[] d : data) {
			System.out.println(Arrays.toString(d));
		}
	}
	
	@Test
	public void testReadExcelAsObject() throws Exception {
		List<User> users = FileUtils.readExcel(new File(FileUtilsTest.class.getResource("test.xls").toURI()), 0, User.class);
		Assert.assertEquals(users.size(), 2);
		Assert.assertEquals(users.get(0).name, "name1");
		Assert.assertEquals(users.get(0).password, "pwd1");
		Assert.assertEquals(users.get(1).name, "name2");
		Assert.assertEquals(users.get(1).password, "pwd2");

	}
}
