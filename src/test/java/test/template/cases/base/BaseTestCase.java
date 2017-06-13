package test.template.cases.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.paypal.selion.platform.dataprovider.DataProviderFactory;
import com.paypal.selion.platform.dataprovider.DataResource;
import com.paypal.selion.platform.dataprovider.SeLionDataProvider;
import com.paypal.selion.platform.dataprovider.impl.InputStreamResource;
import com.paypal.selion.platform.dataprovider.impl.XmlInputStreamResource;

import test.template.common.Config;

public abstract class BaseTestCase {
	private Properties testDataProps = new Properties();
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@BeforeTest
	@Parameters({ "environment" })
	public void setupEnvironment(@Optional String environment) throws Exception {
		log.info("Setup Environment: " + environment);
		loadTestData();
		Config.setupEnvironment(environment);
	}

	private void loadTestData() throws IOException {
		Class<?> clazz = getClass();
		List<Class<?>> classes = new ArrayList<>();
		do {
			classes.add(clazz);
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class);

		Collections.reverse(classes);
		for (Class<?> c : classes) {
			InputStream is = c.getResourceAsStream(c.getSimpleName() + ".properties");
			if (is != null) {
				testDataProps.load(is);
				is.close();
			}
		}
	}

	/**
	 * Read test data from the property file. The property file name is same
	 * with the test case name. If the property file has no related data, check
	 * the config properties
	 * 
	 * Priority: Test.properties > config_env.properties > config.properties
	 */
	protected String data(String key) {
		String value = testDataProps.getProperty(key);
		if (value == null) {
			value = Config.getProperty(key);
		}
		return value;
	}

	protected String[] dataAsArray(String key) {
		return data(key).split(",|;");
	}

	/**
	 * Load data provider.
	 * 
	 * @param file
	 *            The file could be in the same package with the test cases, or
	 *            in class path, or a file path
	 * @param type
	 *            the file type. it could be "yaml", "excel", "json" or "xml"
	 */
	protected Object[][] dataProvider(String file, Class<?> cls, String type) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(file);
		if (is == null) {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		}
		if (is == null) {
			is = new FileInputStream(file);
		}
		DataResource resource = "xml".equalsIgnoreCase(type) ? new XmlInputStreamResource(is, cls, type)
				: new InputStreamResource(is, cls, type);
		SeLionDataProvider dataProvider = DataProviderFactory.getDataProvider(resource);
		Object[][] data = dataProvider.getAllData();
		log.info("Load data provider: \n" + Arrays.deepToString(data));
		return data;
	}

	@AfterTest
	public void tearDown() {
		System.out.println("Test complete");
	}
}
