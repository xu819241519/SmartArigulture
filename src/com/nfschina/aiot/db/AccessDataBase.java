package com.nfschina.aiot.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nfschina.aiot.constant.Constant;
import com.nfschina.aiot.entity.AlarmEntity;
import com.nfschina.aiot.entity.CarbonDioxideEntity;
import com.nfschina.aiot.entity.EnvironmentParameterEntity;
import com.nfschina.aiot.entity.GreenHouseEntity;
import com.nfschina.aiot.entity.HumidityEntity;
import com.nfschina.aiot.entity.IlluminanceEntity;
import com.nfschina.aiot.entity.InstructionEntity;
import com.nfschina.aiot.entity.TemperatureEntity;

/**
 * 连接数据库
 * 
 * @author xu
 *
 */

public class AccessDataBase {

	// 连接字符串
	private static String mUrl = "jdbc:mysql://10.50.200.120:3306/zkghdb?useUnicode=true&amp;characterEncoding=UTF-8";

	// 连接用户名
	private static String mName = "root";
	// 连接密码
	private static String mPassword = "root";

	// resultcode
	private static int mResultCode = Constant.SERVER_CONNECT_FAILED;
	// 连接connection
	private static Connection mConn = null;

	private static java.sql.CallableStatement getCallableStatement(String commStr) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.setLoginTimeout(Constant.TIME_OUT);
			mConn = DriverManager.getConnection(mUrl, mName, mPassword);
			return mConn.prepareCall(commStr);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
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

		CallableStatement stmt = getCallableStatement("call checkLogin(?,?)");
		if (stmt != null) {
			stmt.setString(1, name);
			stmt.setString(2, pswd);
			ResultSet rs = null;
			try {
				if (stmt.execute()) {
					rs = stmt.getResultSet();
					if (rs.next()) {
						System.out.println(rs.getString(1));
						if (rs.getString(1).equals(name)) {
							mResultCode = Constant.SERVER_LOGIN_SUCCESS;
						} else {
							mResultCode = Constant.SERVER_LOGIN_FAILED;
							Constant.LOGIN_FAILED_INFO = rs.getString(2);
						}
					} else {
						mResultCode = Constant.SERVER_LOGIN_FAILED;
					}
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
	public static int connectRegister(String id, String pswd, String username) {
		mResultCode = Constant.SERVER_CONNECT_FAILED;
		CallableStatement stmt = getCallableStatement("call register(?,?,?)");
		if (stmt != null) {
			try {
				stmt.setString(1, id);
				stmt.setString(2, pswd);
				stmt.setString(3, username);
				ResultSet rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					if (rs.getInt(1) != 0) {
						mResultCode = Constant.SERVER_REGISTER_SUCCESS;

					} else {
						mResultCode = Constant.SERVER_REGISTER_FAILED;
					}
					Constant.REGISTER_FAILED_INFO = rs.getString(2);
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
		// String sql = "select * from sys_user where USER_ID= ? and
		// USER_PASSWORD= ?";
		CallableStatement stmt = getCallableStatement("call changepwd(?,?,?)");
		if (stmt != null) {
			ResultSet rs = null;
			try {
				stmt.setString(1, id);
				stmt.setString(2, oldPassword);
				stmt.setString(3, newPassword);
				rs = stmt.executeQuery();
				if (rs != null && rs.next()) {
					if (rs.getString(1).equals("修改密码成功")) {

						mResultCode = Constant.SERVER_CHANGE_PASSWORD_SUCCESS;
						Constant.CURRENT_PASSWORD = newPassword;
					} else {
						mResultCode = Constant.SERVER_CHANGE_PASSWORD_FAILED;
						Constant.CHANGE_PASSWORD_FAILED = rs.getString(1);
					}
				} else {
					Constant.CHANGE_PASSWORD_FAILED = "修改密码失败";
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
		CallableStatement statement = getCallableStatement("call getAlarmHistory(?,?,?)");
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, GreenHouseID);
				statement.setInt(2, page * size);
				statement.setInt(3, size);
				if (statement.execute()) {
					rs = statement.getResultSet();
					while (rs != null && rs.next()) {
						AlarmEntity alarmEntity = new AlarmEntity(rs.getInt(1), rs.getString(2), rs.getString(3),
								rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
								rs.getString(9));
						if (result == null)
							result = new ArrayList<AlarmEntity>();
						result.add(alarmEntity);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
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
	 * @param page
	 *            获取第几页的数据
	 * @param size
	 *            每一页有多少条数据
	 * @return 指令记录实体的list
	 */
	public static List<InstructionEntity> getInstructionHistoryData(int page, int size, String greenHouseID) {
		List<InstructionEntity> result = null;
		java.sql.CallableStatement statement = getCallableStatement("call getInstructionsHistory(?,?,?)");
		ResultSet rs = null;
		if (statement != null) {
			try {
				statement.setString(1, greenHouseID);
				statement.setInt(2, page * size);
				statement.setInt(3, size);
				if (statement.execute()) {
					rs = statement.getResultSet();
					while (rs.next()) {
						InstructionEntity instructionEntity = new InstructionEntity(rs.getInt(1), rs.getString(2),
								rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
						if (result == null)
							result = new ArrayList<InstructionEntity>();
						result.add(instructionEntity);
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null)
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
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
	public static List<GreenHouseEntity> getGreenHouseName(int start, int size) {

		List<GreenHouseEntity> result = null;

		CallableStatement statement = getCallableStatement("call getGreenHouseHistoryName(?,?,?)");
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, Constant.CURRENT_USER);
				statement.setInt(2, start);
				statement.setInt(3, size);
				if (statement.execute()) {
					rs = statement.getResultSet();
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
					if (rs != null)
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

		CallableStatement statement = getCallableStatement("call getGreenHouseData(?,?)");
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				statement.setString(2, "c");
				if (statement.execute()) {
					rs = statement.getResultSet();
					while (rs != null && rs.next()) {
						if (result == null) {
							result = new ArrayList<EnvironmentParameterEntity>();
						}
						CarbonDioxideEntity carbonDioxideEntity = new CarbonDioxideEntity(rs.getInt(1),
								rs.getString(2));
						result.add(carbonDioxideEntity);
					}
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
		CallableStatement statement = getCallableStatement("call getGreenHouseData(?,?)");
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				statement.setString(2, "h");
				if (statement.execute()) {
					rs = statement.getResultSet();
					while (rs != null && rs.next()) {
						if (result == null) {
							result = new ArrayList<EnvironmentParameterEntity>();
						}
						HumidityEntity humidityEntity = new HumidityEntity(rs.getFloat(1), rs.getString(2));
						result.add(humidityEntity);
					}
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
		CallableStatement statement = getCallableStatement("call getGreenHouseData(?,?)");
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				statement.setString(2, "i");
				if (statement.execute()) {
					rs = statement.getResultSet();
					while (rs != null && rs.next()) {
						if (result == null) {
							result = new ArrayList<EnvironmentParameterEntity>();
						}
						IlluminanceEntity illuminanceEntity = new IlluminanceEntity(rs.getInt(1), rs.getString(2));
						result.add(illuminanceEntity);
					}
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
		CallableStatement statement = getCallableStatement("call getGreenHouseData(?,?)");
		if (statement != null) {
			ResultSet rs = null;
			try {
				statement.setString(1, greenHouseID);
				statement.setString(2, "t");
				if (statement.execute()) {
					rs = statement.getResultSet();
					while (rs != null && rs.next()) {
						if (result == null) {
							result = new ArrayList<EnvironmentParameterEntity>();
						}
						TemperatureEntity temperatureEntity = new TemperatureEntity(rs.getFloat(1), rs.getString(2));
						result.add(temperatureEntity);
					}
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
