package com.nfschina.aiot.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 数据库连接工具类
 * @author wujian
 */
public class DBConnectionUtil {
	public static final String driveName = "com.mysql.jdbc.Driver";  
    public static final String username = "root";  
    public static final String password = "root";  
    public static final String url =  "jdbc:mysql://10.50.200.120:3306/zkghdb";
    //public static final String url =  "jdbc:mysql://222.173.73.19:3306/zkghdb";
	public Connection connection = null;
	public PreparedStatement pst ;
	public DBConnectionUtil(String sql){
		try {  
            Class.forName(driveName);//指定连接类型  
            connection = DriverManager.getConnection(url, username, password);//获取连接  
            pst = connection.prepareStatement(sql);//准备执行语句  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	public void close(){
		try {
			this.pst.close();
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		String sql = "select * from thresholdtb";
		DBConnectionUtil connectionUtil = new DBConnectionUtil(sql);
		ResultSet resultSet = null;
		try {
			resultSet = connectionUtil.pst.executeQuery();
			System.out.println(resultSet.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
