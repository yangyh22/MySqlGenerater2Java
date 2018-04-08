package com.generater.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.generater.entity.TableInfoTree;
import com.generater.service.TableInfoService;

@Service
public class TableInfoServiceImpl implements TableInfoService {

	private static String DRIVER = "com.mysql.jdbc.Driver";
	private static String URL = "jdbc:mysql://192.168.0.53:3306";
	private static String USER = "root";
	private static String PASSWORD = "1234";
	private static String TABLE_SCHEMA = "test";

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

		String pk = null;
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

		String pk = null;
		while (rs.next()) {
			TableInfoTree tableInfoTree = new TableInfoTree();
			tableInfoTree.setId(rs.getString("TABLE_NAME"));
			tableInfoTree.setText(rs.getString("TABLE_NAME"));
			tableInfoTree.setLevel("table");
			result.add(tableInfoTree);
		}

		rs.close();
		con.close();
		return result;
	}

}
