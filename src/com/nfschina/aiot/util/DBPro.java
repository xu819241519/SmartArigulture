package com.nfschina.aiot.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * 存储过程工具类
 * @author wujian
 */
public class DBPro {
	private static final String driveName = "com.mysql.jdbc.Driver";  
	private static final String username = "root";  
	private static final String password = "root";  
	//private static final String url =  "jdbc:mysql://222.173.73.19:3306/zkghdb";
	private static final String url =  "jdbc:mysql://10.50.200.120:3306/zkghdb";
	private Connection connection = null;
	public CallableStatement cs;
	
	public DBPro(String sql){
		try {
			Class.forName(driveName);
			DriverManager.setLoginTimeout(8000);
			connection = DriverManager.getConnection(url, username, password);
			cs = connection.prepareCall(sql);
			cs.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void close(){
		try {
			cs.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
