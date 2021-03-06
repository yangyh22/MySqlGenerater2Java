package com.generater.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GenerateVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * tempFilePath
	 */
	private String tempFilePath;

	/**
	 * 生成之后的文件采取下载模式
	 */
	private String downLoadMode = "1";

	/**
	 * 实体类包名
	 */
	private String entityPackageName;
	/**
	 * dao包名
	 */
	private String daoPackageName;

	@Valid
	private List<singleGenerateVO> list;

	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class singleGenerateVO implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private String table_name;

		private String schema;

	}
}
