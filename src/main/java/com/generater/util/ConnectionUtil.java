package com.generater.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.generater.util.TableInfo.FieldInfo;

import freemarker.template.TemplateException;

public class ConnectionUtil {

	private static ThreadLocal<Set<String>> PACKAGE_NAME_SET = new ThreadLocal<>();
	private static String DRIVER = "com.mysql.jdbc.Driver";
	private static String URL = "jdbc:mysql://xxx.xxx.xxx.xxx:3306";
	private static String USER = "xxx";
	private static String PASSWORD = "xxx";
	private static String TABLE_SCHEMA = "xxx";

	public static void main(String[] args) throws IOException, TemplateException, ClassNotFoundException, SQLException {

		getTableInfo("xxx");

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
			System.out.println(rs.getString("COLUMN_NAME"));
		}

		tableInfo.setField_info_list(fieldInfoList);
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
