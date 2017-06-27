package test.template.utils;

import java.io.InputStream;

public final class ClassPathResource {
	public static InputStream getInputStream(String path) {
		InputStream is = ClassPathResource.class.getResourceAsStream(path);
		if (is == null) {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		}
		if (is == null) {
			is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
		}
		return is;
	}
}
