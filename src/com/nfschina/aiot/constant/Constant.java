/**
 * 
 */
package com.nfschina.aiot.constant;

import java.util.List;

import android.content.res.Resources.Theme;
import android.widget.RelativeLayout;

/**
 * ������¼
 * @author xu
 *
 */
public class Constant {

	/**
	 *  ��sharepreferences���
	 */
	
	// ��һ�ε�½
	public static String IS_FIRST_IN = "IsFirstIn";
	// ��ס����
	public static String IS_REMEMBER_PWD = "IsRememberPwd";
	// �Զ���¼
	public static String IS_AUTO_LOGIN = "IsAutoLogin";
	// ����
	public static String PWD = "pwd";
	// �û���
	public static String USER_NAME = "User_Name";
	// ��ʱʱ��
	public static int TIME_OUT = 6;

	/**
	 * ��ע�����
	 */
	// ע��������
	public static int REG_CODE = 1;
	// ע��ɹ�
	public static int REG_SUCCESS = 2;
	// ע��ʧ��
	public static int REG_FAILED = 3;

	/**
	 * ���������ݿ����
	 */
	// �������ݿ�ʧ��
	public static int SERVER_CONNECT_FAILED = 0;
	// �û��������벻��ȷ
	public static int SERVER_LOGIN_FAILED = 1;
	// ��¼�ɹ�
	public static int SERVER_LOGIN_SUCCESS = 2;
	// ��ѯ����
	public static int SERVER_SQL_FAILED = 3;
	// ����δ֪����
	public static int SERVER_UNKNOWN_FAILED = 4;
	// ע��ɹ�
	public static int SERVER_REGISTER_SUCCESS = 5;
	// ע��ʧ��
	public static int SERVER_REGISTER_FAILED = 6;
	// �޸�����ɹ�
	public static int SERVER_CHANGE_PASSWORD_SUCCESS = 7;
	// �޸�����ʧ��
	public static int SERVER_CHANGE_PASSWORD_FAILED = 8;
	

	/**
	 * ��Toast���
	 */
	// �û��������벻��ȷ
	public static String FILL_NAME_PASSWORD = "�û��������벻��ȷ��";
	// δ�������Ϊ
	public static String UNDEF = "δ�������Ϊ";
	// �ٰ�һ���˳�
	public static String CONFIRM_EXIT = "�ٰ�һ���˳�Ӧ��";
	// �б��β
	public static String END_OF_LIST = "�б��β";
	// ���벻��Ϊ��
	public static String FILL_PASSWORD = "��д���󣬲���������Ϊ��";
	// �������벻��ͬ
	public static String DIFF_PASSWORD = "������������벻��ͬ";
	// �û������������
	public static String LOGIN_FAILED_INFO = "�û������������";
	// ��������ʧ��
	public static String CHANGE_PASSWORD_FAILED = "��������ʧ��";
	// ��������ɹ�
	public static String CHANGE_PASSWORD_SUCCESS = "��������ɹ�";
	// ע��ʧ��
	public static String REGISTER_FAILED_INFO = "�û��������벻����Ҫ��";
	// ע��ɹ�
	public static String REGISTER_SUCCESS = "ע��ɹ�";
	// ���ӷ�����ʧ��
	public static String CONNECT_FAILED_INFO = "�����������ʧ��";
	// ��ѯ����
	public static String SQL_FAILED_INFO = "��ѯ������";

	/**
	 * �������й�
	 */
	// ��ҳ��button����
	public static int HOME_BTN_COUNT = 4;
	// ��ҳ��ͼƬ����
	public static int HOME_PICS = 3;

	// ����װ��
	public static int TEMPERATURE = 0;
	public static int CARBONDIOXIDE = 1;
	public static int HUMIDITY = 2;
	public static int SUNSHINE = 3;

	// fragment��ǩ
	public static String NEWS_CONTENT = "news_content";
	public static String NEWS_LIST = "news_list";

	// ��ǰ�û���
	public static String CURRENT_USER = null;
	// ��ǰ����
	public static String CURRENT_PASSWORD = null;

	// ��ʱ����
	public static List<Integer> GreenHouseName = null;
	// ��������
	public static String[] TestListItem = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance",
			"Ackawi", "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };

}
