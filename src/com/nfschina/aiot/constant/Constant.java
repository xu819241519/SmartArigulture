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
	public static String FILL_NAME_PASSWORD = "�û��������벻��ȷ��";
	// undefine case
	public static String UNDEF = "δ�������Ϊ";
	// confirm exit
	public static String CONFIRM_EXIT = "�ٰ�һ���˳�Ӧ��";
	// end of the list
	public static String END_OF_LIST = "�б��β";
	// tips of password
	public static String FILL_PASSWORD = "��д���󣬲���������Ϊ��";
	// tips of register password
	public static String DIFF_PASSWORD = "������������벻��ͬ";
	// failed to login
	public static String LOGIN_FAILED_INFO = "�û������������";
	// change password failed
	public static String CHANGE_PASSWORD_FAILED = "��������ʧ��";
	// change password success 
	public static String CHANGE_PASSWORD_SUCCESS = "��������ɹ�";
	// failed to register
	public static String REGISTER_FAILED_INFO = "�û��������벻����Ҫ��";
	// register success 
	public static String REGISTER_SUCCESS = "ע��ɹ�";
	// failed to connect server
	public static String CONNECT_FAILED_INFO = "�����������ʧ��";
	// failed to execute sql statement
	public static String SQL_FAILED_INFO = "��ѯ������";

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
	public static String[] GreenHouseName = new String[] { "���Ҵ���1", "���Ҵ���2", "���Ҵ���3", "���Ҵ���4", "���Ҵ���5", "���Ҵ���5",
			"���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5",
			"���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5",
			"���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5", "���Ҵ���5" };
	// test data
	public static String[] TestListItem = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance",
			"Ackawi", "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };

}
