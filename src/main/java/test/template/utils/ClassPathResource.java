package test.template.utils;

import java.io.InputStream;
import java.net.URL;

public final class ClassPathResource {
	private String path;

	public ClassPathResource(String path) {
		this.path = path;
	}
	
	public InputStream getInputStream() {
		InputStream is = ClassPathResource.class.getResourceAsStream(path);
		if (is == null) {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		}
		if (is == null) {
			is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
		}
		return is;
	}
	
	public URL getURL() {
		URL url = ClassPathResource.class.getResource(path);
		if (url == null) {
			url = Thread.currentThread().getContextClassLoader().getResource(path);
		}
		if (url == null) {
			url = ClassLoader.getSystemClassLoader().getResource(path);
		}
		return url;
	}
}
