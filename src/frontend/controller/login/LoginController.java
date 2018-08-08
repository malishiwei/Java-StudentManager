package frontend.controller.login;

import java.net.URL;
import java.util.ResourceBundle;


import frontend.view.DbaView;
import frontend.view.SignInView;
import frontend.view.StudentView;
import frontend.view.TeacherView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController implements Initializable{
	//1. 属性区：声明fxml中的控件
	public static int id;
	
	//2. 构造方法区
	public LoginController() {}
	
	//3. 方法区
	/**
	 * 控件们
	 */
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button logIn;
	@FXML
	private Button signIn;
	@FXML
	private ChoiceBox<String> choiceBox_usertype;
	@FXML
	private Label warning;
	
	//方法1：初始化
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adjustUI();
	}
	
	//方法2：调整UI控件
	public void adjustUI(){
		choiceBox_usertype.setItems(FXCollections.observableArrayList("学生","教师","管理员"));
		choiceBox_usertype.getSelectionModel().select(0);
	}
	
	//方法3：监听按钮signIn
	public void SignIn() throws Exception {
		SignInView siv = new SignInView();
		siv.show();
		
	}
	
	//方法4: 监听按钮logIn
	public void Login(ActionEvent event) throws Exception {
		//1. 获取用户输入的用户名和密码
		String name = username.getText();
		String pwd = password.getText();
		String usertype = choiceBox_usertype.getSelectionModel().getSelectedItem();
		
		
		
		
		//2. 判断用户是否是管理员
		if(usertype.equals("管理员")) {
			//管理员账号密码验证
			if(name.equals("dba")&&pwd.equals("123456")){
				new DbaView().show();
				((Node) (event.getSource())).getScene().getWindow().hide();
			}else{
				warning.setText("用户名密码错误，请重新输入");
			}				
		}else {
			//3. 如果用户不是管理员，则对输入的用户名和密码进行合法性判断
			id = LoginService.LoginValidation(name, pwd, usertype,warning);
			//账号合法，依照其账户类型进入相应界面
			if(id>0) {
				if(usertype.equals("学生")){
					new StudentView().show();
				}else if(usertype.equals("教师")) {
					new TeacherView().show();
				}
				((Node) (event.getSource())).getScene().getWindow().hide();
			}else if(id==-2) {//账号不合法
				warning.setText("用户名不存在，请先注册");
			}else {
				warning.setText("用户名密码错误，请重新输入");
			}
		}	
	}
	
	//方法5：监听选择框choiceBox_usertype
	public void UserType() {}
		
	
}
