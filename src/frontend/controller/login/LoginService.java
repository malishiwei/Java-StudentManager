package frontend.controller.login;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import backend.entity.Account;
import frontend.controller.student.StudentService;
import javafx.scene.control.Label;



/**
 * LoginController�����ࣺȫ����̬����
 * Ŀ�ģ�����LoginController���г�UI֮��Ĺ���ʵ��
 */

public class LoginService {
	
	//����1. �ж��û����ĺϷ���
	public static int LoginValidation(String username, String password,String usertype, Label hint) 
			throws FileNotFoundException, IOException, ClassNotFoundException {
		//1. �������ݿ���Ϣ
		/**
		 * ����ֵid: -2��ʾ�û��������ڣ�-1��ʾ�û������ڣ��������>0��ʾ�˺�����ƥ��
		 */
		int id = -2; 
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Account.txt");
		//�ж��ļ��Ƿ����
		if(!f.exists()) {
			f.createNewFile();
		}
		//2. ����ļ����ڣ���ȡ�ļ��е�Account��Ϣ������ArrayList<Account>
		Map<Integer,Account> map = new HashMap<Integer,Account>();
		if(f.length() > 0){
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			map = StudentService.loadAccount();
			//���Account�б�Ϊ��
			if(!map.isEmpty()) {
				//3. ��ȡ����Account�ļ���
				ArrayList<Account> accList = new ArrayList<Account>(map.values());
				for (Account account : accList) {
					if(account.getuserName().equals(username)){
						id = -1; //�û�������
						if(account.getPassword().equals(password)
								&& account.getUsertype().equals(usertype)) {
							id = account.getId(); //����ƥ�����û�����ƥ��
						}
					}
				}
			}
			if(ois != null){
				ois.close();
			}	
		}
		
		return id;	
	}
}
