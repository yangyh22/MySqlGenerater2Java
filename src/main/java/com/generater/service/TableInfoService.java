package com.generater.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.generater.entity.TableInfoTree;
import com.generater.vo.GenerateVO;

import freemarker.template.TemplateException;

public interface TableInfoService {

	/**
	 * @Description 查询表名称列表
	 * @author yangyh
	 * @date 2018年4月8日
	 * @version V1.0.0
	 */
	List<TableInfoTree> querySchemaList() throws ClassNotFoundException, SQLException;

	List<TableInfoTree> queryTableList(String schema) throws ClassNotFoundException, SQLException;

	void generate(GenerateVO generateVO) throws ClassNotFoundException, SQLException, IOException, TemplateException;

}
