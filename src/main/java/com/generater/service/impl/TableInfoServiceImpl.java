package com.generater.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.generater.entity.TableInfoTree;
import org.springframework.stereotype.Service;

import com.generater.service.TableInfoService;
import com.generater.util.ConnectionUtil;
import com.generater.util.GeneratorUtil;
import com.generater.util.TableInfo;
import com.generater.vo.GenerateVO;
import com.hjh.mall.common.core.entity.Updatable;

import freemarker.template.TemplateException;

@Service
public class TableInfoServiceImpl implements TableInfoService {

	private static String DRIVER = "com.mysql.jdbc.Driver";
	private static String URL = "jdbc:mysql://192.168.0.53:3306";
	private static String USER = "root";
	private static String PASSWORD = "1234";
	// private static String TABLE_SCHEMA = "test";

	@Override
	public List<TableInfoTree> querySchemaList() throws ClassNotFoundException, SQLException {

		return getSchemaList();
	}

	private List<TableInfoTree> getSchemaList() throws ClassNotFoundException, SQLException {
		List<TableInfoTree> result = new ArrayList<>();
		Connection con;
		Class.forName(DRIVER);
		con = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement statement = con.createStatement();

		String sql = "SELECT  DISTINCT TABLE_SCHEMA as text FROM information_schema.`TABLES` ";
		ResultSet rs = statement.executeQuery(sql);

		while (rs.next()) {
			TableInfoTree tableInfoTree = new TableInfoTree();
			tableInfoTree.setId(rs.getString("text"));
			tableInfoTree.setText(rs.getString("text"));
			tableInfoTree.setLevel("schema");
			result.add(tableInfoTree);
		}

		rs.close();
		con.close();
		return result;
	}

	@Override
	public List<TableInfoTree> queryTableList(String schema) throws ClassNotFoundException, SQLException {

		return getTableList(schema);
	}

	private List<TableInfoTree> getTableList(String schema) throws ClassNotFoundException, SQLException {
		List<TableInfoTree> result = new ArrayList<>();
		Connection con;
		Class.forName(DRIVER);
		con = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement statement = con.createStatement();

		String sql = "SELECT  TABLE_NAME  FROM information_schema.`TABLES` where TABLE_SCHEMA = '" + schema + "'";
		ResultSet rs = statement.executeQuery(sql);

		while (rs.next()) {
			TableInfoTree tableInfoTree = new TableInfoTree();
			tableInfoTree.setId(rs.getString("TABLE_NAME"));
			tableInfoTree.setText(rs.getString("TABLE_NAME"));
			tableInfoTree.setLevel("table");
			tableInfoTree.setParent_text(schema);
			result.add(tableInfoTree);
		}

		rs.close();
		con.close();
		return result;
	}

	@Override
	public void generate(GenerateVO generateVO)
			throws ClassNotFoundException, SQLException, IOException, TemplateException {
		generateVO.getList().forEach(item -> {
			// 生成实体类
			TableInfo tableInfo = null;
			try {
				tableInfo = ConnectionUtil.getTableInfo(item.getTable_name(),item.getSchema());
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

			 tableInfo.setExtendClass(Updatable.class);
			 tableInfo = ConnectionUtil.checkAndRemove(tableInfo);

			// 生成实体类
			try {
				GeneratorUtil.generateEntity(tableInfo,generateVO);
			} catch (IOException | TemplateException e) {
				e.printStackTrace();
			}

			// 生成sqlmapper
			try {
				GeneratorUtil.generateSqlMapper(tableInfo,generateVO);
			} catch (IOException | TemplateException e) {
				e.printStackTrace();
			}

			// 生成dao,没有实现baseDao
			try {
				GeneratorUtil.generateDao(tableInfo,generateVO);
			} catch (IOException | TemplateException e) {
				e.printStackTrace();
			}
		});

	}

}
