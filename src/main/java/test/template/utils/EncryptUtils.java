package test.template.utils;

import org.jasypt.util.text.BasicTextEncryptor;

import test.template.common.Config;

public class EncryptUtils {

	private static final String PASSWORD = "DkCRQ@CWyO#PAN";
	private static final BasicTextEncryptor ENCRYPTOR = new BasicTextEncryptor();
	
	static {
		ENCRYPTOR.setPassword(PASSWORD);
	}
	
	public static String decrypt(String encryptedText) {
		return ENCRYPTOR.decrypt(encryptedText);
	}
	
	public static String encrypt(String text) {
		return ENCRYPTOR.encrypt(text);
	}
	
	public static void main(String[] args) {
		String encryptedText = encrypt("12345678");
		System.out.println("Encrypted result: " + encryptedText);
		System.out.println("Decrypt result: " + decrypt(encryptedText));
		
		Config.setupEnvironment("qa");
		System.out.println(Config.getProperty("test.password"));
	}

}
