package com.generater.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;

import com.generater.vo.GenerateVO;

/**
 * @author yangyh
 * @version V1.0.0
 * @Description 生成工具类
 * @date 2018年4月8日
 */
public class GeneratorUtil {

	private final static String TEMPLATES_PATH = "D:\\07git\\MySqlGenerater2Java\\src\\main\\resources\\templates";

	public static void main(String[] args) throws IOException, TemplateException, ClassNotFoundException, SQLException {

		// 生成实体类
		TableInfo tableInfo = ConnectionUtil.getTableInfo("test", "test");

		// tableInfo.setExtentClass(Updatable.class);
		// tableInfo = ConnectionUtil.checkAndRemove(tableInfo);
		GenerateVO generateVO = new GenerateVO();
		// 生成实体类
		generateEntity(tableInfo, generateVO);

		// 生成sqlmapper
		generateSqlMapper(tableInfo,generateVO);

		// 生成dao,没有实现baseDao
		generateDao(tableInfo,generateVO);

	}

	/**
	 * @Description 首字母大写
	 * @author yangyh
	 * @date 2018年4月11日
	 * @version V1.0.0
	 */
	private static String upperCase(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}

	/**
	 * @Description 驼峰
	 * @author yangyh
	 * @date 2018年4月11日
	 * @version V1.0.0
	 */
	private static String toHump(String str) {
		str = str.toLowerCase();
		final StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("_(\\w)");
		Matcher m = p.matcher(str);
		while (m.find()) {
			m.appendReplacement(sb, m.group(1).toUpperCase());
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * @Description 生成实体类
	 * @author yangyh
	 * @date 2018年4月2日
	 * @version V1.0.0
	 */
	public static void generateEntity(TableInfo tableInfo, GenerateVO generateVO) throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setDirectoryForTemplateLoading(new File(TEMPLATES_PATH));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Template temp = cfg.getTemplate("entity.ftl");
		Map<String, Object> root = new HashMap<>();

		String tableName = upperCase(toHump(tableInfo.getTable_name()));
		String packageName = "entity";
		packageName = StringUtils.isBlank(generateVO.getEntityPackageName()) ? packageName : generateVO.getEntityPackageName();
		root.put("packageName", packageName);
		root.put("className", tableName);
		root.put("author", "yangyh");

		root.put("attrs", tableInfo);
		if (null != tableInfo.getExtendClass()) {
			Set<String> packageNameList = tableInfo.getPackage_name_list();
			root.put("extendClassParam", "extends Updatable ");
			Set<String> package_name_list = tableInfo.getPackage_name_list();
			if(CollectionUtils.isNotEmpty(package_name_list)){
				package_name_list.add("import com.hjh.mall.common.core.entity.Updatable;");
			}else {
				package_name_list = new HashSet<>();
				package_name_list.add("com.hjh.mall.common.core.entity");
			}
		}

		String defaultFilePath = "D:\\07git\\MySqlGenerater2Java\\src\\main\\java\\com\\generater\\entity";
		if ("1".equals(generateVO.getDownLoadMode())) {
			defaultFilePath = generateVO.getTempFilePath();
		}
		File dir = new File(defaultFilePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream fos = new FileOutputStream(new File(dir, tableName + ".java")); // java文件的生成目录
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
	public static void generateSqlMapper(TableInfo tableInfo,GenerateVO generateVO) throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setDirectoryForTemplateLoading(new File(TEMPLATES_PATH));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Template temp = cfg.getTemplate("mapper.ftl");
		Map<String, Object> root = new HashMap<String, Object>();

		String tableName = upperCase(toHump(tableInfo.getTable_name()));

		String entityPackageName = "entity";
		entityPackageName = StringUtils.isBlank(generateVO.getEntityPackageName()) ? entityPackageName : generateVO.getEntityPackageName();
		root.put("packageName", entityPackageName);
		root.put("className", tableName);
		String daoPackageName = "dao";
		daoPackageName = StringUtils.isBlank(generateVO.getDaoPackageName()) ? daoPackageName : generateVO.getDaoPackageName();
		root.put("daoPackageName", daoPackageName);

		root.put("attrs", tableInfo);

		String defaultFilePath = "D:\\07git\\MySqlGenerater2Java\\src\\main\\resources\\sqlmapper";
		if ("1".equals(generateVO.getDownLoadMode())) {
			defaultFilePath = generateVO.getTempFilePath();
		}
		File dir = new File(defaultFilePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream fos = new FileOutputStream(new File(dir, tableInfo.getTable_name() + "-sqlmapper.xml")); // java文件的生成目录
		Writer out = new OutputStreamWriter(fos);
		temp.process(root, out);

		fos.flush();
		fos.close();
	}

	/**
	 * @Description 生成dao, 没有实现baseDao
	 * @author yangyh
	 * @date 2018年4月8日
	 * @version V1.0.0
	 */
	public static void generateDao(TableInfo tableInfo,GenerateVO generateVO) throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setDirectoryForTemplateLoading(new File(TEMPLATES_PATH));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Template temp = cfg.getTemplate("dao.ftl");
		Map<String, Object> root = new HashMap<String, Object>();

		String tableName = upperCase(toHump(tableInfo.getTable_name()));

		String packageName = "dao";
		packageName = StringUtils.isBlank(generateVO.getDaoPackageName()) ? packageName : generateVO.getDaoPackageName();
		root.put("packageName", packageName);
		root.put("className", tableName + "Dao");
		root.put("entityName", tableName);
		String entityPackageName = "entity";
		entityPackageName = StringUtils.isBlank(generateVO.getEntityPackageName()) ? entityPackageName : generateVO.getEntityPackageName();
		root.put("entityPackageName", entityPackageName);
		root.put("author", "yangyh");

		root.put("attrs", tableInfo);

		String defaultFilePath = "D:\\07git\\MySqlGenerater2Java\\src\\main\\java\\com\\generater\\dao";
		if ("1".equals(generateVO.getDownLoadMode())) {
			defaultFilePath = generateVO.getTempFilePath();
		}
		File dir = new File(defaultFilePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream fos = new FileOutputStream(new File(dir, tableName + "Dao.java")); // java文件的生成目录
		Writer out = new OutputStreamWriter(fos);
		temp.process(root, out);

		fos.flush();
		fos.close();
	}

}
