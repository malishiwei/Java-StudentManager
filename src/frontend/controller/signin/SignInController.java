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
	//1. ������
	
	 
	/**
	 * �ؼ���
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
	
	//2. ���췽��
	public SignInController() {}
		
	//3. ������
	//����1����ʼ��
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	//����2������UI�ؼ�
	public void adjustUI(){
		
	}
	
	//����3�������ύ��ť
	public void SubmitInfo() {
		//1. ����UI
		warning.setVisible(false);
		hint.setVisible(false);
		
		//2. ���û���¼����Ϣ�����Ҵ�������
		try {
			Student stu = new Student(name.getText(),gender.getText(),Integer.valueOf(classes.getText().trim())
					,Integer.valueOf(id.getText().trim()),phone.getText(),
					homeAddress.getText(),contactsName.getText(),contactsPhone.getText());
			Account user = new Account(username.getText(),password.getText()
					,Integer.valueOf(id.getText().trim()));

			//2. ����SignInService�еķ������ж��Ƿ�ע��ɹ�
			if(SignInService.isAccountExsit(username.getText())) {
				//3. �ж��û����Ƿ�Ϊ�� 
				if(!username.getText().equals("")) {
					SignInService.SaveUser(stu,user,hint);
				}else {
					hint.setVisible(true);
					hint.setText("�û���Ϊ�գ�����������");
				}
			}else {
				hint.setVisible(true);
				hint.setText("�˺��Ѵ��ڣ�����������");
			}
		}catch(NumberFormatException e) {
			warning.setVisible(true);
			warning.setText("������Ϣ�ĸ�ʽ��������������");
		}
		
		
		
	}
	
	//����4���������ذ�ť
	public void Back(ActionEvent event) throws Exception {
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

}
