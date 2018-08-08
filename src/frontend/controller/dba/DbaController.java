package frontend.controller.dba;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;

import backend.entity.Account;
import backend.entry.dba.DbaClassScoreEntry;
import backend.entry.dba.DbaGradeScoreEntry;
import backend.entry.dba.DbaSearchResultEntry;
import backend.entry.dba.DbaTeacherDutyEntry;
import backend.entry.student.StudentDutyEntry;
import backend.entry.student.StudentFeedbackEntry;
import backend.entry.teacher.TeacherDutyEntry;
import frontend.controller.student.StudentService;
import frontend.controller.teacher.TeacherService;
import frontend.view.LoginView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;



public class DbaController implements Initializable{
	//1. ������
	ObservableList<DbaTeacherDutyEntry> dutyList = FXCollections.observableArrayList();
	private LocalDate date;
	//2. ���췽��
	public DbaController() {}
		
	//3. ������
	//����1����ʼ��
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//2. ��ʼ��UI���
		choice_Subject.setItems(FXCollections.observableArrayList("�ܷ�","����","��ѧ","Ӣ��",
				"����","��ѧ","����","����","��ʷ","����"));
		choice_Test.setItems(FXCollections.observableArrayList("��һ���¿�"
				,"�ڶ����¿�","�������¿�","���Ĵ��¿�","������¿�","�������¿�"
				,"���ߴ��¿�","�ڰ˴��¿�","�ھŴ��¿�","��ʮ���¿�"));
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
		pane_Account.setVisible(false);
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
	 * 2. AccountPane��
	 */
	
	/**
	 * ---�ؼ���
	 */	
	@FXML
	private Pane pane_Account;
	
	//TableView��
	@FXML
	private TableView<DbaSearchResultEntry> tab_Search;
	@FXML
	private TableColumn<ArrayList<DbaSearchResultEntry>, TextField> col_AccountId;
	@FXML
	private TableColumn<ArrayList<DbaSearchResultEntry>, TextField> col_AccountName;
	@FXML
	private TableColumn<ArrayList<DbaSearchResultEntry>, TextField> col_AccountUsertype;
	@FXML
	private TableColumn<ArrayList<DbaSearchResultEntry>, Button> col_AccountDelete;
	@FXML
	private TableColumn<ArrayList<DbaSearchResultEntry>, Button> col_AccountModify;
	
	//Button��
	@FXML
	private Button button_account;
	@FXML
	private ImageView image_Search;
	
	//TextField��
	@FXML
	private TextField text_SearchId;
	@FXML
	private Label label_warning;
	
	/**
	 * ---������
	 */
	
	//2.1 �������˵�button_account
	public void EnterAccount() {
		button_account.setOpacity(0.38);
	}
	public void ExitAccount() {
		button_account.setOpacity(0.0);
	}
	public void AccountWindow() {
		pane_Home.setVisible(false);
		pane_Account.setVisible(true);
		pane_Score.setVisible(false);
		pane_Duty.setVisible(false);
		pane_Feedback.setVisible(false);
	}
	
	
	//2.2  ����image_Search
	public void ShowSearchResult() {
		//����UI
		label_warning.setVisible(false);
		//1. ���ɽ���б�
		HashMap<Integer,Account> accMap= StudentService.loadAccount();
		if(accMap.isEmpty()) {
			label_warning.setVisible(true);
			label_warning.setText("��ǰ�û��б�Ϊ��");
			return;
		}
		
		//2. ����input��չʾ���		
		ArrayList<DbaSearchResultEntry> result = new ArrayList<>();
		String input = text_SearchId.getText();
		result = DbaService.searchResult(accMap,input,label_warning);		
		ObservableList<DbaSearchResultEntry> searchResult 
				= FXCollections.observableArrayList(result); 
		
		//3. ���ñ�񣬰󶨱���ÿ��		
		col_AccountId.setCellValueFactory(
				new PropertyValueFactory<ArrayList<DbaSearchResultEntry>,TextField>("text_id"));
		col_AccountName.setCellValueFactory(
				new PropertyValueFactory<ArrayList<DbaSearchResultEntry>,TextField>("text_name"));
		col_AccountUsertype.setCellValueFactory(
				new PropertyValueFactory<ArrayList<DbaSearchResultEntry>,TextField>("text_usertype"));
		col_AccountDelete.setCellValueFactory(
				new PropertyValueFactory<ArrayList<DbaSearchResultEntry>,Button>("button_delete"));
		col_AccountModify.setCellValueFactory(
				new PropertyValueFactory<ArrayList<DbaSearchResultEntry>,Button>("button_modify"));
		
		//4. չʾ���
		tab_Search.setItems(searchResult);
	}
	
	
	//2.3 ����button_Save
	public void SaveSearchResult() {
		//1. ˢ�±��
		ShowSearchResult();
		//2. ������ʾ�Ի���
		Alert alert1 = new Alert(AlertType.INFORMATION);
		alert1.setContentText("����ɹ�");
		alert1.showAndWait();
	
	}
	
	
	
	
	/**
	 * 3. ScorePane��
	 */
	/**
	 * ---�ؼ���
	 */
	
	//Pane��
	@FXML
	private Pane pane_Score;
	@FXML
	private TitledPane pane_GradeScore;
	@FXML
	private TitledPane pane_ClassScore;
	
	//Tab��
	@FXML
	private TableView<DbaGradeScoreEntry> tab_GradeScore;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, String> col_GradeStatistics;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradeScore;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradeChinese;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradeMath;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradeEnglish;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradePhysics;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradeChemistry;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradeBiology;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradePolitics;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradeHistory;
	@FXML
	private TableColumn<ArrayList<DbaGradeScoreEntry>, Double> col_GradeGeology;
	
	
	
	
	@FXML
	private TableView<DbaClassScoreEntry> tab_ClassScore;
	@FXML
	private TableColumn<ArrayList<DbaClassScoreEntry>, String> col_Class;
	@FXML
	private TableColumn<ArrayList<DbaClassScoreEntry>, Double> col_ClassMean;
	@FXML
	private TableColumn<ArrayList<DbaClassScoreEntry>, Double> col_ClassMax;
	@FXML
	private TableColumn<ArrayList<DbaClassScoreEntry>, Double> col_ClassMin;
	@FXML
	private TableColumn<ArrayList<DbaClassScoreEntry>, Double> col_ClassStd;
	@FXML
	private TableColumn<ArrayList<DbaClassScoreEntry>, Integer> col_ClassCount;
	
	//Button��
	@FXML
	private Button button_score;
	@FXML
	private ChoiceBox<String> choice_Subject;
	@FXML
	private ChoiceBox<String> choice_Test;
	
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
		pane_Account.setVisible(false);
		pane_Score.setVisible(true);
		pane_Duty.setVisible(false);
		pane_Feedback.setVisible(false);
	}
	
	//3.2 ����image_search
	public void ShowScoreAnalysis() {
		pane_GradeScore.setExpanded(true);
		ShowGradeScore();
		pane_ClassScore.setExpanded(true);
		ShowClassScore();
	}
	
	//3.2 ����titlePane_GradeScore
	public void ShowGradeScore() {
		//1. ��ȡ����
		//��ȡȫУ����ѧ����ÿ�ο��Ե�ͳ��������߷֣���ͷ֣�ƽ���֣�����	
		HashMap<String,HashMap<String,DbaGradeScoreEntry>> analysis = DbaService.gradeScoreAnalysis();
		//��ѯ�Ŀ�������
		String test = choice_Test.getSelectionModel().getSelectedItem();
		//�ôο��Ե�ȫУͳ�Ʒ�������߷֣���ͷ֣�ƽ���֣�����
		HashMap<String,DbaGradeScoreEntry> testAnalysis = new HashMap<>();
		if(test!=null) {
			testAnalysis = analysis.get(test);
		}

		//2. չʾ�б�
		ObservableList<DbaGradeScoreEntry> grade = FXCollections.observableArrayList();
		if(testAnalysis!=null) {
			grade.add(testAnalysis.get("��߷�"));
			grade.add(testAnalysis.get("��ͷ�"));
			grade.add(testAnalysis.get("ƽ����"));
			grade.add(testAnalysis.get("����"));
		}
		
		//3. ���ñ�񣬰󶨱���ÿ��

		col_GradeStatistics.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,String>("statistics"));
		col_GradeScore.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("score"));
		col_GradeChinese.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("chinese"));
		col_GradeMath.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("math"));
		col_GradeEnglish.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("english"));
		col_GradePhysics.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("physics"));
		col_GradeChemistry.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("chemistry"));
		col_GradeBiology.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("biology"));
		col_GradePolitics.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("politics"));
		col_GradeHistory.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("history"));
		col_GradeGeology.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaGradeScoreEntry>,Double>("geology"));
	
		//4. չʾ���
		tab_GradeScore.setItems(grade);
	}
	
	
	//3.3 ����titledPane_ClassScore
	public void ShowClassScore() {
		//1. ��ȡ����
		/**HashMap<String test, 
		 * 		HashMap<String subject,
		  					HashMap<String classes,DbaClassScoreEntry>>>*/
		HashMap<String, HashMap<String, HashMap<String, DbaClassScoreEntry>>> classAnalysis = DbaService.classScoreAnalysis();
		//��ȡ����test
		String test = choice_Test.getSelectionModel().getSelectedItem();
		//��ȡ��Ŀ
		String subject = choice_Subject.getSelectionModel().getSelectedItem();
		
		//2. չʾ�б�
		ObservableList<DbaClassScoreEntry> resList = FXCollections.observableArrayList();
		if(test!=null&&subject!=null) {
			HashMap<String, DbaClassScoreEntry> map = classAnalysis.get(test).get(subject);
			ArrayList<DbaClassScoreEntry> result = new ArrayList<>(map.values());
			for(DbaClassScoreEntry e:result) {
				resList.add(e);
			}
		}
		//3. ���ñ�񣬰󶨱���ÿ��
		col_Class.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,String>("classes"));
		col_ClassMean.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Double>("mean"));
		col_ClassMax.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Double>("max"));
		col_ClassMin.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Double>("min"));
		col_ClassStd.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Double>("std"));
		col_ClassCount.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Integer>("count"));

		//3. չʾ���
		tab_ClassScore.setItems(resList);
	}

	/**
	 * 4. DutyPane��
	 */
	
	/**
	 * ---�ؼ���
	 */
	@FXML
	private Pane pane_Duty;
	
	//Button��
	@FXML
	private Button button_duty;
	@FXML
	private DatePicker date_Date;
	@FXML
	private DatePicker date_StuDate;
	
	//Table��---TeacherDuty
	@FXML
	private TableView<DbaTeacherDutyEntry> tab_Duty;
	@FXML
	private TableColumn<ArrayList<DbaTeacherDutyEntry>, Integer> col_TeacherId;
	@FXML
	private TableColumn<ArrayList<DbaTeacherDutyEntry>, String> col_TeacherName;
	@FXML
	private TableColumn<ArrayList<DbaTeacherDutyEntry>, String> col_TeacherClass;
	@FXML
	private TableColumn<ArrayList<DbaTeacherDutyEntry>, CheckBox> col_Morning;
	@FXML
	private TableColumn<ArrayList<DbaTeacherDutyEntry>, CheckBox> col_Noon;
	@FXML
	private TableColumn<ArrayList<DbaTeacherDutyEntry>, CheckBox> col_Evening;
	@FXML
	private TableColumn<ArrayList<DbaTeacherDutyEntry>, TextField> col_Note;

	
	//Table��---StudentDuty
	@FXML
	private TableView<StudentDutyEntry> tab_StudentDuty;
	@FXML
	private TableColumn<ArrayList<StudentDutyEntry>, String> col_StuId;
	@FXML
	private TableColumn<ArrayList<StudentDutyEntry>, String> col_StuName;
	@FXML
	private TableColumn<ArrayList<StudentDutyEntry>, String> col_StuClass;
	@FXML
	private TableColumn<ArrayList<StudentDutyEntry>, String> col_StuDuty;
	@FXML
	private TableColumn<ArrayList<StudentDutyEntry>, String> col_StuReason;
	
	/**
	 * ---������
	 */
	
	//4.1 ����button_duty
	public void EnterDuty() {
		button_duty.setOpacity(0.38);
		ShowTeacherDuty();
	}
	public void ExitDuty() {
		button_duty.setOpacity(0.0);
	}
	public void DutyWindow() {
		pane_Home.setVisible(false);
		pane_Account.setVisible(false);
		pane_Score.setVisible(false);
		pane_Duty.setVisible(true);
		pane_Feedback.setVisible(false);
	}
	
	//4.2 ����imageview_search
	public void ShowTeacherDuty() {
		//�ָ����
		tab_Duty.setDisable(false);
		tab_Duty.setEditable(true);
		
		//1. ������ʦ��������					
		date = date_Date.getValue();
		
		//2. ����չʾ�б�
		dutyList = DbaService.iniObList(date);
		
		//3.  ���ñ�񣬰󶨱���ÿ��
		col_TeacherId.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,Integer>("id"));
		col_TeacherName.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,String>("name"));
		col_TeacherClass.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,String>("classes"));
		col_Morning.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,CheckBox>("morning"));
		col_Noon.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,CheckBox>("noon"));
		col_Evening.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,CheckBox>("evening"));
		col_Note.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,TextField>("note"));

		
		//4. չʾ���
		tab_Duty.setItems(dutyList);
		
	}
	
	//4.2  ����button_SubmitDuty
	public void SubmitTeacherDuty() {
		//1.������ʾ�Ի���
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("�ύ���ڼ�¼");
		alert.setContentText("ȷ���ύ?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    //2. ����HashMap<LocalDate,TeacherDutyEntry> teacherDuty
			HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> teacherDuty 
				= DbaService.updateTeacherDuty(dutyList, date);
			//3. ���µ�teacherDutyд��TeacherDuty.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/TeacherDuty.txt", teacherDuty);	
			//4. �����Ի���
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("�ύ�ɹ�");
			alert1.showAndWait();
			//5. ʹ�����
			tab_Duty.setDisable(true);
		} else {
		   ShowTeacherDuty();
		}

	}

	
	//4.3 ������չʾѧ���ĳ��ڼ�¼
	public void ShowStudentDuty() {
		//1. ����ȫУ����ѧ���ĳ��ڼ�¼
		HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> dutyMap = TeacherService.loadStudentDuty();
		//2. ������ѡ���ڣ���ȡȫУ�ĳ��ڼ�¼
		LocalDate date = date_StuDate.getValue();
		HashMap<String,HashMap<String,StudentDutyEntry>> dutyDayMap = dutyMap.get(date);
		if(dutyDayMap==null) {
			dutyDayMap = new HashMap<>();
		}
		//3. ��ȫУ�ĳ��ڼ�¼��ȡ��һ��ArrayList
		ArrayList<StudentDutyEntry> dutyDayList = new ArrayList<>();
		for(Entry<String, HashMap<String, StudentDutyEntry>> e:dutyDayMap.entrySet()) {
			dutyDayList.addAll(e.getValue().values());
		}
		//4. �����ɱ��List
		ObservableList<StudentDutyEntry> stuDutyList = FXCollections.observableArrayList();
		for(StudentDutyEntry sde:dutyDayList) {
			stuDutyList.add(sde);
		}
		
		//5.  ���ñ�񣬰󶨱���ÿ��
		col_StuClass.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("classes"));
		col_StuId.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("name"));
		col_StuName.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("id"));
		col_StuDuty.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("duty"));
		col_StuReason.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("reason"));
		
		//6. չʾ���
		tab_StudentDuty.setItems(stuDutyList);
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
		pane_Account.setVisible(false);
		pane_Score.setVisible(false);
		pane_Duty.setVisible(false);
		pane_Feedback.setVisible(true);
		
	}
	
	//5.2 ����titledPane_Letters
	public void ShowLetters() {
		//��titledPane
		tab_Letters.setExpanded(true);
		
		//1. ��������
		ArrayList<StudentFeedbackEntry> feedbacks = TeacherService.loadFeedback();
		if(!feedbacks.isEmpty()) {
			//2. �����ؼ�����
			TitledPane[] tab_letter = new TitledPane[feedbacks.size()];
			
			//3. ����Accordion
			for(int i=0;i<feedbacks.size();i++) {
				//3.1 �½�TiledPane
				String title = (i+1)+"\t"+feedbacks.get(i).getDate().toString()
						+"\t�༶:"+feedbacks.get(i).getClasses()+"\t"+
						feedbacks.get(i).getName()+"\t\t��������:"
						+feedbacks.get(i).getType()+"\t\t����:"+feedbacks.get(i).getTitle();
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