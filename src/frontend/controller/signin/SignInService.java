package frontend.controller.signin;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import backend.entity.Account;
import backend.entity.Student;
import backend.entity.Teacher;
import frontend.controller.student.StudentService;
import frontend.controller.teacher.TeacherService;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;



/**
 * SignInController�����ࣺȫ����̬����
 * Ŀ�ģ�����SignInController���г�UI֮��Ĺ���ʵ��
 */
public class SignInService {
	
	//1. ����1����ȡ�˺���Ϣ ��֤�˺��Ƿ��Ѵ���
	public static boolean isAccountExsit(String username) {
		HashMap<Integer,Account> accMap = new HashMap<>();
		ObjectInputStream ois = null;
		/**
		 * ��¼ע���˺��Ƿ����ע��ɹ���
		 * ---true�����˺Ų�����,ע��ɹ�
		 * ---false�����˺Ŵ��ڣ�ע��ʧ��
		 */
		boolean succeed = true; 
		
		try {
			//1. �����˺����ݿ�Account.txt����Ϣ
			File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt");
			if(!f.exists()) {
				f.createNewFile();
			}
			//2. ���Account.txt���ڣ���ע���˺���������˺Ž���һһ�ȶ�
			if(f.length() > 0){
				ois = new ObjectInputStream(new FileInputStream(f));
				accMap = (HashMap<Integer,Account>)ois.readObject();
				//��ȡ��Account����
				ArrayList<Account> accList = new ArrayList<>(accMap.values());
				if(accList.size() != 0){
					for (Account account : accList) {
						if(username.equals(account.getuserName())){
							succeed = false;
							System.out.println("�û��Ѵ��ڣ�"+account);
							break;
						}
					}
				}
			}
			//3. ���Account.txt�ļ�������---�˺ſ���ע��
			
		} catch(EOFException e) {
			System.out.println("��ǰ�б�Ϊ��");
		} catch (FileNotFoundException e1) {
			System.out.println("�Ҳ����˺��ļ�");
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("ע���ȡ�ļ�����");
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("��ȡ�������쳣");
			e.printStackTrace();
		}finally{
			try {
				if(ois != null){
					ois.close();
				}
			} catch (IOException e) {
				System.out.println("�رն�������������");
				e.printStackTrace();
			}
		}
		return succeed;
	}
	
	
	//����2��ʹ��IO�����û���Ϣд�����ݿ���
	public static void SaveUser(Student stu,Account user,Label hint) {
		//1. �������
		HashMap<Integer,Account> accMap = new HashMap<>();
		HashMap<Integer, Student> stuMap = new HashMap<>();
		HashMap<Integer,Teacher> teaMap = new HashMap<>();
		ObjectOutputStream stuOOS = null;
		ObjectOutputStream accOOS = null;
		
		
		//2. ��Student��Accountд��.txt
		try {
			File stuf = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Students.txt");
			File userf = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Account.txt");
			//2.1 �ж�Students.txt�Ƿ����
			if(!stuf.exists()) {
				stuf.createNewFile();
			}
			if(!userf.exists()) {
				userf.createNewFile();
			}
			/**2.2 ���Students.txt����������������
				---��Students.txt������˺���Ϣȫ�����
			*/
			if(stuf.length() > 0){
				stuMap = StudentService.loadStudent();
				accMap = StudentService.loadAccount();
				teaMap = TeacherService.loadTeacher();
				//�������Student��Teacher��Account��Ϣ
				for(Map.Entry<Integer, Account> entry : accMap.entrySet()) {
					System.out.println("Account:"+entry.getValue());
				}
				for (Map.Entry<Integer, Student> entry : stuMap.entrySet()) {
					System.out.println("Student:"+entry.getValue());
				}			
				for(Map.Entry<Integer, Teacher> entry : teaMap.entrySet()) {
					System.out.println("Teacher:"+entry.getValue());
				}
			}
			
			//3. �½��˺ţ���Students.txt���һ��Student������Account.txt���һ��Account����
			if(user.getuserName().length()>0 && user.getPassword().length()>=6){
				stuOOS = new ObjectOutputStream(new FileOutputStream("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Students.txt"));
				accOOS = new ObjectOutputStream(new FileOutputStream("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Account.txt"));
				stuMap.put(stu.getId(),stu);
				accMap.put(user.getId(),user);
				stuOOS.writeObject(stuMap);
				accOOS.writeObject(accMap);
				
				//������ʾ�Ի���
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("ע��ɹ�");
				alert.showAndWait();
				System.out.println("��ǰStudent:"+stu);
				System.out.println("��ǰAccount:"+user);
				System.out.println("ע��ɹ�");
				
			}else{
				if(user.getuserName().length()==0){
					hint.setVisible(true);
					hint.setText("�˺ų�������ڵ���1λ");
				}else{
					hint.setVisible(true);
					hint.setText("���볤������ڵ���6λ");
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("ע��ʱ�û���Ϣ�Ҳ����ļ�");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ע��ʱ�û�����쳣");
			e.printStackTrace();
		} finally{
			try {
				if(accOOS != null){
					accOOS.close();
				}
			} catch (IOException e) {
				System.out.println("ע���û��ر��쳣");
				e.printStackTrace();
			}
			try {
				if(stuOOS != null){
					stuOOS.close();
				}
			} catch (IOException e) {
				System.out.println("ע�������Ϣ�ر��쳣");
				e.printStackTrace();
			}
		}
	}
	
}
