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
 * SignInController工具类：全部静态方法
 * 目的：帮助SignInController进行除UI之外的功能实现
 */
public class SignInService {
	
	//1. 方法1：读取账号信息 验证账号是否已存在
	public static boolean isAccountExsit(String username) {
		HashMap<Integer,Account> accMap = new HashMap<>();
		ObjectInputStream ois = null;
		/**
		 * 记录注册账号是否可以注册成功：
		 * ---true代表账号不存在,注册成功
		 * ---false代表账号存在，注册失败
		 */
		boolean succeed = true; 
		
		try {
			//1. 加载账号数据库Account.txt的信息
			File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt");
			if(!f.exists()) {
				f.createNewFile();
			}
			//2. 如果Account.txt存在：将注册账号与里面的账号进行一一比对
			if(f.length() > 0){
				ois = new ObjectInputStream(new FileInputStream(f));
				accMap = (HashMap<Integer,Account>)ois.readObject();
				//提取出Account集合
				ArrayList<Account> accList = new ArrayList<>(accMap.values());
				if(accList.size() != 0){
					for (Account account : accList) {
						if(username.equals(account.getuserName())){
							succeed = false;
							System.out.println("用户已存在："+account);
							break;
						}
					}
				}
			}
			//3. 如果Account.txt文件不存在---账号可以注册
			
		} catch(EOFException e) {
			System.out.println("当前列表为空");
		} catch (FileNotFoundException e1) {
			System.out.println("找不到账号文件");
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("注册读取文件错误");
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("读取对象流异常");
			e.printStackTrace();
		}finally{
			try {
				if(ois != null){
					ois.close();
				}
			} catch (IOException e) {
				System.out.println("关闭对象输入流错误");
				e.printStackTrace();
			}
		}
		return succeed;
	}
	
	
	//方法2：使用IO流将用户信息写入数据库中
	public static void SaveUser(Student stu,Account user,Label hint) {
		//1. 定义变量
		HashMap<Integer,Account> accMap = new HashMap<>();
		HashMap<Integer, Student> stuMap = new HashMap<>();
		HashMap<Integer,Teacher> teaMap = new HashMap<>();
		ObjectOutputStream stuOOS = null;
		ObjectOutputStream accOOS = null;
		
		
		//2. 将Student和Account写入.txt
		try {
			File stuf = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Students.txt");
			File userf = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Account.txt");
			//2.1 判断Students.txt是否存在
			if(!stuf.exists()) {
				stuf.createNewFile();
			}
			if(!userf.exists()) {
				userf.createNewFile();
			}
			/**2.2 如果Students.txt存在且里面有内容
				---将Students.txt里面的账号信息全部输出
			*/
			if(stuf.length() > 0){
				stuMap = StudentService.loadStudent();
				accMap = StudentService.loadAccount();
				teaMap = TeacherService.loadTeacher();
				//输出所有Student，Teacher，Account信息
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
			
			//3. 新建账号：向Students.txt添加一个Student对象，向Account.txt添加一个Account对象
			if(user.getuserName().length()>0 && user.getPassword().length()>=6){
				stuOOS = new ObjectOutputStream(new FileOutputStream("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Students.txt"));
				accOOS = new ObjectOutputStream(new FileOutputStream("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Account.txt"));
				stuMap.put(stu.getId(),stu);
				accMap.put(user.getId(),user);
				stuOOS.writeObject(stuMap);
				accOOS.writeObject(accMap);
				
				//弹出提示对话框
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("注册成功");
				alert.showAndWait();
				System.out.println("当前Student:"+stu);
				System.out.println("当前Account:"+user);
				System.out.println("注册成功");
				
			}else{
				if(user.getuserName().length()==0){
					hint.setVisible(true);
					hint.setText("账号长度需大于等于1位");
				}else{
					hint.setVisible(true);
					hint.setText("密码长度需大于等于6位");
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("注册时用户信息找不到文件");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("注册时用户输出异常");
			e.printStackTrace();
		} finally{
			try {
				if(accOOS != null){
					accOOS.close();
				}
			} catch (IOException e) {
				System.out.println("注册用户关闭异常");
				e.printStackTrace();
			}
			try {
				if(stuOOS != null){
					stuOOS.close();
				}
			} catch (IOException e) {
				System.out.println("注册基本信息关闭异常");
				e.printStackTrace();
			}
		}
	}
	
}
