/**
 * 
 */
package com.nfschina.aiot.constant;

/**
 * @author xu
 *
 */
public class Constant {

	// relate to shareperferences
	// �Ƿ��һ�ν���
	public static String IS_FIRST_IN = "IsFirstIn";
	// �Ƿ��ס����
	public static String IS_REMEMBER_PWD = "IsRememberPwd";
	// �Ƿ��Զ���¼
	public static String IS_AUTO_LOGIN = "IsAutoLogin";
	// ����
	public static String PWD = "pwd";
	// �û���
	public static String USER_NAME = "User_Name";

	// relate to register
	// ע��������
	public static int REG_CODE = 1;
	// ע��ɹ�
	public static int REG_SUCCESS = 2;
	// ע��ʧ��
	public static int REG_FAILED = 3;

	// relate to toast
	// �û�����������ʾ
	public static String FILL_NAME_PASSWORD = "�û��������벻��ȷ��";
	// undefine case
	public static String UNDEF = "δ�������Ϊ";
	// confirm exit
	public static String CONFIRM_EXIT = "�ٰ�һ���˳�Ӧ��";

	// relate to counts
	//the count of home buttons
	public static int HOME_BTN_COUNT = 4;
	//the count of home pics
	private static int HOME_PICS = 3;
	
	public static int getHomePicCount() {
		return HOME_PICS;
	}
	public static void setHomePicCount(int hOME_PICS) {
		HOME_PICS = hOME_PICS;
	}
	
	
	
	//temp data
	public static String [] GreenHouseName = new String []{"���Ҵ���1","���Ҵ���2","���Ҵ���3","���Ҵ���4","���Ҵ���5"};
	
	

}
