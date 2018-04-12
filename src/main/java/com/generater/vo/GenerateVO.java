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

		@NotEmpty
		private List<String> table_name_list;

		@NotBlank
		private String schema;

	}
}
