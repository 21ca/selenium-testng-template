package test.template.context;

import java.util.HashMap;
import java.util.Map;

/**
 * A local Context/Storage based on ThreadLocal
 */
public final class LocalContext {
	private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<Map<String, Object>>() {
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};

	public static void put(String key, Object value) {
		THREAD_LOCAL.get().put(key, value);
	}
	
	public static Object get(String key) {
		return THREAD_LOCAL.get().get(key);
	}
	
	public static Object remove(String key) {
		return THREAD_LOCAL.get().remove(key);
	}
}
