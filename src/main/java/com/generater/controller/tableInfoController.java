package com.generater.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generater.entity.TableInfoTree;
import com.generater.service.TableInfoService;

/**
 * @Description 表结构信息的conroller
 * @author yangyh
 * @date 2018年4月8日
 * @version V1.0.0
 */
@RestController
@RequestMapping(value = "/tableInfo")
public class tableInfoController {

	@Autowired
	TableInfoService tableInfoService;

	@RequestMapping("/schemaList")
	public List<TableInfoTree> q() throws ClassNotFoundException, SQLException {
		return tableInfoService.querySchemaList();
	}

	@RequestMapping("/tableList")
	public List<TableInfoTree> w(String schema) throws ClassNotFoundException, SQLException {
		return tableInfoService.queryTableList(schema);
	}

}
