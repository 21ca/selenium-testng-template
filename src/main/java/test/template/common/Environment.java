package test.template.common;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.template.utils.EncryptUtils;

enum Environment {
	DEFAULT(""),
	STAGING("stg"),
	PRODUCTION("prod"),
	QA("qa");
	
	private static Logger LOG = LoggerFactory.getLogger(Environment.class);
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
				LOG.error("Load config file error.", e);			
			}
		}
		return props != null ? decrypt(props.getProperty(key)) : null;
	}

	private String decrypt(String text) {
		if (text != null && text.startsWith("enc:")) {
			String enc = text.substring(4);
			try {
				return EncryptUtils.decrypt(enc);
			} catch (Exception e) {
				LOG.error("Decrypt error.", e);			
			}
		}
		return text;
	}
}
