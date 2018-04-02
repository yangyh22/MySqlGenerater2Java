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
	 * 字段名称列表
	 */
	List<FieldInfo> field_info_list;

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