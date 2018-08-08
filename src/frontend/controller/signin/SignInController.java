package frontend.controller.signin;

import java.net.URL;
import java.util.ResourceBundle;

import backend.entity.Account;
import backend.entity.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController implements Initializable{
	//1. 属性区
	
	 
	/**
	 * 控件区
	 */
	@FXML
	private TextField name = new TextField(" ");
	@FXML
	private TextField id= new TextField(" ");
	@FXML
	private TextField phone= new TextField(" ");
	@FXML
	private TextField homeAddress= new TextField(" ");
	@FXML
	private TextField contactsName= new TextField(" ");
	@FXML
	private TextField gender= new TextField(" ");
	@FXML
	private TextField contactsPhone= new TextField(" ");
	@FXML
	private TextField classes= new TextField(" ");
	@FXML
	private Button button_submit;
	@FXML
	private Button button_back;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Label hint;
	@FXML
	private Label warning;
	
	//2. 构造方法
	public SignInController() {}
		
	//3. 方法区
	//方法1：初始化
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	//方法2：调整UI控件
	public void adjustUI(){
		
	}
	
	//方法3：监听提交按钮
	public void SubmitInfo() {
		//1. 调整UI
		warning.setVisible(false);
		hint.setVisible(false);
		
		//2. 从用户端录入信息，并且创建对象
		try {
			Student stu = new Student(name.getText(),gender.getText(),Integer.valueOf(classes.getText().trim())
					,Integer.valueOf(id.getText().trim()),phone.getText(),
					homeAddress.getText(),contactsName.getText(),contactsPhone.getText());
			Account user = new Account(username.getText(),password.getText()
					,Integer.valueOf(id.getText().trim()));

			//2. 调用SignInService中的方法，判断是否注册成功
			if(SignInService.isAccountExsit(username.getText())) {
				//3. 判断用户名是否为空 
				if(!username.getText().equals("")) {
					SignInService.SaveUser(stu,user,hint);
				}else {
					hint.setVisible(true);
					hint.setText("用户名为空，请重新输入");
				}
			}else {
				hint.setVisible(true);
				hint.setText("账号已存在，请重新输入");
			}
		}catch(NumberFormatException e) {
			warning.setVisible(true);
			warning.setText("输入信息的格式有误，请重新输入");
		}
		
		
		
	}
	
	//方法4：监听返回按钮
	public void Back(ActionEvent event) throws Exception {
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

}
