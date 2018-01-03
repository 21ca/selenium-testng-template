package test.template.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Used as a global context/storage
 */
public final class GlobalContext {
	private static final Map<String, Object> CONTEXT = new HashMap<String, Object>();

	public static void put(String key, Object value) {
		CONTEXT.put(key, value);
	}

	public static Object get(String key) {
		return CONTEXT.get(key);
	}
	
	public static Object remove(String key) {
		return CONTEXT.remove(key);
	}
}
