package com.nfschina.aiot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.AlarmEntity;
import com.nfschina.aiot.entity.InstructionEntity;

public class AccessDataBase {

	// the connection string
	private static String mUrl = "jdbc:mysql://10.50.200.236:3306/javademo?"
			+ "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";

	// the result code
	private static int mResultCode = Constant.SERVER_CONNECT_FAILED;
	// the connection
	private static Connection mConn = null;

	private static Statement getStatement() {
		Statement statement = null;
		mResultCode = Constant.SERVER_CONNECT_FAILED;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			mConn = DriverManager.getConnection(mUrl);
			statement = (Statement) mConn.createStatement();
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
	public static int connectLogin(String name, String pswd) throws Exception {

		mResultCode = Constant.SERVER_CONNECT_FAILED;

		Statement stmt = getStatement();
		if (stmt != null) {

			String sql = "select * from info where id=1";
			ResultSet rs = null;
			try {
				rs = stmt.executeQuery(sql);
				if (rs != null && rs.next()) {
					mResultCode = Constant.SERVER_LOGIN_SUCCESS;
				} else {
					mResultCode = Constant.SERVER_LOGIN_FAILED;
				}
			} catch (java.sql.SQLException e) {
				mResultCode = Constant.SERVER_SQL_FAILED;
				e.printStackTrace();
			} finally {
				if (rs != null)
					rs.close();
				stmt.close();
				closeConnection();
			}
		}
		closeConnection();

		return mResultCode;
	}

	/**
	 * connect to the server and register
	 * 
	 * @param name
	 *            the user name
	 * @param pswd
	 *            the password
	 * @return the resultcode in the Constant
	 */
	public static int connectRegister(String name, String pswd) {
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
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				closeConnection();

			}
		}
		closeConnection();
		return mResultCode;
	}

	/**
	 * connect to the server and change the password
	 * 
	 * @param name
	 *            the user name
	 * @param oldPassword
	 *            the old password
	 * @param newPassword
	 *            the new password
	 * @return the resultcode in the Constant
	 */
	public static int connectChangePassword(String name, String oldPassword, String newPassword) {

		mResultCode = Constant.SERVER_CONNECT_FAILED;

		Statement stmt = getStatement();
		if (stmt != null) {

			String sql = "select * from info where id=1";
			ResultSet rs = null;
			try {
				rs = stmt.executeQuery(sql);
				if (rs != null && rs.next()) {
					sql = "";
					int row = stmt.executeUpdate(sql);
					if (row == 1) {
						mResultCode = Constant.SERVER_CHANGE_PASSWORD_SUCCESS;
					}
				} else {
					mResultCode = Constant.SERVER_CHANGE_PASSWORD_FAILED;
				}
			} catch (java.sql.SQLException e) {
				mResultCode = Constant.SERVER_SQL_FAILED;
				e.printStackTrace();
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				closeConnection();
			}
		}
		closeConnection();
		return mResultCode;
	}

	/**
	 * close the connection
	 */
	private static void closeConnection() {
		if (mConn != null) {
			try {
				mConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mConn = null;
		}
	}

	/**
	 * get the alarm history data from the remote database
	 * @param page the page of the data
	 * @param size the count of one page
	 * @return the list data of the alarm history
	 */
	public static List<AlarmEntity> getAlarmHistoryData(int page, int size) {
		List<AlarmEntity> result = null;
		Statement statement = getStatement();
		if (statement != null) {
			String sql = "select * from Warning limit " +page*size+","+size;
			ResultSet rs = null;
			try {
				rs = statement.executeQuery(sql);
				while (rs != null && rs.next()) {
					AlarmEntity alarmEntity = new AlarmEntity(rs.getInt(1), rs.getInt(2), rs.getString(4),
							rs.getTimestamp(5), rs.getString(6), rs.getString(7));
					if (result == null)
						result = new ArrayList<AlarmEntity>();
					result.add(alarmEntity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				closeConnection();
			}
		}
		closeConnection();
		return result;
	}

	/**
	 * get the instruction data from the remote database
	 * @param page the page of the data
	 * @param size the count of one page
	 * @return the list data of the instruction history
	 */
	public static List<InstructionEntity> getInstructionHistoryData(int page, int size) {
		List<InstructionEntity> result = null;
		Statement statement = getStatement();
		if (statement != null) {
			String sql = "";
			ResultSet rs = null;
			try {
				rs = statement.executeQuery(sql);
				while (rs != null && rs.next()) {
					InstructionEntity instructionEntity = new InstructionEntity(rs.getInt(0), rs.getString(1), rs.getTimestamp(2), rs.getTimestamp(3), rs.getInt(4), rs.getInt(5));
					if (result == null)
						result = new ArrayList<InstructionEntity>();
					result.add(instructionEntity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				closeConnection();
			}
		}
		closeConnection();
		return result;
	}
}
