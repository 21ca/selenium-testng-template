package test.template.cases.demo;
import java.io.File;
import java.io.IOException;
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
		String ymal = FileUtils.toYmal(user);
		System.out.println("Ymal String: " + ymal);

		User user2 = FileUtils.fromYmal(ymal, User.class);
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
	
	@Test
	public void testYmal2() throws IOException {
		File file = new File("src/test/java/test/template/cases/demo/dataprovider/user.yml");;
		List<test.template.cases.demo.dataprovider.User> users = 
				FileUtils.readFromYmal(file, List.class);
		System.out.println(users);
	}

}