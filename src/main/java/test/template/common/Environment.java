package test.template.common;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

enum Environment {
	DEFAULT(""),
	STAGING("stg"),
	PRODUCTION("prod"),
	QA("qa");
	
	private String env;
	private Properties props;
	
	private Environment(String env) {
		this.env = env;
	}
	
	public static Environment of(String env) {
		if (env == null)  {
			return DEFAULT;
		}
		
		for (Environment e : values()) {
			if (e.env.equals(env)) {
				return e;
			}
		}
		return null;
	}
		
	public synchronized String getProperty(String key) {
		if (props == null) {
			props = new Properties();

			try {
				props.load(Environment.class.getResourceAsStream("/config.properties"));
			} catch (IOException e) { 
				e.printStackTrace();
			}
			try {
				if (StringUtils.isNotEmpty(env)) {
					props.load(Environment.class.getResourceAsStream("/config_" + env + ".properties"));
				}
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
		return props != null ? props.getProperty(key) : null;
	}
}
