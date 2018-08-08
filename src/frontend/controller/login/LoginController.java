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
	//1. ������������fxml�еĿؼ�
	public static int id;
	
	//2. ���췽����
	public LoginController() {}
	
	//3. ������
	/**
	 * �ؼ���
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
	
	//����1����ʼ��
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adjustUI();
	}
	
	//����2������UI�ؼ�
	public void adjustUI(){
		choiceBox_usertype.setItems(FXCollections.observableArrayList("ѧ��","��ʦ","����Ա"));
		choiceBox_usertype.getSelectionModel().select(0);
	}
	
	//����3��������ťsignIn
	public void SignIn() throws Exception {
		SignInView siv = new SignInView();
		siv.show();
		
	}
	
	//����4: ������ťlogIn
	public void Login(ActionEvent event) throws Exception {
		//1. ��ȡ�û�������û���������
		String name = username.getText();
		String pwd = password.getText();
		String usertype = choiceBox_usertype.getSelectionModel().getSelectedItem();
		
		
		
		
		//2. �ж��û��Ƿ��ǹ���Ա
		if(usertype.equals("����Ա")) {
			//����Ա�˺�������֤
			if(name.equals("dba")&&pwd.equals("123456")){
				new DbaView().show();
				((Node) (event.getSource())).getScene().getWindow().hide();
			}else{
				warning.setText("�û��������������������");
			}				
		}else {
			//3. ����û����ǹ���Ա�����������û�����������кϷ����ж�
			id = LoginService.LoginValidation(name, pwd, usertype,warning);
			//�˺źϷ����������˻����ͽ�����Ӧ����
			if(id>0) {
				if(usertype.equals("ѧ��")){
					new StudentView().show();
				}else if(usertype.equals("��ʦ")) {
					new TeacherView().show();
				}
				((Node) (event.getSource())).getScene().getWindow().hide();
			}else if(id==-2) {//�˺Ų��Ϸ�
				warning.setText("�û��������ڣ�����ע��");
			}else {
				warning.setText("�û��������������������");
			}
		}	
	}
	
	//����5������ѡ���choiceBox_usertype
	public void UserType() {}
		
	
}
