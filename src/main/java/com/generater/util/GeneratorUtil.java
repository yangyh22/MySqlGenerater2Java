package com.generater.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @Description 生成工具类
 * @author yangyh
 * @date 2018年4月8日
 * @version V1.0.0
 */
public class GeneratorUtil {

	private final static String TEMPLATES_PATH = "D:\\07git\\MySqlGenerater2Java\\src\\main\\resources\\templates";

	public static void main(String[] args) throws IOException, TemplateException, ClassNotFoundException, SQLException {

		// 生成实体类
		TableInfo tableInfo = ConnectionUtil.getTableInfo("test");

		// tableInfo.setCurrentClass(Updatable.class);
		// tableInfo = ConnectionUtil.checkAndRemove(tableInfo);

		// 生成实体类
		generateEntity(tableInfo);

		// 生成sqlmapper
		generateSqlMapper(tableInfo);

	}

	/**
	 * @Description 生成实体类
	 * @author yangyh
	 * @date 2018年4月2日
	 * @version V1.0.0
	 */
	public static void generateEntity(TableInfo tableInfo) throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setDirectoryForTemplateLoading(new File(TEMPLATES_PATH));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Template temp = cfg.getTemplate("entity.ftl");
		Map<String, Object> root = new HashMap<String, Object>();

		root.put("packageName", "com.generater.entity");
		root.put("className", "Test");
		root.put("author", "yangyh");

		root.put("attrs", tableInfo);

		File dir = new File("D:\\07git\\MySqlGenerater2Java\\src\\main\\java\\com\\generater\\entity");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream fos = new FileOutputStream(new File(dir, "Test.java")); // java文件的生成目录
		Writer out = new OutputStreamWriter(fos);
		temp.process(root, out);

		fos.flush();
		fos.close();
	}

	/**
	 * @Description 生成sqlmapper
	 * @author yangyh
	 * @date 2018年4月2日
	 * @version V1.0.0
	 */
	public static void generateSqlMapper(TableInfo tableInfo) throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setDirectoryForTemplateLoading(new File(TEMPLATES_PATH));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Template temp = cfg.getTemplate("mapper.ftl");
		Map<String, Object> root = new HashMap<String, Object>();

		root.put("packageName", "com.generater.entity");
		root.put("className", "Test");

		root.put("attrs", tableInfo);

		File dir = new File("D:\\07git\\MySqlGenerater2Java\\src\\main\\resources\\sqlmapper");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream fos = new FileOutputStream(new File(dir, "test-sqlmapper.xml")); // java文件的生成目录
		Writer out = new OutputStreamWriter(fos);
		temp.process(root, out);

		fos.flush();
		fos.close();
	}

}
