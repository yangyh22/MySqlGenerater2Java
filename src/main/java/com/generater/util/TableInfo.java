package com.generater.util;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class TableInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 需要导入的包名列表
	 */
	Set<String> package_name_list;

	/**
	 * 表名称
	 */
	String table_name;

	/**
	 * 主键
	 */
	String pk;

	/**
	 * java字段名称列表,当实体类继承基础的VO以后，这个列表与mysql_field_info_list是不同的
	 */
	List<FieldInfo> java_field_info_list;

	/**
	 * mysql字段名称列表
	 */
	List<FieldInfo> mysql_field_info_list;

	/**
	 * 当前的class的名称，用来处理实体类继承基础的VO 以后，删除基础的VO 存在的字段
	 */
	Class<?> extendClass;

	@Data
	public static class FieldInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String java_type;
		private String mysql_type;
		private String name;
		private String column_comment;

	}

}