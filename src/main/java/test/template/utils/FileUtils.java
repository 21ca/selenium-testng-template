package test.template.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXB;

import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileUtils {
	private static final Charset UTF8 = Charset.forName("UTF-8");

	public static String toJson(Object o) {
		Gson json = new GsonBuilder().setPrettyPrinting().create();
		return json.toJson(o);
	}

	public static <T> T fromJson(String s, Class<T> clazz) {
		Gson json = new GsonBuilder().setPrettyPrinting().create();
		return json.fromJson(s, clazz);
	}

	public static void saveAsJson(Object o, File file) throws IOException {
		writeToFile(file, toJson(o));
	}

	public static <T> T readFromJson(File file, Class<T> clazz) throws IOException {
		return fromJson(readFile(file), clazz);
	}

	public static String toXml(Object o) {
		StringWriter sw = new StringWriter();
		JAXB.marshal(o, sw);
		return sw.toString();
	}

	public static <T> T fromXml(String s, Class<T> clazz) {
		return JAXB.unmarshal(new StringReader(s), clazz);
	}

	public static void saveAsXml(Object o, File file) throws IOException {
		writeToFile(file, toXml(o));
	}

	public static <T> T readFromXml(File file, Class<T> clazz) throws IOException {
		return fromXml(readFile(file), clazz);
	}
	
	public static String toYmal(Object o) {
		Yaml ymal = new Yaml();
		return ymal.dump(o);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromYmal(String s, Class<T> clazz) {
		Yaml ymal = new Yaml();
		return (T) ymal.load(new StringReader(s));
	}

	public static void saveAsYmal(Object o, File file) throws IOException {
		writeToFile(file, toYmal(o));
	}

	public static <T> T readFromYmal(File file, Class<T> clazz) throws IOException {
		return fromYmal(readFile(file), clazz);
	}

	public static String readFile(File file) throws IOException {
		return org.apache.commons.io.FileUtils.readFileToString(file, UTF8);
	}

	public static void writeToFile(File file, String data) throws IOException {
		org.apache.commons.io.FileUtils.writeStringToFile(file, data, UTF8);
	}
}
