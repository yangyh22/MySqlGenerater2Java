package com.generater.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class GenerateVO implements Serializable {

	private List<singleGenerateVO> list;

	@Data
	public static class singleGenerateVO implements Serializable {

		private List<String> table_name_list;

		private String schema;

	}
}
