package com.generater.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class TableInfoTree implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; // 人员编号
	private String text; // 人员名称
	private Boolean checked = false; // 是否选中
	private String level;
	private String parent_text;
	private List<TableInfoTree> children; // 子节点
}
