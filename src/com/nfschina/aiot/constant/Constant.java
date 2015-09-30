/**
 * 
 */
package com.nfschina.aiot.constant;

import android.content.res.Resources.Theme;
import android.widget.RelativeLayout;

/**
 * @author xu
 *
 */
public class Constant {

	// relate to shareperferences
	// is the first time of login
	public static String IS_FIRST_IN = "IsFirstIn";
	// is remember the password
	public static String IS_REMEMBER_PWD = "IsRememberPwd";
	// is auto login
	public static String IS_AUTO_LOGIN = "IsAutoLogin";
	// password
	public static String PWD = "pwd";
	// user name
	public static String USER_NAME = "User_Name";

	// relate to register
	// the request code of registering
	public static int REG_CODE = 1;
	// register success
	public static int REG_SUCCESS = 2;
	// register failed
	public static int REG_FAILED = 3;

	// relate to return code of connecting server
	// connect the server failed
	public static int SERVER_CONNECT_FAILED = 0;
	// username or password failed
	public static int SERVER_LOGIN_FAILED = 1;
	// login success
	public static int SERVER_LOGIN_SUCCESS = 2;
	// sql statement failed
	public static int SERVER_SQL_FAILED = 3;
	// other failed
	public static int SERVER_UNKNOWN_FAILED = 4;
	// register success
	public static int SERVER_REGISTER_SUCCESS = 5;
	// register failed
	public static int SERVER_REGISTER_FAILED = 6;
	// change password success
	public static int SERVER_CHANGE_PASSWORD_SUCCESS = 7;
	// change password failed
	public static int SERVER_CHANGE_PASSWORD_FAILED = 8;

	// relate to toast
	// tips of username or password
	public static String FILL_NAME_PASSWORD = "用户名或密码不正确！";
	// undefine case
	public static String UNDEF = "未定义的行为";
	// confirm exit
	public static String CONFIRM_EXIT = "再按一次退出应用";
	// end of the list
	public static String END_OF_LIST = "列表结尾";
	// tips of password
	public static String FILL_PASSWORD = "填写有误，不允许密码为空";
	// tips of register password
	public static String DIFF_PASSWORD = "两次输入的密码不相同";
	// failed to login
	public static String LOGIN_FAILED_INFO = "用户名或密码错误";
	// change password failed
	public static String CHANGE_PASSWORD_FAILED = "更改密码失败";
	// change password success 
	public static String CHANGE_PASSWORD_SUCCESS = "更改密码成功";
	// failed to register
	public static String REGISTER_FAILED_INFO = "用户名或密码不符合要求";
	// register success 
	public static String REGISTER_SUCCESS = "注册成功";
	// failed to connect server
	public static String CONNECT_FAILED_INFO = "与服务器连接失败";
	// failed to execute sql statement
	public static String SQL_FAILED_INFO = "查询语句出错";

	// relate to counts
	// the count of home buttons
	public static int HOME_BTN_COUNT = 4;
	// the count of home pics
	public static int HOME_PICS = 3;

	// identify the status
	public static int TEMPERATURE = 0;
	public static int CARBONDIOXIDE = 1;
	public static int HUMIDITY = 2;
	public static int SUNSHINE = 3;

	// fragment tags
	public static String NEWS_CONTENT = "news_content";
	public static String NEWS_LIST = "news_list";
	
	
	// the current user
	public static String CURRENT_USER = null ;
	
	// temp data
	public static String[] GreenHouseName = new String[] { "温室大棚1", "温室大棚2", "温室大棚3", "温室大棚4", "温室大棚5", "温室大棚5",
			"温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5",
			"温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5",
			"温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5", "温室大棚5" };
	// test data
	public static String[] TestListItem = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance",
			"Ackawi", "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };

}
