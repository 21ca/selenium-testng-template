package test.template.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MTLogBuffer extends OutputStream {

	private static MTLogBuffer INSTANCE = new MTLogBuffer();
	
	public static MTLogBuffer getInstance(){
		return INSTANCE;
	}
	
	private ThreadLocal<ByteArrayOutputStream> local = new ThreadLocal<ByteArrayOutputStream>() {
		protected ByteArrayOutputStream initialValue() {
			return new ByteArrayOutputStream();
		}
	};
	
	@Override
	public void write(int b) throws IOException {
		local.get().write(b);
	}
	
	public static void clear() {
		INSTANCE.local.get().reset();
	}
	
	public static String getStringLog() {
		return new String(INSTANCE.local.get().toByteArray());
	}
	
}
