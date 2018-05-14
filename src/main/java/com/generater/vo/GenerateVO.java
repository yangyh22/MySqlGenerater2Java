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
	 * 实体类包名
	 */
	private String entityPackageName;
	/**
	 * dao包名
	 */
	private String daoPackageName;

	@Valid
	@NotEmpty
	private List<singleGenerateVO> list;

	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class singleGenerateVO implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@NotBlank
		private String table_name;

		@NotBlank
		private String schema;

	}
}
