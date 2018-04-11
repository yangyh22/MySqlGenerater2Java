package com.generater.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GenerateVO implements Serializable {

	@NotBlank
	private String ww;

	@NotBlank
	private String qq;

	@NotNull
	private List<singleGenerateVO> list;

	@Data
	public static class singleGenerateVO implements Serializable {

		private List<String> table_name_list;

		private String schema;

	}
}
