package com.nfschina.aiot.db;

import java.net.IDN;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.AlarmEntity;
import com.nfschina.aiot.entity.CarbonDioxideEntity;
import com.nfschina.aiot.entity.InstructionEntity;


/**
 * 连接数据库
 * @author xu
 *
 */

public class AccessDataBase {

	// 连接字符串
	private static String mUrl = "jdbc:mysql://10.50.200.120:3306/zkghdb?"
			+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

	// resultcode
	private static int mResultCode = Constant.SERVER_CONNECT_FAILED;
	// 连接connection
	private static Connection mConn = null;

	/**
	 * 获取statement
	 * @return statement
	 */
	private static Statement getStatement() {
		Statement statement = null;
		mResultCode = Constant.SERVER_CONNECT_FAILED;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.setLoginTimeout(Constant.TIME_OUT);
			mConn = DriverManager.getConnection(mUrl);
			statement = (Statement) mConn.createStatement();
		} catch (ClassNotFoundException e) {
			mResultCode = Constant.SERVER_UNKNOWN_FAILED;
			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			mResultCode = Constant.SERVER_CONNECT_FAILED;
			e.printStackTrace();
		}
		return statement;
	}

	/**
	 * 连接服务器执行登录
	 * 
	 * @param name 登录名
	 * @param pswd 密码
	 * @return the resultcode
	 * @throws Exception
	 */
	public static int connectLogin(String name, String pswd) throws Exception {

		mResultCode = Constant.SERVER_CONNECT_FAILED;

		Statement stmt = getStatement();
		if (stmt != null) {

			String sql = "select * from sys_user where USER_ID='" + name + "' and USER_PASSWORD='" + pswd + "'";
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
	 * 连接服务器注册
	 * 
	 * @param id 登录名
	 * @param pswd 密码
	 * @return the resultcode
	 */
	public static int connectRegister(String id, String pswd) {
		mResultCode = Constant.SERVER_CONNECT_FAILED;
		Statement stmt = getStatement();
		if (stmt != null) {
			String sql = "select * from sys_user where USER_ID='" + id + "'";
			try {
				ResultSet rs = stmt.executeQuery(sql);
				if (rs != null && rs.next()) {
					mResultCode = Constant.SERVER_REGISTER_EXIST;
					rs.close();

				} else {
					Date date=new Date();
					DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time=format.format(date);
					sql = "insert into sys_user (USER_ID,USER_PASSWORD,ADD_TIME) values('" + id + "','" + pswd + "','" + time + "')" ;
					int rows = stmt.executeUpdate(sql);
					if(rows>0)
						mResultCode = Constant.SERVER_REGISTER_SUCCESS;
					else
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
	 * 连接服务器修改密码
	 * 
	 * @param id 用户名
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return the resultcode
	 */
	public static int connectChangePassword(String id, String oldPassword, String newPassword) {

		mResultCode = Constant.SERVER_CONNECT_FAILED;

		Statement stmt = getStatement();
		if (stmt != null) {

			String sql = "select * from sys_user where USER_ID='" + id + "' and USER_PASSWORD='" + oldPassword + "'";
			ResultSet rs = null;
			try {
				rs = stmt.executeQuery(sql);
				if (rs != null && rs.next()) {
					sql = "update sys_user set USER_PASSWORD='" + newPassword + "' where USER_ID='" + id + "'";
					int row = stmt.executeUpdate(sql);
					if (row == 1) {
						mResultCode = Constant.SERVER_CHANGE_PASSWORD_SUCCESS;
						Constant.CURRENT_PASSWORD = newPassword;
					}
					rs.close();
				} else {
					mResultCode = Constant.SERVER_CHANGE_PASSWORD_FAILED;
					if(rs != null)
						rs.close();
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
	 * 关闭连接
	 */
	private static void closeConnection() {
		if (mConn != null) {
			try {
				mConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				mConn = null;
			}
		}
	}

	/**
	 * 从服务器获取报警记录
	 * 
	 * @param page 获取第几页的数据
	 * @param size 每一页有多少条数据
	 * @return 报警记录实体的list
	 */
	public static List<AlarmEntity> getAlarmHistoryData(int page, int size) {
		List<AlarmEntity> result = null;
		Statement statement = getStatement();
		if (statement != null) {
			String sql = "select * from warningvaluetb limit " + page * size + "," + size;
			ResultSet rs = null;
			try {
				rs = statement.executeQuery(sql);
				while (rs != null && rs.next()) {
					AlarmEntity alarmEntity = new AlarmEntity(rs.getInt("warningvalueid"), rs.getInt("greenhouseid"), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7));
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
	 * 从服务器获取指令记录
	 * 
	 * @param page 获取第几页的数据
	 * @param size 每一页有多少条数据
	 * @return 指令记录实体的list
	 */
	public static List<InstructionEntity> getInstructionHistoryData(int page, int size) {
		List<InstructionEntity> result = null;
		Statement statement = getStatement();
		if (statement != null) {
			String sql = "select * from instructiontb limit " + page * size + "," + size;
			ResultSet rs = null;
			try {
				rs = statement.executeQuery(sql);
				while (rs != null && rs.next()) {
					InstructionEntity instructionEntity = new InstructionEntity(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
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

	
	/**
	 * 获取大棚的名字
	 * @return 大棚名字的list
	 */
	public static List<Integer> getGreenHouseName() {
//		List<Integer> result = null;
//		Statement statement = getStatement();
//		if (statement != null) {
//			String sql = "select UserID from UserInfoTB where UserName='" + Constant.CURRENT_USER +"' and Password='" + Constant.CURRENT_PASSWORD +"'" ;
//			ResultSet rs = null;
//			try {
//				rs = statement.executeQuery(sql);
//				if (rs != null && rs.next()) {
//					int userID = rs.getInt(0);
//					rs.close();
//					sql = "select distinct GreenHouseID from GreehHouseDeviceTB where UserID=" + userID;
//					rs = statement.executeQuery(sql);
//					while(rs != null && rs.next()){
//						if(result == null)
//							result = new ArrayList<Integer>();
//						result.add(rs.getInt(0));
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				try {
//					statement.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				closeConnection();
//			}
//		}
//
//		closeConnection();
//		return result;
		List<Integer> result = new ArrayList<Integer>();
		for(int i = 0;i < 10 ; ++ i)
			result.add(i);
		return result;
	}
		
	
	/**
	 * 获取二氧化碳的数据
	 * @param greenHouseID 大棚ID
	 * @return 二氧化碳的数据的list
	 */
	public static List<CarbonDioxideEntity> getCarbonDioxideData(int greenHouseID){
		List<CarbonDioxideEntity> result = null;
		Statement statement = getStatement();
		if(statement != null){
			String sql = "select * from ";
		}
		closeConnection();
		return result;
	}
	
	
}
