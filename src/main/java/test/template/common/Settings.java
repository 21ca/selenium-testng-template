package test.template.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Define const here
 *
 */
public final class Settings {
	private static Logger log = LoggerFactory.getLogger(Settings.class);

	private static ThreadLocal<Environment> environments = new ThreadLocal<Environment>();
	
	public synchronized static String getProperty(String key) {
		return environments.get().getProperty(key);
	}
	
	public synchronized static int getIntProperty(String key) {
		return Integer.parseInt(environments.get().getProperty(key));
	}
	
	public synchronized static String[] getArrayProperty(String key) {
		return environments.get().getProperty(key).split(",|;");
	}
	
	public synchronized static boolean getBoolean(String key) {
		if (environments.get() == null) {
			log.error("Environment not set, use default environment");
		}
		return "true".equals(environments.get().getProperty(key));
	}

	public static void setupEnvironment(String env) {
		Environment environment = Environment.of(env);
		if (environment == null){
			environment = Environment.DEFAULT;
			log.error("Invalid Environment, set it to default");
		}
		environments.set(environment);
	}
	
	public static Environment getEnvironment() {
		return environments.get();
	}
}
