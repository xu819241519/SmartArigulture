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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.AlarmEntity;
import com.nfschina.aiot.entity.CarbonDioxideEntity;
import com.nfschina.aiot.entity.EnvironmentParameterEntity;
import com.nfschina.aiot.entity.GreenHouseEntity;
import com.nfschina.aiot.entity.HumidityEntity;
import com.nfschina.aiot.entity.IlluminanceEntity;
import com.nfschina.aiot.entity.InstructionEntity;
import com.nfschina.aiot.entity.TemperatureEntity;

import android.database.CursorJoiner.Result;

/**
 * 连接数据库
 * 
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
	 * 
	 * @return statement
	 */
	private static PreparedStatement getPreparedStatement(String sql) {
		PreparedStatement statement = null;
		mResultCode = Constant.SERVER_CONNECT_FAILED;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.setLoginTimeout(Constant.TIME_OUT);
			mConn = DriverManager.getConnection(mUrl);
			statement = (PreparedStatement) mConn.prepareStatement(sql);
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
	 * @param name
	 *            登录名
	 * @param pswd
	 *            密码
	 * @return the resultcode
	 * @throws Exception
	 */
	public static int connectLogin(String name, String pswd) throws Exception {

		mResultCode = Constant.SERVER_CONNECT_FAILED;
		String sql = "select * from sys_user where USER_ID=? and USER_PASSWORD=?";
		PreparedStatement stmt = getPreparedStatement(sql);
		if (stmt != null) {
			stmt.setString(1, name);
			stmt.setString(2, pswd);
			ResultSet rs = null;
			try {
				rs = stmt.executeQuery();
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
	 * @param id
	 *            登录名
	 * @param pswd
	 *            密码
	 * @return the resultcode
	 */
	public static int connectRegister(String id, String pswd) {
		mResultCode = Constant.SERVER_CONNECT_FAILED;
		String sql = "select * from sys_user where USER_ID=?";
		PreparedStatement stmt = getPreparedStatement(sql);
		if (stmt != null) {
			try {
				stmt.setString(1, id);
				ResultSet rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					mResultCode = Constant.SERVER_REGISTER_EXIST;
					rs.close();

				} else {
					if (rs != null)
						rs.close();
					stmt.close();
					closeConnection();
					Date date = new Date();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = format.format(date);
					sql = "insert into sys_user (USER_ID,USER_PASSWORD,ADD_TIME) values(?,?,?)";
					stmt = getPreparedStatement(sql);
					stmt.setString(1, id);
					stmt.setString(2, pswd);
					stmt.setString(3, time);
					int rows = stmt.executeUpdate();
					if (rows > 0) {
						mResultCode = Constant.SERVER_REGISTER_SUCCESS;
					} else
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
	 * @param id
	 *            用户名
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @return the resultcode
	 */
	public static int connectChangePassword(String id, String oldPassword, String newPassword) {

		mResultCode = Constant.SERVER_CONNECT_FAILED;
		String sql = "select * from sys_user where USER_ID= ? and USER_PASSWORD= ?";
		PreparedStatement stmt = getPreparedStatement(sql);
		if (stmt != null) {
			ResultSet rs = null;
			try {
				stmt.setString(1, id);
				stmt.setString(2, oldPassword);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					rs.close();
					stmt.close();
					closeConnection();
					sql = "update sys_user set USER_PASSWORD= ? where USER_ID= ?";
					stmt = getPreparedStatement(sql);
					stmt.setString(1, newPassword);
					stmt.setString(2, id);
					int row = stmt.executeUpdate();
					if (row == 1) {
						mResultCode = Constant.SERVER_CHANGE_PASSWORD_SUCCESS;
						Constant.CURRENT_PASSWORD = newPassword;
					}
				} else {
					mResultCode = Constant.SERVER_CHANGE_PASSWORD_FAILED;
					if (rs != null)
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
	 * @param page
	 *            获取第几页的数据
	 * @param size
	 *            每一页有多少条数据
	 * @return 报警记录实体的list
	 */
	public static List<AlarmEntity> getAlarmHistoryData(int page, int size, String GreenHouseID) {
		List<AlarmEntity> result = null;
		Connection conn = null;
		java.sql.CallableStatement comm = null;
		ResultSet ds = null;
		String commStr = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.setLoginTimeout(Constant.TIME_OUT);
			conn = DriverManager.getConnection(mUrl);
			commStr = "call getAllGreenHouseInfo(?,?,?)";
			comm = conn.prepareCall(commStr);
			comm.setString(1, "0000");
			comm.setInt(2, 0);
			comm.setInt(3, 10);
			comm.execute();
			ds = comm.getResultSet();
			while (ds.next()) {
				System.out.println(ds.getString(1));
			}
			comm.getMoreResults();
			ds = comm.getResultSet();
			while(ds.next()){
				System.out.println(ds.getString(1));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// String sql = "select * from warningtb where greenhouseid= ? limit
		// ?,?";
		// PreparedStatement statement = getPreparedStatement(sql);
		// if (statement != null) {
		// ResultSet rs = null;
		// try {
		// statement.setString(1, GreenHouseID);
		// statement.setInt(2, page * size);
		// statement.setInt(3, size);
		// rs = statement.executeQuery();
		// while (rs != null && rs.next()) {
		// AlarmEntity alarmEntity = new AlarmEntity(rs.getInt(1),
		// rs.getString(2), rs.getFloat(4),
		// rs.getInt(5), rs.getFloat(6), rs.getInt(7), rs.getString(8),
		// rs.getInt(9));
		// if (result == null)
		// result = new ArrayList<AlarmEntity>();
		// result.add(alarmEntity);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// if (rs != null)
		// rs.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// try {
		// statement.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// closeConnection();
		// }
		// }
		// closeConnection();
		return result;
	}

	/**
	 * 从服务器获取指令记录
	 * 
	 * @param page
	 *            获取第几页的数据
	 * @param size
	 *            每一页有多少条数据
	 * @return 指令记录实体的list
	 */
	public static List<InstructionEntity> getInstructionHistoryData(int page, int size, String greenHouseID) {
		List<InstructionEntity> result = null;
		String sql = "select * from instructiontb where greenhouseid= ? limit ?,?";
		PreparedStatement statement = getPreparedStatement(sql);
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				statement.setInt(2, page * size);
				statement.setInt(3, size);
				rs = statement.executeQuery();
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
	 * 
	 * @return 大棚名字的list
	 */
	public static List<GreenHouseEntity> getGreenHouseName() {
		List<GreenHouseEntity> result = null;
		String sql = "select * from sys_user where USER_ID= ? and USER_PASSWORD= ?";
		PreparedStatement statement = getPreparedStatement(sql);
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, Constant.CURRENT_USER);
				statement.setString(2, Constant.CURRENT_PASSWORD);
				rs = statement.executeQuery();
				while (rs != null && rs.next()) {
					rs.close();
					statement.close();
					closeConnection();
					sql = "select GreenHouseId,GreenHouseName from greenhouseinfotb where GreenHouseId in (select HOUSE_ID from greenhouse_user where USER_ID=?)";
					statement = getPreparedStatement(sql);
					statement.setString(1, Constant.CURRENT_USER);
					rs = statement.executeQuery();
					while (rs != null && rs.next()) {
						if (result == null)
							result = new ArrayList<GreenHouseEntity>();
						GreenHouseEntity greenHouseEntity = new GreenHouseEntity();
						greenHouseEntity.setID(rs.getString(1));
						greenHouseEntity.setName(rs.getString(2));
						result.add(greenHouseEntity);
					}
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
	 * 获取二氧化碳的数据
	 * 
	 * @param greenHouseID
	 *            大棚ID
	 * @return 二氧化碳的数据的list
	 */
	public static List<EnvironmentParameterEntity> getCarbonDioxideData(String greenHouseID) {
		List<EnvironmentParameterEntity> result = null;
		String sql = "select co2,recordtime from environmentparatb where greenhouseid=? limit 2000";
		PreparedStatement statement = getPreparedStatement(sql);
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				rs = statement.executeQuery();
				while (rs != null && rs.next()) {
					if (result == null) {
						result = new ArrayList<EnvironmentParameterEntity>();
					}
					CarbonDioxideEntity carbonDioxideEntity = new CarbonDioxideEntity(rs.getInt(1), rs.getString(2));
					result.add(carbonDioxideEntity);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				try {
					if (rs != null) {
						rs.close();
					}
					statement.close();

				} catch (SQLException e) {
					e.printStackTrace();

				}

			}
		}
		closeConnection();
		return result;
	}

	/**
	 * 获取湿度的数据
	 * 
	 * @param greenHouseID
	 *            温室ID
	 * @return 湿度实体list
	 */
	public static List<EnvironmentParameterEntity> getHumidityData(String greenHouseID) {
		List<EnvironmentParameterEntity> result = null;
		String sql = "select humidity,recordtime from environmentparatb where greenhouseid=? limit 2000";
		PreparedStatement statement = getPreparedStatement(sql);
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				rs = statement.executeQuery();
				while (rs != null && rs.next()) {
					if (result == null) {
						result = new ArrayList<EnvironmentParameterEntity>();
					}
					HumidityEntity humidityEntity = new HumidityEntity(rs.getFloat(1), rs.getString(2));
					result.add(humidityEntity);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				try {
					if (rs != null) {
						rs.close();
					}
					statement.close();

				} catch (SQLException e) {
					e.printStackTrace();

				}

			}
		}
		closeConnection();
		return result;
	}

	/**
	 * 获取光照数据
	 * 
	 * @param greenHouseID
	 *            温室ID
	 * @return 光照实体list
	 */
	public static List<EnvironmentParameterEntity> getIlluminanceData(String greenHouseID) {
		List<EnvironmentParameterEntity> result = null;
		String sql = "select illuminance,recordtime from environmentparatb where greenhouseid=? limit 2000";
		PreparedStatement statement = getPreparedStatement(sql);
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				rs = statement.executeQuery();
				while (rs != null && rs.next()) {
					if (result == null) {
						result = new ArrayList<EnvironmentParameterEntity>();
					}
					IlluminanceEntity illuminanceEntity = new IlluminanceEntity(rs.getInt(1), rs.getString(2));
					result.add(illuminanceEntity);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				try {
					if (rs != null) {
						rs.close();
					}
					statement.close();

				} catch (SQLException e) {
					e.printStackTrace();

				}

			}
		}
		closeConnection();
		return result;
	}

	/**
	 * 获取温度数据
	 * 
	 * @param greenHouseID
	 *            温室ID
	 * @return 温度实体list
	 */
	public static List<EnvironmentParameterEntity> getTemperatureData(String greenHouseID) {
		List<EnvironmentParameterEntity> result = null;
		String sql = "select temperature,recordtime from environmentparatb where greenhouseid=? limit 2000";
		PreparedStatement statement = getPreparedStatement(sql);
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				rs = statement.executeQuery();
				while (rs != null && rs.next()) {
					if (result == null) {
						result = new ArrayList<EnvironmentParameterEntity>();
					}
					TemperatureEntity temperatureEntity = new TemperatureEntity(rs.getFloat(1), rs.getString(2));
					result.add(temperatureEntity);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				try {
					if (rs != null) {
						rs.close();
					}
					statement.close();

				} catch (SQLException e) {
					e.printStackTrace();

				}

			}
		}
		closeConnection();
		return result;
	}

}
