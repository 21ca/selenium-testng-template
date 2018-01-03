package test.template.utils.demo;

import java.io.FileInputStream;
import java.io.InputStream;

import test.template.utils.ClassPathResource;
import test.template.utils.SftpUtils;

public class SftpUtilsTest {

	public static void main(String[] args) throws Exception {
		InputStream is = new FileInputStream("pom.xml");
		SftpUtils.put("andy", "andy", "192.168.202.136", is, "/home/andy/pom.xml");
		
		InputStream is2 = new ClassPathResource("config.properties").getInputStream();
		SftpUtils.put("andy", "andy", "192.168.202.136", is2, "/home/andy/config.properties");
		
		byte[] data = SftpUtils.getBytes("andy", "andy", "192.168.202.136", "/home/andy/config.properties");
		System.out.println(new String(data));
	}

}
