/**
 * 
 */
package com.nfschina.aiot.constant;

import java.util.List;

import android.R.string;
import android.content.res.Resources.Theme;
import android.widget.RelativeLayout;

/**
 * 常量记录
 * @author xu
 *
 */
public class Constant {

	/**
	 *  与sharepreferences相关
	 */
	
	// 第一次登陆
	public static String IS_FIRST_IN = "IsFirstIn";
	// 记住密码
	public static String IS_REMEMBER_PWD = "IsRememberPwd";
	// 自动登录
	public static String IS_AUTO_LOGIN = "IsAutoLogin";
	// 密码
	public static String PWD = "pwd";
	// 登录名
	public static String USER_ID = "User_ID";
	// 超时时间
	public static int TIME_OUT = 6;
	
	/**
	 * 与修改密码有关
	 */
	// 修改密码请求码
	public static int CHANGE_PSWD_CODE = 1;
	// 修改密码成功
	public static int CHANGE_PSWD_SUCCESS_CODE = 2;

	/**
	 * 与注册相关
	 */
	// 注册请求码
	public static int REG_CODE = 1;
	// 注册成功
	public static int REG_SUCCESS = 2;
	// 注册失败
	public static int REG_FAILED = 3;
	// 注册返回值的键
	public static String REG_RETURN = "userid";

	/**
	 * 与连接数据库相关
	 */
	// 连接数据库失败
	public static int SERVER_CONNECT_FAILED = 0;
	// 登录名或密码不正确
	public static int SERVER_LOGIN_FAILED = 1;
	// 登录成功
	public static int SERVER_LOGIN_SUCCESS = 2;
	// 查询有误
	public static int SERVER_SQL_FAILED = 3;
	// 其他未知错误
	public static int SERVER_UNKNOWN_FAILED = 4;
	// 注册成功
	public static int SERVER_REGISTER_SUCCESS = 5;
	// 注册失败
	public static int SERVER_REGISTER_FAILED = 6;
	// 修改密码成功
	public static int SERVER_CHANGE_PASSWORD_SUCCESS = 7;
	// 修改密码失败
	public static int SERVER_CHANGE_PASSWORD_FAILED = 8;
	// 注册失败，该用户名已经存在
	public static int SERVER_REGISTER_EXIST = 10;
	
	

	/**
	 * 与Toast相关
	 */
	// 登录名或密码不正确
	public static String FILL_NAME_PASSWORD = "登录名或密码不正确！";
	// 未定义的行为
	public static String UNDEF = "未定义的行为";
	// 再按一次退出
	public static String CONFIRM_EXIT = "再按一次退出应用";
	// 列表结尾
	public static String END_OF_LIST = "列表结尾";
	// 密码不能为空
	public static String FILL_PASSWORD = "填写有误，不允许密码为空";
	// 两次密码不相同
	public static String DIFF_PASSWORD = "两次输入的新密码不相同";
	// 新密码和旧密码不能相同
	public static String SAME_PASSWORD = "新密码和旧密码不能相同";
	// 登录名或密码错误
	public static String LOGIN_FAILED_INFO = "登录名或密码错误";
	// 更改密码失败
	public static String CHANGE_PASSWORD_FAILED = "更改密码失败";
	// 更改密码成功
	public static String CHANGE_PASSWORD_SUCCESS = "更改密码成功";
	// 注册失败
	public static String REGISTER_FAILED_INFO = "登录名或密码不符合要求";
	// 注册失败，用户名已经存在
	public static String REGISTER_FAILED_EXIST = "该登录名已经被注册！请尝试其他登录名";
	// 注册成功
	public static String REGISTER_SUCCESS = "注册成功";
	// 连接服务器失败
	public static String CONNECT_FAILED_INFO = "与服务器连接失败";
	// 查询出错
	public static String SQL_FAILED_INFO = "查询语句出错";

	/**
	 * 与数量有关
	 */
	// 主页面button数量
	public static int HOME_BTN_COUNT = 4;
	// 主页面图片数量
	public static int HOME_PICS = 3;

	// 表明装填
	public static int TEMPERATURE = 0;
	public static int CARBONDIOXIDE = 1;
	public static int HUMIDITY = 2;
	public static int SUNSHINE = 3;

	// fragment标签
	public static String NEWS_CONTENT = "news_content";
	public static String NEWS_LIST = "news_list";

	// 当前登录名
	public static String CURRENT_USER = null;
	// 当前密码
	public static String CURRENT_PASSWORD = null;

	// 临时数据
	public static List<Integer> GreenHouseName = null;
	// 测试数据
	public static String[] TestListItem = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance",
			"Ackawi", "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };

}
