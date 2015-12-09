/**
 * 
 */
package com.nfschina.aiot.constant;

/**
 * ������¼
 * 
 * @author xu
 *
 */
public class Constant {

	// ����汾
	public static String VERSION = "0.1";

	/**
	 * ��sharepreferences���
	 */

	// ��һ�ε�½
	public static String IS_FIRST_IN = "IsFirstIn" + VERSION;
	// ��ס����
	public static String IS_REMEMBER_PWD = "IsRememberPwd";
	// �Զ���¼
	public static String IS_AUTO_LOGIN = "IsAutoLogin";
	// ����
	public static String PWD = "pwd";
	// ��¼��
	public static String USER_ID = "User_ID";
	// ��ʱʱ��
	public static int TIME_OUT = 6;
	// ���Ź���
	public static String FACTORY_ID = "Factory_id";

	/**
	 * ���޸������й�
	 */
	// �޸�����������
	public static int CHANGE_PSWD_CODE = 1;
	// �޸�����ɹ�
	public static int CHANGE_PSWD_SUCCESS_CODE = 2;

	/**
	 * ��ע�����
	 */
	// ע��������
	public static int REG_CODE = 1;
	// ע��ɹ�
	public static int REG_SUCCESS = 2;
	// ע��ʧ��
	public static int REG_FAILED = 3;
	// ע�᷵��ֵ�ļ�
	public static String REG_RETURN = "userid";

	/**
	 * ���������ݿ����
	 */
	// �������ݿ�ʧ��
	public static int SERVER_CONNECT_FAILED = 0;
	// ��¼�������벻��ȷ
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
	// ��¼�������벻��ȷ
	public static String FILL_NAME_PASSWORD = "��¼�������벻��ȷ��";
	// δ�������Ϊ
	public static String UNDEF = "δ�������Ϊ";
	// �ٰ�һ���˳�
	public static String CONFIRM_EXIT = "�ٰ�һ���˳�Ӧ��";
	// �б��β
	public static String END_OF_LIST = "�Ѽ���ȫ��";
	// ���벻��Ϊ��
	public static String FILL_PASSWORD = "��д���󣬲���������Ϊ��";
	// �������벻��ͬ
	public static String DIFF_PASSWORD = "��������������벻��ͬ";
	// ������;����벻����ͬ
	public static String SAME_PASSWORD = "������;����벻����ͬ";
	// ��¼�����������
	public static String LOGIN_FAILED_INFO = "��¼�����������";
	// ��������ʧ��
	public static String CHANGE_PASSWORD_FAILED = "�޸�����ʧ��";
	// ��������ɹ�
	public static String CHANGE_PASSWORD_SUCCESS = "��������ɹ�";
	// ע��ʧ��
	public static String REGISTER_FAILED_INFO = "ע��ʧ��";
	// ע��ʧ�ܣ��û����Ѿ�����
	public static String REGISTER_FAILED_EXIST = "�õ�¼���Ѿ���ע�ᣡ�볢��������¼��";
	// ע��ɹ�
	public static String REGISTER_SUCCESS = "ע��ɹ�";
	// ���ӷ�����ʧ��
	public static String CONNECT_FAILED_INFO = "�����������ʧ��";
	// ��ѯ����
	public static String SQL_FAILED_INFO = "��ѯ������";
	// ��������
	public static String NET_STATE_DISABLE = "û�п��õ����磬������������";

	/**
	 * �������й�
	 */
	// ��ҳ��button����
	public static int HOME_BTN_COUNT = 4;
	// ��ҳ��ͼƬ����
	public static int HOME_PICS = 3;

	// ����װ��
	public static int TEMPERATURE = 3;
	public static int CARBONDIOXIDE = 0;
	public static int HUMIDITY = 2;
	public static int ILLUMINANCE = 1;

	// fragment��ǩ
	public static String NEWS_CONTENT = "news_content";
	public static String NEWS_LIST = "news_list";

	// ��ǰ��¼��
	public static String CURRENT_USER = null;
	// ��ǰ����
	public static String CURRENT_PASSWORD = null;

	// ��intent���ݴ����й�
	public static String INTENT_EXTRA_HISTORY_HOUSE_ID = "GreenHouseID";

}
