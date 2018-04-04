package com.generater.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.generater.util.TableInfo.FieldInfo;

import freemarker.template.TemplateException;

public class ConnectionUtil {

	private static ThreadLocal<Set<String>> PACKAGE_NAME_SET = new ThreadLocal<>();

	private static String DRIVER = "com.mysql.jdbc.Driver";
	private static String URL = "jdbc:mysql://192.168.0.53:3306";
	private static String USER = "root";
	private static String PASSWORD = "1234";
	private static String TABLE_SCHEMA = "wx_mall";

	public static void main(String[] args) throws IOException, TemplateException, ClassNotFoundException, SQLException {

		TableInfo tableInfo = getTableInfo("goods");

		tableInfo = checkAndRemove(tableInfo);

		// tableInfo = check(Updatable.class, tableInfo);

		System.err.println("");

		List<FieldInfo> field_info_list = tableInfo.getJava_field_info_list();
		field_info_list.forEach(fieldInfo -> System.err.println(fieldInfo.getName()));
		System.out.println("finish");

	}

	/**
	 * @Description 递归校验父类,去掉父类中已经存在的字段
	 * @author yangyh
	 * @date 2018年4月4日
	 * @version V1.0.0
	 */
	public static TableInfo checkAndRemove(TableInfo tableInfo) {
		Class<?> superclass = tableInfo.getCurrentClass().getSuperclass();
		if (Object.class.equals(superclass)) {
			// 校验
			return removeField(tableInfo);
		} else {
			// 递归校验父类,去掉父类中已经存在的字段
			return checkAndRemove(removeField(tableInfo));
		}
	}

	/**
	 * @param 校验
	 */
	private static TableInfo removeField(TableInfo oldTableInfo) {
		Class<?> cla = oldTableInfo.getCurrentClass();
		List<Field> fieldList = Arrays.asList(cla.getDeclaredFields());
		List<FieldInfo> olfFieldInfoList = oldTableInfo.getJava_field_info_list();

		TableInfo addTableInfo = new TableInfo();
		addTableInfo.setTable_name(oldTableInfo.getTable_name());
		addTableInfo.setPk(oldTableInfo.getPk());
		addTableInfo.setPackage_name_list(oldTableInfo.getPackage_name_list());
		addTableInfo.setCurrentClass(cla.getSuperclass());// 重要,为了递归
		addTableInfo.setMysql_field_info_list(oldTableInfo.getMysql_field_info_list());

		// 匹配出父类没有的字段列表
		List<FieldInfo> addList = olfFieldInfoList.stream().filter(fieldInfo -> {
			return !fieldList.stream().anyMatch(field -> {
				return fieldInfo.getName().equals(field.getName()) ? true : false;
			});
		}).collect(Collectors.toList());
		addTableInfo.setJava_field_info_list(addList);

		return addTableInfo;
	}

	private static void init() {
		PACKAGE_NAME_SET.remove();
		Set<String> set = new HashSet<>();
		PACKAGE_NAME_SET.set(set);
	}

	/**
	 * @Description 获得表信息
	 * @author yangyh
	 * @date 2018年4月2日
	 * @version V1.0.0
	 */
	public static TableInfo getTableInfo(String tableName) throws ClassNotFoundException, SQLException {
		init();
		Connection con;
		Class.forName(DRIVER);
		con = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement statement = con.createStatement();

		String sql = "SELECT  COLUMN_NAME, DATA_TYPE,COLUMN_COMMENT,TABLE_NAME FROM information_schema.columns WHERE table_name = '"
				+ tableName + "' AND TABLE_SCHEMA = '" + TABLE_SCHEMA + "' order by COLUMN_KEY desc";
		ResultSet rs = statement.executeQuery(sql);

		String pk = null;
		List<FieldInfo> fieldInfoList = new ArrayList<>();
		TableInfo tableInfo = new TableInfo();
		while (rs.next()) {
			if (StringUtils.isBlank(pk)) {
				pk = rs.getString("COLUMN_NAME");
				tableInfo.setPk(pk);
			}
			tableInfo.setTable_name(rs.getString("TABLE_NAME"));

			FieldInfo fieldInfo = new FieldInfo();
			fieldInfo.setColumn_comment(rs.getString("COLUMN_COMMENT"));
			fieldInfo.setMysql_type(rs.getString("DATA_TYPE"));
			fieldInfo.setName(rs.getString("COLUMN_NAME"));
			fieldInfo.setJava_type(paseJavaType(fieldInfo.getMysql_type()));

			fieldInfoList.add(fieldInfo);
			// System.out.println(rs.getString("COLUMN_NAME"));
		}

		tableInfo.setJava_field_info_list(fieldInfoList);
		tableInfo.setMysql_field_info_list(fieldInfoList);
		tableInfo.setPackage_name_list(PACKAGE_NAME_SET.get());

		rs.close();
		con.close();
		return tableInfo;
	}

	private static String paseJavaType(String mysql_type) {
		Set<String> set = PACKAGE_NAME_SET.get();
		String javaType = "";
		JavaTypeEnum verifyType = JavaTypeEnum.val(mysql_type);
		switch (verifyType) {
		case BIGDECIMAL:
			javaType = JavaTypeEnum.BIGDECIMAL.getDescription();
			set.add(JavaTypeEnum.BIGDECIMAL.getPackageName());
			break;
		case DOUBLE:
			javaType = JavaTypeEnum.DOUBLE.getDescription();
			break;
		case INTEGER:
			javaType = JavaTypeEnum.INTEGER.getDescription();
			break;
		case STRING:
			javaType = JavaTypeEnum.STRING.getDescription();
			break;
		default:
			break;
		case DEFAULT:
			javaType = JavaTypeEnum.DEFAULT.getDescription();
			break;

		}
		PACKAGE_NAME_SET.set(set);
		return javaType;
	}

}
