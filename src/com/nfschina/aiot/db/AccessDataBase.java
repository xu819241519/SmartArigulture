package com.nfschina.aiot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import com.mysql.jdbc.Statement;
import com.nfschina.aiot.constant.Constant;

import android.R.bool;

public class AccessDataBase {

	private static String mUrl = "jdbc:mysql://10.50.200.236:3306/javademo?"
			+ "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";

	private static int mResultCode = Constant.SERVER_CONNECT_FAILED;

	private static Statement getStatement() {
		Statement statement = null;
		mResultCode = Constant.SERVER_CONNECT_FAILED;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(mUrl);
			statement = (Statement) conn.createStatement();
		} catch (ClassNotFoundException e) {
			mResultCode = Constant.SERVER_UNKNOWN_FAILED;
			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			mResultCode = Constant.SERVER_SQL_FAILED;
			e.printStackTrace();
		}
		return statement;
	}

	/**
	 * connect to the server and perform login
	 * 
	 * @param name
	 *            the login name
	 * @param pswd
	 *            the login password
	 * @return the resultcode in the Constant
	 * @throws Exception
	 */
	public static int ConnectLogin(String name, String pswd) throws Exception {

		mResultCode = Constant.SERVER_CONNECT_FAILED;

		Statement stmt = getStatement();
		if (stmt != null) {

			String sql = "select * from info where id=1";

			try {
				ResultSet rs = stmt.executeQuery(sql);
				if (rs != null && rs.next()) {
					mResultCode = Constant.SERVER_LOGIN_SUCCESS;
				} else {
					mResultCode = Constant.SERVER_LOGIN_FAILED;
				}
			} catch (java.sql.SQLException e) {
				mResultCode = Constant.SERVER_SQL_FAILED;
				e.printStackTrace();
			}

		}
		return mResultCode;
	}

	
	/**
	 * connect to the server and register
	 * @param name the user name
	 * @param pswd the password
	 * @return the resultcode in the Constant
	 */
	public static int ConnectRegister(String name, String pswd) {
		mResultCode = Constant.SERVER_CONNECT_FAILED;
		Statement stmt = getStatement();
		if (stmt != null) {
			String sql = "insert into ";
			try {
				boolean rs = stmt.execute(sql);
				if (rs) {
					mResultCode = Constant.SERVER_REGISTER_SUCCESS;

				} else {

					mResultCode = Constant.SERVER_REGISTER_FAILED;
				}

			} catch (java.sql.SQLException e) {
				mResultCode = Constant.SERVER_SQL_FAILED;
				e.printStackTrace();
			}
		}
		return mResultCode;
	}
	
	
	
	/**
	 * connect to the server and change the password
	 * @param name the user name
	 * @param oldPassword the old password
	 * @param newPassword the new password
	 * @return the resultcode in the Constant
	 */
	public static int ConnectChangePassword(String name, String oldPassword, String newPassword) {

		mResultCode = Constant.SERVER_CONNECT_FAILED;

		Statement stmt = getStatement();
		if (stmt != null) {

			String sql = "select * from info where id=1";

			try {
				ResultSet rs = stmt.executeQuery(sql);
				if (rs != null && rs.next()) {
					sql = "";
					int row = stmt.executeUpdate(sql);
					if(row == 1){
						mResultCode = Constant.SERVER_CHANGE_PASSWORD_SUCCESS;
					}
				} else {
					mResultCode = Constant.SERVER_CHANGE_PASSWORD_FAILED;
				}
			} catch (java.sql.SQLException e) {
				mResultCode = Constant.SERVER_SQL_FAILED;
				e.printStackTrace();
			}
		}
		return mResultCode;
	}

}
