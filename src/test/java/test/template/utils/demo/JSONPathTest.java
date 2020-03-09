package test.template.utils.demo;

import com.jayway.jsonpath.JsonPath;

public class JSONPathTest {

	public static void main(String[] args) {
		String json = "{'name': 'test'}";
		String name = JsonPath.read(json, "@.name");
		System.out.println(name);
		
	}

}
