package test.template.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileUtils {
	
	static {
		System.setProperty("java.io.tmpdir", "logs");
	}
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

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

	public static <T> T readFromJson(InputStream is, Class<T> clazz) {
		return fromJson(readInputStream(is), clazz);
	}
	
	public static <T> T readFromJson(File file, Class<T> clazz) {
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

	public static <T> T readFromXml(InputStream is, Class<T> clazz) {
		return fromXml(readInputStream(is), clazz);
	}
	
	public static <T> T readFromXml(File file, Class<T> clazz) {
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

	public static <T> T readFromYmal(InputStream is, Class<T> clazz) {
		return fromYmal(readInputStream(is), clazz);
	}
	
	public static <T> T readFromYmal(File file, Class<T> clazz) {
		return fromYmal(readFile(file), clazz);
	}

	/**
	 * Read Excel file to List
	 * 
	 * Each row is a String array.
	 */
	public static List<String[]> readExcel(File file, int sheetIndex) throws Exception {
		Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		List<String[]> data = new ArrayList<>();
		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row == null) continue;
			int last = row.getLastCellNum();
			String[] rowData = new String[last];
			for (int j = 0; j < last; j++) {
				Cell cell = row.getCell(j);
				rowData[j] = cell == null ? null : cell.toString();
			}
			data.add(rowData);
		}
		return data;
	}
	
	/**
	 * Read Excel file to Object
	 * 
	 * The first row is used to defile fields.
	 * 
	 * Excel file Example:
	 *  id    name    password
	 *   1    test        pwd1
	 *   2    jack        pwd2
	 */
	public static <T> List<T> readExcel(File file, int sheetIndex, Class<T> clazz) throws Exception {
		List<T> result = new ArrayList<T>();
		List<String[]> data = readExcel(file, sheetIndex);
		if (data.size() < 2) { // At least 2 rows.
			return result;
		}
		
		Iterator<String[]> iter = data.iterator();
		String[] firstRow = iter.next(); // First row, the column names, are mapped to the object fields
		
		Field[] fields = new Field[firstRow.length];
		for (int i = 0; i < firstRow.length; i++) {
			try {
				Field field = clazz.getDeclaredField(firstRow[i]);
				field.setAccessible(true);
				fields[i] = field;
			} catch (NoSuchFieldException e) {
				LOG.info("NoSuchFieldException: {}", e.getMessage());
			}
		}
			
		while (iter.hasNext()) {
			T obj = clazz.newInstance();
			String[] row = iter.next();
			for (int i = 0; i < row.length; i++) {
				if (row[i] != null && fields[i] != null) {
					if (fields[i].getType() == int.class || fields[i].getType() == Integer.class) {
						fields[i].set(obj, Integer.parseInt(row[i]));
					} else if (fields[i].getType() == String.class) {
						fields[i].set(obj, row[i]);
					}
				}
			}
			result.add(obj);
		}
		return result;
	}

	public static String readFile(File file) {
		try {
			return org.apache.commons.io.FileUtils.readFileToString(file, UTF8);
		} catch (Exception e) {
			LOG.error("Failed to read file", e);
			return null;
		}
	}
	
	public static String readInputStream(InputStream is) {
		try {
			return IOUtils.toString(is, UTF8);
		} catch (Exception e) {
			LOG.error("Failed to read input stream", e);
			return null;
		}
	}

	public static void writeToFile(File file, String data) throws IOException {
		org.apache.commons.io.FileUtils.writeStringToFile(file, data, UTF8);
	}
	
	public static String readPdf(String file) throws Exception {
		return readPdf(new FileInputStream(file));
	}
	
	public static String readPdf(File file) throws Exception {
		return readPdf(new FileInputStream(file));
	}

	public static String readPdf(InputStream is) throws Exception {
		PDDocument document = PDDocument.load(is);
		PDFTextStripper textStripper = new PDFTextStripper(); 
		return textStripper.getText(document);
	}
}
