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
 * LoginController工具类：全部静态方法
 * 目的：帮助LoginController进行除UI之外的功能实现
 */

public class LoginService {
	
	//方法1. 判断用户名的合法性
	public static int LoginValidation(String username, String password,String usertype, Label hint) 
			throws FileNotFoundException, IOException, ClassNotFoundException {
		//1. 导入数据库信息
		/**
		 * 返回值id: -2表示用户名不存在，-1表示用户名存在，密码错误，>0表示账号密码匹配
		 */
		int id = -2; 
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Account.txt");
		//判断文件是否存在
		if(!f.exists()) {
			f.createNewFile();
		}
		//2. 如果文件存在，读取文件中的Account信息，存入ArrayList<Account>
		Map<Integer,Account> map = new HashMap<Integer,Account>();
		if(f.length() > 0){
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			map = StudentService.loadAccount();
			//如果Account列表不为空
			if(!map.isEmpty()) {
				//3. 获取所有Account的集合
				ArrayList<Account> accList = new ArrayList<Account>(map.values());
				for (Account account : accList) {
					if(account.getuserName().equals(username)){
						id = -1; //用户名存在
						if(account.getPassword().equals(password)
								&& account.getUsertype().equals(usertype)) {
							id = account.getId(); //密码匹配且用户类型匹配
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
