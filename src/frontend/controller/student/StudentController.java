package frontend.controller.student;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import backend.entity.Account;
import backend.entity.Student;
import backend.entry.student.HomeworkKey;
import backend.entry.student.StudentHomeworkEntry;
import backend.entry.student.StudentTestEntry;
import frontend.controller.teacher.TeacherService;
import frontend.view.LoginView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;


public class StudentController implements Initializable{
	//1. ������
	private HashMap<Integer,Account> accMap = new HashMap<>();
	private HashMap<Integer, Student> stuMap = new HashMap<>();
	private Student stu = new Student();
	private Account account = new Account();
	private HashMap<String,StudentTestEntry> scoreMap = new HashMap<>();
	//homeworkMap: key - HomeworkKey("homework","subject");
	private HashMap<HomeworkKey,StudentHomeworkEntry> homeworkMap = new HashMap<>();
	
	//2. ���췽��
	public StudentController() {}
		
	//3. ������
	//����1����ʼ��
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//1. ��ʼ������
		//Students.txt->stuList
		stuMap= StudentService.loadStudent();
		//Account.txt->accList
		accMap = StudentService.loadAccount();
		//stu -> Student
		stu = StudentService.findStudent(stuMap);
		//account -> Account
		account = StudentService.findAccount(accMap);
		//����ɼ�
		scoreMap =StudentService.loadScore(stu);
		//������ҵ
		homeworkMap = StudentService.loadHomework(stu);
		
		//2. ��ʼ��������Ϣҳ
		initializeProfile();
		
	}
	
	
	
	/**
	 * 1. HomePane��
	 */
	/**
	 * ---�ؼ���
	 */
	
	@FXML
	private Pane pane_Home;
	
	//Button��
	@FXML
	private Button button_home;
	
	//TextField��
	@FXML
	private TextField text_slogan;
	
	/**
	 * ---������
	 */
	//1.1  ����button_home
	public void EnterHome() {
		button_home.setOpacity(0.38);
	}
	public void ExitHome() {
		button_home.setOpacity(0.0);
	}
	public void HomeWindow() {
		pane_Home.setVisible(true);
		pane_Profile.setVisible(false);
		pane_Score.setVisible(false);
		pane_Feedback.setVisible(false);
	}
	
	//1.2 ����text_slogan
	public void EnterSlogan() {
		text_slogan.setDisable(false);
		text_slogan.getText();
	}
	public void ExitSlogan() {
		text_slogan.setDisable(true);
	}
	
	
	/**
	 * 2. ProfilePane��
	 */
	
	/**
	 * ---�ؼ���
	 */	
	//Label��
	@FXML
	private Label hint;
	@FXML
	private Label infoWarning;
	@FXML
	private Pane pane_Profile;
	
	//Button��
	@FXML
	private Button button_profile;
	
	//TextField��
	@FXML
	private TextField text_name;
	@FXML
	private TextField text_gender;
	@FXML
	private TextField text_classes;
	@FXML
	private TextField text_id;
	@FXML
	private TextField text_phone;
	@FXML
	private TextField text_address;
	@FXML
	private TextField text_contact_name;
	@FXML
	private TextField text_contact_phone;
	@FXML
	private PasswordField text_newPassword;
	@FXML
	private PasswordField text_oldPassword;
	
	/**
	 * ---������
	 */
	//2.1 ��ʼ��������Ϣҳ
	public void initializeProfile() {
		//1. ����ѧ��������Ϣ

		text_name.setPromptText(stu.getName());
		text_gender.setPromptText(stu.getGender());
		text_classes.setPromptText(String.valueOf(stu.getClasses()));		
		text_id.setPromptText(String.valueOf(stu.getId()));
		text_phone.setPromptText(stu.getPhone());
		text_address.setPromptText(stu.getHomeAddress());
		text_contact_name.setPromptText(stu.getContactsName());		
		text_contact_phone.setPromptText(stu.getContactsPhone());
		
		//2. �����ı����ʼ״̬Ϊ��ɫ
		text_name.setDisable(true);
		text_gender.setDisable(true);
		text_classes.setDisable(true);
		text_id.setDisable(true);
		text_phone.setDisable(true);
		text_address.setDisable(true);
		text_contact_name.setDisable(true);
		text_contact_phone.setDisable(true);
		
		
	}
	
	
	//2.2 ������ťbutton_revise_profile---�޸��û���Ϣ
	public void ReviseProfile() {
		infoWarning.setVisible(false);
		//�����޸�id��
		text_name.setDisable(false);
		text_gender.setDisable(false);
		text_classes.setDisable(false);
		text_phone.setDisable(false);
		text_address.setDisable(false);
		text_contact_name.setDisable(false);
		text_contact_phone.setDisable(false);
		
	}
	
	
	//2.3 ������ťbutton_revise_account
	public void ReviseAccount() {
		hint.setText("");
		text_oldPassword.setDisable(false);
		text_newPassword.setDisable(false);
		
	}
	
	//2.4 ������ťbutton_submit_profile
	public void SubmitProfile() {
		infoWarning.setVisible(false);
		try {
			//1. ��¼�޸���Ϣ
			if(text_name.getText().length() > 0){
				stu.setName(text_name.getText());
			}
			if(text_gender.getText().length() > 0){
				stu.setGender(text_gender.getText());
			}
			if(text_classes.getText().length() > 0){
				stu.setClasses(Integer.valueOf(text_classes.getText().trim()));
			}
			if(text_phone.getText().length() > 0){
				stu.setPhone(text_phone.getText());
			}
			if(text_address.getText().length() > 0){
				stu.setHomeAddress(text_address.getText());
			}
			if(text_contact_name.getText().length() > 0){
				stu.setContactsName(text_contact_name.getText());
			}
			if(text_contact_phone.getText().length() > 0){
				stu.setContactsPhone(text_contact_phone.getText());
			}
			text_name.setDisable(true);
			text_gender.setDisable(true);
			text_classes.setDisable(true);
			text_phone.setDisable(true);
			text_address.setDisable(true);
			text_contact_name.setDisable(true);
			text_contact_phone.setDisable(true);
			
			//2. ����stuList���Student����
			stuMap.put(stu.getId(), stu);
			
			//3. д���޸ĺ��ѧ��������Ϣ
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt", stuMap);
			System.out.println("Student�����޸ĳɹ�");
		}catch(NumberFormatException e) {
			infoWarning.setText("����İ༶��Ϣ��ʽ��������������");
			infoWarning.setVisible(true);
		}
		
		//4. ������ʾ�Ի���
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("������Ϣ�޸ĳɹ�");
		alert.showAndWait();
	}
	
	//2.5 ������ťbutton_submit_account
	public void SubmitAccount() {

		//1. �ж�---�������������ԭʼ������ͬ���޸�����
		if(account.getPassword().equals(text_oldPassword.getText())){
			if(text_newPassword.getText().length() < 6){
				hint.setText("���볤������ڵ���6λ");	
			}else {
				account.setPassword(text_newPassword.getText());
				hint.setText("�����޸ĳɹ�");
			}
			//3. ����accList���Account����
			accMap.put(account.getId(), account);
			//4. ��������д���޸ĺ������
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\E:\\eclipse_workspace\\StudentManagerSubmission\\database\\Account.txt", accMap);
			System.out.println("Account�����޸ĳɹ�");
			//4. ������ʾ�Ի���
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("�����޸ĳɹ�");
			alert.showAndWait();
		}else{
			hint.setText("���벻��ȷ����������ȷ����");
		}
		
		//3. �����ı���״̬Ϊ����ѡ
		text_oldPassword.setDisable(true);
		text_newPassword.setDisable(true);
		
		
	}
	
	//2.6 �������˵�buttons
	public void EnterProfile() {
		button_profile.setOpacity(0.38);
	}
	public void ExitProfile() {
		button_profile.setOpacity(0.0);
	}
	public void ProfileWindow() {
		pane_Home.setVisible(false);
		pane_Profile.setVisible(true);
		pane_Score.setVisible(false);
		pane_Feedback.setVisible(false);
	}
	
	
	
	/**
	 * 3. ScorePane��
	 */
	/**
	 * ---�ؼ���
	 */
	
	@FXML
	private Pane pane_Score;
	
	//Button��
	@FXML
	private Button button_score;
	
	//Table��
	@FXML
	private TableView<StudentTestEntry> table_Score;
	@FXML
	private Tab tab_inquireScore;
	@FXML
	private Tab tab_inquireHomework;
	
	//TableColumnColumn��
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_test;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_score;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_chinese;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_math;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_english;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_physics;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_chemistry;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_biology;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_politics;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_history;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_geology;
	@FXML
	private TableColumn<ArrayList<StudentTestEntry>, String> col_rank;
	
	//ChoiceBox��
	@FXML
	private ChoiceBox<String> choice_homework;
	@FXML
	private ChoiceBox<String> choice_subject;
	@FXML
	private TextArea text_HomeworkScore;
	@FXML
	private TextArea text_Knowledge;
	@FXML
	private TextArea text_Comment;
	@FXML
	private Label label_warning;
	
	/**
	 * ---������
	 */
	//3.1 ����button_score
	public void EnterScore() {
		button_score.setOpacity(0.38);
	}
	public void ExitScore() {
		button_score.setOpacity(0.0);
	}
	public void ScoreWindow() {
		ShowScores();
		pane_Home.setVisible(false);
		pane_Profile.setVisible(false);
		pane_Score.setVisible(true);
		pane_Feedback.setVisible(false);
		ChooseHomework();
		ChooseSubject();	
	}
	
	//3.2 ��ʼ�����
	public void ChooseHomework() {
		label_warning.setVisible(false);
		choice_homework.setItems(FXCollections.observableArrayList("��һ����ҵ","�ڶ�����ҵ","��������ҵ"
				,"���Ĵ���ҵ","�������ҵ","��������ҵ","���ߴ���ҵ","�ڰ˴���ҵ","�ھŴ���ҵ","��ʮ����ҵ"));
	}
	public void ChooseSubject() {
		label_warning.setVisible(false);
		choice_subject.setItems(FXCollections.observableArrayList(
				"����","��ѧ","Ӣ��","����","��ѧ","����","����","��ʷ","����"));
	}
	
	//3.3 չʾѧ���ɼ�
	public void ShowScores() {
		//1. ��������
		ObservableList<StudentTestEntry> scores = FXCollections.observableArrayList();
		ArrayList<StudentTestEntry> scoreList = new ArrayList<>(scoreMap.values());
		for(int i=0;i<scoreList.size();i++) {
			scores.add(scoreList.get(i));
		}
		//2.  ���ñ�񣬰󶨱���ÿ��
		col_test.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("test"));
		col_score.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("score"));
		col_chinese.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("chinese"));
		col_math.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("math"));
		col_english.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("english"));
		col_physics.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("physics"));
		col_chemistry.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("chemistry"));
		col_biology.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("biology"));
		col_politics.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("politics"));
		col_history.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("history"));
		col_geology.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("geology"));
		col_rank.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentTestEntry>,String>("rank"));
		//3. չʾ���
		table_Score.setItems(scores);
	}
	
	
	//3.4 չʾѧ����ҵ�÷�
	public void ShowHomework() {
		//1. ������ѡ����չʾ�ɼ�
		String homework = choice_homework.getSelectionModel().getSelectedItem();
		String subject = choice_subject.getSelectionModel().getSelectedItem();
		HomeworkKey hk = new HomeworkKey(homework,subject);
		StudentHomeworkEntry hse  = homeworkMap.get(hk);
		if(hse!=null) {
			text_HomeworkScore.setText(hse.getScore()+"");
			text_Knowledge.setText(hse.getKnowledge());
			text_Comment.setText(hse.getComment());
		}else {
			label_warning.setText("��ѯ�ĳɼ���δ¼��");
			label_warning.setVisible(true);

			text_HomeworkScore.setText("");
			text_Knowledge.setText("");
			text_Comment.setText("");
		}
	}
	
	/**
	 * 4. FeedbackPane��
	 */
	
	/**
	 * ---�ؼ���
	 */
	@FXML
	private Pane pane_Feedback;
	
	//Button��
	@FXML
	private Button button_feedback;
	@FXML
	private RadioButton button_Anonymity;
	
	//TextField��
	@FXML
	private TextField text_title_Teacher;
	@FXML
	private TextField text_title_Headmaster;
	
	@FXML
	private TextArea textArea_Teacher;
	@FXML
	private TextArea textArea_Headmaster;
	
	//Label��
	@FXML
	private Label label_TeacherWarning;
	@FXML
	private Label label_HeadmasterWarning;
	
	/**
	 * ---������
	 */
	
	//4.1 ����button_feedback
	public void EnterFeedback() {
		button_feedback.setOpacity(0.38);
	}
	public void ExitFeedback() {
		button_feedback.setOpacity(0.0);
	}
	public void FeedbackWindow() {
		pane_Home.setVisible(false);
		pane_Profile.setVisible(false);
		pane_Score.setVisible(false);
		pane_Feedback.setVisible(true);
	}
	
	//4.2 ������ťbutton_
	public void submitFeedback(){
		//1. �ύ�Խ�ʦ�ķ���
		label_TeacherWarning.setVisible(false);
		label_HeadmasterWarning.setVisible(false);
		
		StudentService.Feedback(text_title_Teacher, textArea_Teacher,
				label_TeacherWarning,button_Anonymity,stu,"��ʦ");
		//2. �ύ��У���ķ���
		StudentService.Feedback(text_title_Headmaster, textArea_Headmaster,
				label_HeadmasterWarning,button_Anonymity,stu,"У��");
		
		//3. ������ʾ�Ի���
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("�ύ�ɹ�");
		alert.showAndWait();
	}
	
	/**
	 * 5. Exit��
	 */
	/**
	 * ---�ؼ���
	 */
	//Button��
	@FXML
	private Button button_exit;
	
	/**
	 * ---������
	 */
	//5.1  ����button_exit
	public void EnterExit() {
		button_exit.setOpacity(0.38);
	}
	public void ExitExit() {
		button_exit.setOpacity(0.0);
	}
	public void ExitWindow(ActionEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("�˳�����");
		alert.setContentText("ȷ���˳�?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			((Node) (e.getSource())).getScene().getWindow().hide();
			try {
				new LoginView().show();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		} else {
		   return;
		}
	}
	
}
