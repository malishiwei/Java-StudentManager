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
	//1. 属性区
	ObservableList<DbaTeacherDutyEntry> dutyList = FXCollections.observableArrayList();
	private LocalDate date;
	//2. 构造方法
	public DbaController() {}
		
	//3. 方法区
	//方法1：初始化
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//2. 初始化UI插件
		choice_Subject.setItems(FXCollections.observableArrayList("总分","语文","数学","英语",
				"物理","化学","生物","政治","历史","地理"));
		choice_Test.setItems(FXCollections.observableArrayList("第一次月考"
				,"第二次月考","第三次月考","第四次月考","第五次月考","第六次月考"
				,"第七次月考","第八次月考","第九次月考","第十次月考"));
	}
	
	
	/**
	 * 1. HomePane区
	 */
	/**
	 * ---控件区
	 */
	
	@FXML
	private Pane pane_Home;
	
	//Button区
	@FXML
	private Button button_home;
	
	//TextField区
	@FXML
	private TextField text_slogan;
	
	/**
	 * ---方法区
	 */
	//1.1  监听button_home
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
	
	//1.2 监听text_slogan
	public void EnterSlogan() {
		text_slogan.setDisable(false);
		text_slogan.getText();
	}
	public void ExitSlogan() {
		text_slogan.setDisable(true);
	}

	
	
	/**
	 * 2. AccountPane区
	 */
	
	/**
	 * ---控件区
	 */	
	@FXML
	private Pane pane_Account;
	
	//TableView区
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
	
	//Button区
	@FXML
	private Button button_account;
	@FXML
	private ImageView image_Search;
	
	//TextField区
	@FXML
	private TextField text_SearchId;
	@FXML
	private Label label_warning;
	
	/**
	 * ---方法区
	 */
	
	//2.1 监听主菜单button_account
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
	
	
	//2.2  监听image_Search
	public void ShowSearchResult() {
		//调整UI
		label_warning.setVisible(false);
		//1. 生成结果列表
		HashMap<Integer,Account> accMap= StudentService.loadAccount();
		if(accMap.isEmpty()) {
			label_warning.setVisible(true);
			label_warning.setText("当前用户列表为空");
			return;
		}
		
		//2. 根据input来展示结果		
		ArrayList<DbaSearchResultEntry> result = new ArrayList<>();
		String input = text_SearchId.getText();
		result = DbaService.searchResult(accMap,input,label_warning);		
		ObservableList<DbaSearchResultEntry> searchResult 
				= FXCollections.observableArrayList(result); 
		
		//3. 配置表格，绑定表格的每列		
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
		
		//4. 展示表格
		tab_Search.setItems(searchResult);
	}
	
	
	//2.3 监听button_Save
	public void SaveSearchResult() {
		//1. 刷新表格
		ShowSearchResult();
		//2. 弹出提示对话框
		Alert alert1 = new Alert(AlertType.INFORMATION);
		alert1.setContentText("保存成功");
		alert1.showAndWait();
	
	}
	
	
	
	
	/**
	 * 3. ScorePane区
	 */
	/**
	 * ---控件区
	 */
	
	//Pane区
	@FXML
	private Pane pane_Score;
	@FXML
	private TitledPane pane_GradeScore;
	@FXML
	private TitledPane pane_ClassScore;
	
	//Tab区
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
	
	//Button区
	@FXML
	private Button button_score;
	@FXML
	private ChoiceBox<String> choice_Subject;
	@FXML
	private ChoiceBox<String> choice_Test;
	
	/**
	 * ---方法区
	 */
	//3.1 监听button_score
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
	
	//3.2 监听image_search
	public void ShowScoreAnalysis() {
		pane_GradeScore.setExpanded(true);
		ShowGradeScore();
		pane_ClassScore.setExpanded(true);
		ShowClassScore();
	}
	
	//3.2 监听titlePane_GradeScore
	public void ShowGradeScore() {
		//1. 获取数据
		//获取全校所有学生，每次考试的统计量：最高分，最低分，平均分，方差	
		HashMap<String,HashMap<String,DbaGradeScoreEntry>> analysis = DbaService.gradeScoreAnalysis();
		//查询的考试批次
		String test = choice_Test.getSelectionModel().getSelectedItem();
		//该次考试的全校统计分析：最高分，最低分，平均分，方差
		HashMap<String,DbaGradeScoreEntry> testAnalysis = new HashMap<>();
		if(test!=null) {
			testAnalysis = analysis.get(test);
		}

		//2. 展示列表
		ObservableList<DbaGradeScoreEntry> grade = FXCollections.observableArrayList();
		if(testAnalysis!=null) {
			grade.add(testAnalysis.get("最高分"));
			grade.add(testAnalysis.get("最低分"));
			grade.add(testAnalysis.get("平均分"));
			grade.add(testAnalysis.get("方差"));
		}
		
		//3. 配置表格，绑定表格的每列

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
	
		//4. 展示表格
		tab_GradeScore.setItems(grade);
	}
	
	
	//3.3 监听titledPane_ClassScore
	public void ShowClassScore() {
		//1. 获取数据
		/**HashMap<String test, 
		 * 		HashMap<String subject,
		  					HashMap<String classes,DbaClassScoreEntry>>>*/
		HashMap<String, HashMap<String, HashMap<String, DbaClassScoreEntry>>> classAnalysis = DbaService.classScoreAnalysis();
		//获取考试test
		String test = choice_Test.getSelectionModel().getSelectedItem();
		//获取科目
		String subject = choice_Subject.getSelectionModel().getSelectedItem();
		
		//2. 展示列表
		ObservableList<DbaClassScoreEntry> resList = FXCollections.observableArrayList();
		if(test!=null&&subject!=null) {
			HashMap<String, DbaClassScoreEntry> map = classAnalysis.get(test).get(subject);
			ArrayList<DbaClassScoreEntry> result = new ArrayList<>(map.values());
			for(DbaClassScoreEntry e:result) {
				resList.add(e);
			}
		}
		//3. 配置表格，绑定表格的每列
		col_Class.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,String>("classes"));
		col_ClassMean.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Double>("mean"));
		col_ClassMax.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Double>("max"));
		col_ClassMin.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Double>("min"));
		col_ClassStd.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Double>("std"));
		col_ClassCount.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaClassScoreEntry>,Integer>("count"));

		//3. 展示表格
		tab_ClassScore.setItems(resList);
	}

	/**
	 * 4. DutyPane区
	 */
	
	/**
	 * ---控件区
	 */
	@FXML
	private Pane pane_Duty;
	
	//Button区
	@FXML
	private Button button_duty;
	@FXML
	private DatePicker date_Date;
	@FXML
	private DatePicker date_StuDate;
	
	//Table区---TeacherDuty
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

	
	//Table区---StudentDuty
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
	 * ---方法区
	 */
	
	//4.1 监听button_duty
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
	
	//4.2 监听imageview_search
	public void ShowTeacherDuty() {
		//恢复表格
		tab_Duty.setDisable(false);
		tab_Duty.setEditable(true);
		
		//1. 加载老师出勤数据					
		date = date_Date.getValue();
		
		//2. 生成展示列表
		dutyList = DbaService.iniObList(date);
		
		//3.  配置表格，绑定表格的每列
		col_TeacherId.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,Integer>("id"));
		col_TeacherName.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,String>("name"));
		col_TeacherClass.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,String>("classes"));
		col_Morning.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,CheckBox>("morning"));
		col_Noon.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,CheckBox>("noon"));
		col_Evening.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,CheckBox>("evening"));
		col_Note.setCellValueFactory(new PropertyValueFactory<ArrayList<DbaTeacherDutyEntry>,TextField>("note"));

		
		//4. 展示表格
		tab_Duty.setItems(dutyList);
		
	}
	
	//4.2  监听button_SubmitDuty
	public void SubmitTeacherDuty() {
		//1.弹出提示对话框
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("提交出勤记录");
		alert.setContentText("确认提交?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    //2. 更新HashMap<LocalDate,TeacherDutyEntry> teacherDuty
			HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> teacherDuty 
				= DbaService.updateTeacherDuty(dutyList, date);
			//3. 将新的teacherDuty写入TeacherDuty.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/TeacherDuty.txt", teacherDuty);	
			//4. 弹出对话框
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("提交成功");
			alert1.showAndWait();
			//5. 使表格变灰
			tab_Duty.setDisable(true);
		} else {
		   ShowTeacherDuty();
		}

	}

	
	//4.3 按日期展示学生的出勤记录
	public void ShowStudentDuty() {
		//1. 加载全校所有学生的出勤记录
		HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> dutyMap = TeacherService.loadStudentDuty();
		//2. 根据所选日期，获取全校的出勤记录
		LocalDate date = date_StuDate.getValue();
		HashMap<String,HashMap<String,StudentDutyEntry>> dutyDayMap = dutyMap.get(date);
		if(dutyDayMap==null) {
			dutyDayMap = new HashMap<>();
		}
		//3. 将全校的出勤记录提取到一个ArrayList
		ArrayList<StudentDutyEntry> dutyDayList = new ArrayList<>();
		for(Entry<String, HashMap<String, StudentDutyEntry>> e:dutyDayMap.entrySet()) {
			dutyDayList.addAll(e.getValue().values());
		}
		//4. 将其变成表格List
		ObservableList<StudentDutyEntry> stuDutyList = FXCollections.observableArrayList();
		for(StudentDutyEntry sde:dutyDayList) {
			stuDutyList.add(sde);
		}
		
		//5.  配置表格，绑定表格的每列
		col_StuClass.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("classes"));
		col_StuId.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("name"));
		col_StuName.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("id"));
		col_StuDuty.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("duty"));
		col_StuReason.setCellValueFactory(new PropertyValueFactory<ArrayList<StudentDutyEntry>,String>("reason"));
		
		//6. 展示表格
		tab_StudentDuty.setItems(stuDutyList);
	}
	
	
	
	/**
	 * 5. Feedback区
	 */
	/**
	 * ---控件区
	 */
	@FXML
	private Pane pane_Feedback;
	@FXML
	private TitledPane tab_Letters;
	@FXML
	private Accordion pane_Letter;
	
	//Button区
	@FXML
	private Button button_feedback;
	@FXML
	private ImageView image_letter;
	
	/**
	 * ---方法区
	 */
	//5.1  监听button_exit
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
	
	//5.2 监听titledPane_Letters
	public void ShowLetters() {
		//打开titledPane
		tab_Letters.setExpanded(true);
		
		//1. 生成数据
		ArrayList<StudentFeedbackEntry> feedbacks = TeacherService.loadFeedback();
		if(!feedbacks.isEmpty()) {
			//2. 创建控件数组
			TitledPane[] tab_letter = new TitledPane[feedbacks.size()];
			
			//3. 设置Accordion
			for(int i=0;i<feedbacks.size();i++) {
				//3.1 新建TiledPane
				String title = (i+1)+"\t"+feedbacks.get(i).getDate().toString()
						+"\t班级:"+feedbacks.get(i).getClasses()+"\t"+
						feedbacks.get(i).getName()+"\t\t反馈对象:"
						+feedbacks.get(i).getType()+"\t\t标题:"+feedbacks.get(i).getTitle();
				TextArea text =new TextArea(feedbacks.get(i).getContent());
				text.setPromptText("发送内容");
				tab_letter[i] = new TitledPane(title,text);
						
				//3.2 设置内容
				pane_Letter.autosize();
				pane_Letter.getPanes().add(tab_letter[i]);
			
			}
			
		}
		
		//4. 处理按钮
		image_letter.setDisable(true);
	}
	
	/**
	 * 6. Exit区
	 */
	/**
	 * ---控件区
	 */
	//Button区
	@FXML
	private Button button_exit;
	
	/**
	 * ---方法区
	 */
	//6.1  监听button_exit
	public void EnterExit() {
		button_exit.setOpacity(0.38);
	}
	public void ExitExit() {
		button_exit.setOpacity(0.0);
	}
	public void ExitWindow(ActionEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("退出程序");
		alert.setContentText("确认退出?");
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