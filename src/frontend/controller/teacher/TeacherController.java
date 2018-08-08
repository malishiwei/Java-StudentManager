package frontend.controller.teacher;


import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import backend.entity.Account;
import backend.entity.Teacher;
import backend.entry.student.HomeworkKey;
import backend.entry.student.StudentDutyEntry;
import backend.entry.student.StudentFeedbackEntry;
import backend.entry.student.StudentHomeworkEntry;
import backend.entry.student.StudentTestEntry;
import backend.entry.teacher.TeacherDutyEntry;
import frontend.controller.student.StudentService;
import frontend.view.LoginView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class TeacherController implements Initializable{
	//1. ������
	private Teacher teacher;
	private Account account;
	private ArrayList<Integer> stuIdList; //����ʦ��Ӧ�İ༶����ѧ��id�ļ���
	HashMap<Integer,Account> accMap = new HashMap<>();
	HashMap<Integer,Teacher> teaMap = new HashMap<>();
	private HashMap<Integer,HashMap<String,StudentTestEntry>> stuScoreMap;//ȫУ����ѧ���ɼ���
	private HashMap<Integer,HashMap<HomeworkKey,StudentHomeworkEntry>> stuHomeworkMap;//ȫУ����ѧ����ҵ��
	ObservableList<StudentDutyEntry> dutyList = FXCollections.observableArrayList();
	
	private TextField[][] text_Score;
	private TextField[][] text_Homework;
	
	//2. ���췽��
	public TeacherController() {}
		
	//3. ������
	//����1����ʼ��
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//1. ��ʼ������
		//Account.txt->accList
		accMap= StudentService.loadAccount();
		//Teacher.txt -> teaList
		teaMap = TeacherService.loadTeacher();
		//��ʼ��Teacher��Ӧ��Account
		account = TeacherService.findAccount(accMap);
		//��ʼ��Teacher����
		teacher = TeacherService.findTeacher(teaMap);
		//Students.txt->stuList
		stuIdList = TeacherService.loadStudentId(teacher);
//		for(int i=0;i<stuIdList.size();i++) {
//			System.out.println(stuIdList.get(i));
//		}
		//��ʼ��ȫУ����ѧ���ĳɼ���
		stuScoreMap = StudentService.loadStudentScore();
		//��ʼ����ʦ��Ӧ��ѧ����ҵ��
		stuHomeworkMap = StudentService.loadStudentHomework();
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
		pane_Duty.setVisible(false);
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
	
	//2.1 �������˵�buttons
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
		pane_Duty.setVisible(false);
		pane_Feedback.setVisible(false);
	}
		
		
	//2.2 ����UI�ؼ�
	public void initializeProfile() {
		//1. ������ʦ������Ϣ

		text_name.setPromptText(teacher.getName());
		text_gender.setPromptText(teacher.getGender());
		text_classes.setPromptText(String.valueOf(teacher.getClasses()));		
		text_id.setPromptText(String.valueOf(teacher.getId()));
		text_phone.setPromptText(teacher.getPhone());
		text_address.setPromptText(teacher.getHomeAddress());
		text_contact_name.setPromptText(teacher.getContactsName());		
		text_contact_phone.setPromptText(teacher.getContactsPhone());
		
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
	
	
	//2.3  ������ťbutton_revise_profile---�޸��û���Ϣ
	public void ReviseProfile() {
		infoWarning.setText("");;
		//�����޸�id��
		text_name.setDisable(false);
		text_gender.setDisable(false);
		text_classes.setDisable(false);
		text_phone.setDisable(false);
		text_address.setDisable(false);
		text_contact_name.setDisable(false);
		text_contact_phone.setDisable(false);
		
	}
	
	
	//2.4 ������ťbutton_revise_account
	public void ReviseAccount() {
		hint.setText("");
		text_oldPassword.setDisable(false);
		text_newPassword.setDisable(false);
		
	}
	
	//2.5 ������ťbutton_submit_profile
	public void SubmitProfile() {
		//1. ��¼�޸���Ϣ
		try {
			if(text_name.getText().length() > 0){
				teacher.setName(text_name.getText());
			}
			if(text_gender.getText().length() > 0){
				teacher.setGender(text_gender.getText());
			}
			if(text_classes.getText().length() > 0){
				teacher.setClasses(Integer.valueOf(text_classes.getText().trim()));
			}
			if(text_phone.getText().length() > 0){
				teacher.setPhone(text_phone.getText());
			}
			if(text_address.getText().length() > 0){
				teacher.setHomeAddress(text_address.getText());
			}
			if(text_contact_name.getText().length() > 0){
				teacher.setContactsName(text_contact_name.getText());
			}
			if(text_contact_phone.getText().length() > 0){
				teacher.setContactsPhone(text_contact_phone.getText());
			}
			text_name.setDisable(true);
			text_gender.setDisable(true);
			text_classes.setDisable(true);
			text_phone.setDisable(true);
			text_address.setDisable(true);
			text_contact_name.setDisable(true);
			text_contact_phone.setDisable(true);
			
			//2. ����stuList���Student����
			teaMap.put(teacher.getId(),teacher);
			
			//3. ����teaList -> Teachers.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt", teaMap);
			System.out.println("Teacher�����޸ĳɹ�");
			
		}catch(NumberFormatException e) {
			infoWarning.setText("����İ༶��Ϣ��ʽ��������������");
			infoWarning.setVisible(true);
		}
		
		//4. ������ʾ�Ի���
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("������Ϣ�޸ĳɹ�");
		alert.showAndWait();	
	}
	
	//2.6 ������ťbutton_submit_account
	public void SubmitAccount() {
		//1. �ж�---�������������ԭʼ������ͬ���޸�����
		if(account.getPassword().equals(text_oldPassword.getText())){
			if(text_newPassword.getText().length() < 6){
				hint.setText("���볤������ڵ���6λ");	
			}else {
				account.setPassword(text_newPassword.getText());
				hint.setText("�����޸ĳɹ�");
				//������ʾ�Ի���
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("�˻��޸ĳɹ�");
				alert.showAndWait();
			}
			//3. ����accList���Account����
			accMap.put(account.getId(), account);
			//4. ��������д���޸ĺ������
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt", accMap);
			
		}else{
			hint.setText("���벻��ȷ����������ȷ����");
		}
		
		//3. �����ı���״̬Ϊ����ѡ
		text_oldPassword.setDisable(true);
		text_newPassword.setDisable(true);
		
		//������ʾ�Ի���
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("�����޸ĳɹ�");
		alert.showAndWait();
	}
	
	
	
	
	
	/**
	 * 3. ScorePane��
	 */
	/**
	 * ---�ؼ���
	 */
	
	@FXML
	private Pane pane_Score;
	@FXML
	private GridPane gridpane_Score;
	@FXML
	private GridPane pane_Homework;
	@FXML
	private TextArea text_Knowledge;
	@FXML
	private TextField text_Banji;
	
	//Button��
	@FXML
	private Button button_score;
	
	//Choice_Box��
	@FXML
	private ChoiceBox<String> choice_Test;
	@FXML
	private ChoiceBox<String> choice_Homework;
	@FXML
	private ChoiceBox<String> choice_Subject;
	
	
	
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
		pane_Home.setVisible(false);
		pane_Profile.setVisible(false);
		pane_Score.setVisible(true);
		pane_Duty.setVisible(false);
		pane_Feedback.setVisible(false);
		
		//����ѡ���ѡ��
		choice_Test.setItems(FXCollections.observableArrayList("��һ���¿�","�ڶ����¿�","�������¿�"
				,"���Ĵ��¿�","������¿�","�������¿�","���ߴ��¿�","�ڰ˴��¿�","�ھŴ��¿�","��ʮ���¿�"));
		choice_Homework.setItems(FXCollections.observableArrayList("��һ����ҵ","�ڶ�����ҵ","��������ҵ"
				,"���Ĵ���ҵ","�������ҵ","��������ҵ","���ߴ���ҵ","�ڰ˴���ҵ","�ھŴ���ҵ","��ʮ����ҵ"));
		choice_Subject.setItems(FXCollections.observableArrayList("����","��ѧ","Ӣ��","����",
				"��ѧ","����","����","��ʷ","����"));
		text_Banji.setText(teacher.getClasses()+"");
				
	}
	
	//3.2 ¼��ѧ���ɼ�---����titlegridpane_ScoreScore
	public void ImportScore() {
		//1. ��������
		/**
		 * ��ǰ����Ϊ��
		 * stuScoreMap: HashMap<Integer,HashMap<String,StudentTestEntry>>
		 * Ϊ�˷���ʹ�ã�������ȡ��EntrySet<Integer,HashMap<String,StudentTestEntry>>
		 */
		//��ȡ����ѧ������
		ArrayList<String> stuNameList = TeacherService.loadStudentName(teacher);
		
		//2.����GridPane���ԣ�gridpane_Score������������-stuScoreMap.size()  ��-13
		gridpane_Score.setDisable(false);
		gridpane_Score.setGridLinesVisible(false);
		gridpane_Score.setVgap(3);
		int n = stuIdList.size(); //����ʦ���ϵ�ѧ������ 
		text_Score = new TextField[n][13];
		String test = choice_Test.getSelectionModel().getSelectedItem();

	    //3. ��ӵ�Ԫ��
		for(int i=0;i<n;i++) {
			for(int j=0;j<13;j++) {
				text_Score[i][j] = new TextField("");
				text_Score[i][j].setAlignment(Pos.CENTER);
				gridpane_Score.add(text_Score[i][j], j, i);
			}
		}
		
		//4. ��ʼ����Ԫ���е�����
		TeacherService.initializeScorePane(stuScoreMap, text_Score,
				stuIdList, stuNameList, test);
		
		
	}
	
	
	//3.4 �ύѧ���ɼ�---����button�ύ�ɼ�
	public void SubmitScore() {
		//����ȷ�϶Ի���
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("�ύ�ɼ�");
		alert.setContentText("ȷ���ύ?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//1. ����ȫУ�ɼ���
			/**
			 * ��TextField[][] text_Score��choice_Test test �ڼ��ο��� ��Ϊ�������뺯����
			 * �õ��ð�����ѧ���ڼ��ο��Եĳɼ�
			 */
			stuScoreMap = TeacherService.updateStudentScore(stuScoreMap,text_Score,choice_Test,teacher.getClasses()+"");
			//2. ���µĳɼ���д��.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentScore.txt",stuScoreMap);
			//3. UI�������
			gridpane_Score.setDisable(true);
			//������ʾ�Ի���
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("�ύ�ɹ�");
			alert1.showAndWait();
		}else {
			return;
		}

	}
	
	
	//3.4  ����ѧ����ҵ---����button����
	public void ImportHomework() {
		//1. ��������
		/**
		 * ��ǰ����Ϊ��
		 * stuHomeworkMap: HashMap<Integer,HashMap<String,StudentHomeworkEntry>>
		 * Ϊ�˷���ʹ�ã�������ȡ��EntrySet<Integer,HashMap<String,StudentTestEntry>>
		 */
		//��ȡ����ѧ������
		ArrayList<String> stuNameList = TeacherService.loadStudentName(teacher);
		
		//2.����GridPane���ԣ�gridpane_Score������������-id.size()  ��-4
		pane_Homework.setDisable(false);
		text_Knowledge.setDisable(false);
		pane_Homework.setGridLinesVisible(false);
		pane_Homework.setVgap(3);
		int n = stuIdList.size(); //����ʦ���ϵ�ѧ������ 
		text_Homework= new TextField[n][4];
		String homework = choice_Homework.getSelectionModel().getSelectedItem();
		String subject = choice_Subject.getSelectionModel().getSelectedItem();
		
		//3. ��ӵ�Ԫ��
		for(int i=0;i<n;i++) {		
			//3.1 ����id��
			//��ȡ��i��ѧ����id
			text_Homework[i][0] = new TextField("");

			//3.2 ����name��
			text_Homework[i][1] = new TextField("");

			//3.3 ���õ÷���
			text_Homework[i][2] = new TextField("");
			
			//3.4 ����������
			text_Homework[i][3] = new TextField("");
			
			//3.5 ��Ӱ�ť��GridPane
			pane_Homework.add(text_Homework[i][0],0,i);
			pane_Homework.add(text_Homework[i][1],1,i);
			pane_Homework.add(text_Homework[i][2],2,i);
			pane_Homework.add(text_Homework[i][3],3,i);
		}
		
	    //4. ��ʼ����Ԫ��
		TeacherService.initializeHomeworkPane(stuHomeworkMap, text_Homework,
				stuIdList, stuNameList,homework,subject);
		
		
	}
	
	
	//3.5  �ύѧ����ҵ---����button_SubmitHomework
	public void SubmitHomework() {
		//1. ����ȷ�϶Ի���
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("�ύ��ҵ");
		alert.setContentText("ȷ���ύ?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//1. ����ȫУ�ɼ���
			/**
			 * ��TextField[][] text_Homework��choice_Homworkt choice_Subject ��Ϊ�������뺯����
			 * �õ��ð�����ѧ��subject��Ŀ��homework�ĵ÷ֱ�
			 */
			stuHomeworkMap = TeacherService.updateStudentHomework(stuHomeworkMap,
					text_Homework,choice_Homework,choice_Subject,text_Knowledge);
			//2. ���µ���ҵ��д��.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentHomework.txt",stuHomeworkMap);
			//4. ����UI����
			text_Knowledge.setDisable(true);
			pane_Homework.setDisable(true);
			//������ʾ�Ի���
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("�ύ�ɹ�");
			alert1.showAndWait();
		}else {
			return;
		}	
	}
	
	
	
	/**
	 * 4. DutyPane��
	 */
	
	/**
	 * ---�ؼ���
	 */
	@FXML
	private Pane pane_Duty;
	
	//TextField��
	@FXML
	private TextField text_Class;
	@FXML
	private DatePicker date_Date;
	@FXML
	private TextField text_Id;
	@FXML
	private TextField text_Name;
	@FXML
	private TextField text_Duty;
	@FXML
	private TextArea text_Reason;

	//Button��
	@FXML
	private Button button_duty;
	
	
	//Table��
	@FXML
	private TableView<StudentDutyEntry> tab_Duty;
	@FXML
	private TableColumn<ArrayList<TeacherDutyEntry>, LocalDate> col_date;
	@FXML
	private TableColumn<ArrayList<TeacherDutyEntry>, String> col_id;
	@FXML
	private TableColumn<ArrayList<TeacherDutyEntry>, String> col_class;
	@FXML
	private TableColumn<ArrayList<TeacherDutyEntry>, String> col_name;
	@FXML
	private TableColumn<ArrayList<TeacherDutyEntry>, String> col_duty;
	@FXML
	private TableColumn<ArrayList<TeacherDutyEntry>, String> col_reason;
	
	
	
	/**
	 * ---������
	 */
	
	//4.1 ����button_duty
	public void EnterDuty() {
		button_duty.setOpacity(0.38);
	}
	public void ExitDuty() {
		button_duty.setOpacity(0.0);
	}
	public void DutyWindow() {
		pane_Home.setVisible(false);
		pane_Profile.setVisible(false);
		pane_Score.setVisible(false);
		pane_Duty.setVisible(true);
		pane_Feedback.setVisible(false);
		
		//�̶��༶
		text_Class.setText(teacher.getClasses()+"");
		
		//չʾ�ð�ѧ����ʷ���ڼ�¼
		ShowDuty(TeacherService.loadStudentDuty());
	}
	
	//4.2 ����button_AddDuty()
	public void AddDuty() {
		//3. ����ť���
		text_Id.setDisable(false);
		text_Name.setDisable(false);
		text_Duty.setDisable(false);
		text_Reason.setDisable(false);
	}
	
	
	//4.3 ����button_SubmitDuty
	public void SubmitDuty() {
		//1. ��������ĳ�����Ϣ������һ��DutyEntry
		LocalDate date = date_Date.getValue();
		String id = text_Id.getText();
		String banji = text_Class.getText();
		String name = text_Name.getText();
		String duty = text_Duty.getText();
		String reason = text_Reason.getText();
		StudentDutyEntry sde = new StudentDutyEntry(date,id,banji,name,duty,reason);
		
		//2.������ʾ�Ի���
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("�ύ���ڼ�¼");
		alert.setContentText("ȷ���ύ?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//2.1 ����ȫУdutyMap
			HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>>
			dutyMap = TeacherService.updateStudentDuty(sde);
			//2.2 ���µ�dutyMapд��.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentDuty.txt", dutyMap);
			//2.3 ��������Ϣ��ʾ�������table��
			ShowDuty(dutyMap);
			//2.4 ������ʾ�Ի���
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("�ύ�ɹ�");
			alert1.showAndWait();
			//2.5 ����ť���
			text_Id.setDisable(true);
			text_Name.setDisable(true);
			text_Duty.setDisable(true);
			text_Reason.setDisable(true);
		}
	}

	public void ShowDuty(HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> dutyMap) {
		//1. ����date���Ͱ༶��չʾ�ð�ĳ��ڼ�¼
		LocalDate date = date_Date.getValue();
		String classes = text_Class.getText();
		ArrayList<StudentDutyEntry> classDutyList = new ArrayList<>();
		for(Map.Entry<LocalDate, HashMap<String,HashMap<String,StudentDutyEntry>>> map:dutyMap.entrySet()) {
			if(map.getValue().get(classes)!=null) {
				classDutyList.addAll(map.getValue().get(classes).values());
			}	
		}
		if(dutyMap.get(date)!=null) {
			if(dutyMap.get(date).get(classes)!=null) {
				classDutyList = new ArrayList<>(dutyMap.get(date).get(classes).values());
			}
		}
	
		//2. ���ɱ���б�
		for(StudentDutyEntry sde:classDutyList) {
			dutyList.add(sde);
		}
	
		//2.  ���ñ�񣬰󶨱���ÿ��
		col_date.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,LocalDate>("date"));
		col_id.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("id"));
		col_class.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("banji"));
		col_name.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("name"));
		col_duty.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("duty"));
		col_reason.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("reason"));
		
		//3. չʾ���
		tab_Duty.setItems(dutyList);
	}
	
	
	/**
	 * 5. Feedback��
	 */
	/**
	 * ---�ؼ���
	 */
	@FXML
	private Pane pane_Feedback;
	@FXML
	private TitledPane tab_Letters;
	@FXML
	private Accordion pane_Letter;
	
	//Button��
	@FXML
	private Button button_feedback;
	@FXML
	private ImageView image_letter;
	
	/**
	 * ---������
	 */
	//5.1  ����button_exit
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
		pane_Duty.setVisible(false);
		pane_Feedback.setVisible(true);
	}
	
	//5.2 ����titledPane_Letters: ��ȡѧ������ʦ�ķ���
	public void ShowLetters() {
		//1. ��titledPane
		tab_Letters.setExpanded(true);
		
		//1. ��������
		ArrayList<StudentFeedbackEntry> feedbacks = TeacherService.loadTeacherFeedback(teacher);
		if(!feedbacks.isEmpty()) {
			//2. �����ؼ�����
			TitledPane[] tab_letter = new TitledPane[feedbacks.size()];
			
			//3. ����Accordion
			for(int i=0;i<feedbacks.size();i++) {
				//3.1 �½�TiledPane
				String title = (i+1)+"\t"+feedbacks.get(i).getDate().toString()
					+"\t"+feedbacks.get(i).getName()+"\t\t����:"+feedbacks.get(i).getTitle();
			
				TextArea text =new TextArea(feedbacks.get(i).getContent());
				text.setPromptText("��������");
				tab_letter[i] = new TitledPane(title,text);
						
				//3.2 ��������
				pane_Letter.autosize();
				pane_Letter.getPanes().add(tab_letter[i]);
			
			}
			
		}
		
		//4. ����ť
		image_letter.setDisable(true);
	}
	
	/**
	 * 6. Exit��
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
	//6.1  ����button_exit
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