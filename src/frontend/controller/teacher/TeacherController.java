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
	//1. 属性区
	private Teacher teacher;
	private Account account;
	private ArrayList<Integer> stuIdList; //该老师对应的班级所有学生id的集合
	HashMap<Integer,Account> accMap = new HashMap<>();
	HashMap<Integer,Teacher> teaMap = new HashMap<>();
	private HashMap<Integer,HashMap<String,StudentTestEntry>> stuScoreMap;//全校所有学生成绩单
	private HashMap<Integer,HashMap<HomeworkKey,StudentHomeworkEntry>> stuHomeworkMap;//全校所有学生作业表
	ObservableList<StudentDutyEntry> dutyList = FXCollections.observableArrayList();
	
	private TextField[][] text_Score;
	private TextField[][] text_Homework;
	
	//2. 构造方法
	public TeacherController() {}
		
	//3. 方法区
	//方法1：初始化
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//1. 初始化数据
		//Account.txt->accList
		accMap= StudentService.loadAccount();
		//Teacher.txt -> teaList
		teaMap = TeacherService.loadTeacher();
		//初始化Teacher对应的Account
		account = TeacherService.findAccount(accMap);
		//初始化Teacher对象
		teacher = TeacherService.findTeacher(teaMap);
		//Students.txt->stuList
		stuIdList = TeacherService.loadStudentId(teacher);
//		for(int i=0;i<stuIdList.size();i++) {
//			System.out.println(stuIdList.get(i));
//		}
		//初始化全校所有学生的成绩单
		stuScoreMap = StudentService.loadStudentScore();
		//初始化教师对应的学生作业表
		stuHomeworkMap = StudentService.loadStudentHomework();
		//2. 初始化个人信息页
		initializeProfile();
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
		pane_Profile.setVisible(false);
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
	 * 2. ProfilePane区
	 */
	
	/**
	 * ---控件区
	 */	
	//Label区
	@FXML
	private Label hint;
	@FXML
	private Label infoWarning;
	@FXML
	private Pane pane_Profile;
	
	//Button区
	@FXML
	private Button button_profile;
	
	//TextField区
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
	 * ---方法区
	 */
	
	//2.1 监听主菜单buttons
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
		
		
	//2.2 调整UI控件
	public void initializeProfile() {
		//1. 加载老师基本信息

		text_name.setPromptText(teacher.getName());
		text_gender.setPromptText(teacher.getGender());
		text_classes.setPromptText(String.valueOf(teacher.getClasses()));		
		text_id.setPromptText(String.valueOf(teacher.getId()));
		text_phone.setPromptText(teacher.getPhone());
		text_address.setPromptText(teacher.getHomeAddress());
		text_contact_name.setPromptText(teacher.getContactsName());		
		text_contact_phone.setPromptText(teacher.getContactsPhone());
		
		//2. 设置文本框初始状态为灰色
		text_name.setDisable(true);
		text_gender.setDisable(true);
		text_classes.setDisable(true);
		text_id.setDisable(true);
		text_phone.setDisable(true);
		text_address.setDisable(true);
		text_contact_name.setDisable(true);
		text_contact_phone.setDisable(true);
				
	}
	
	
	//2.3  监听按钮button_revise_profile---修改用户信息
	public void ReviseProfile() {
		infoWarning.setText("");;
		//不能修改id号
		text_name.setDisable(false);
		text_gender.setDisable(false);
		text_classes.setDisable(false);
		text_phone.setDisable(false);
		text_address.setDisable(false);
		text_contact_name.setDisable(false);
		text_contact_phone.setDisable(false);
		
	}
	
	
	//2.4 监听按钮button_revise_account
	public void ReviseAccount() {
		hint.setText("");
		text_oldPassword.setDisable(false);
		text_newPassword.setDisable(false);
		
	}
	
	//2.5 监听按钮button_submit_profile
	public void SubmitProfile() {
		//1. 记录修改信息
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
			
			//2. 更新stuList里的Student对象
			teaMap.put(teacher.getId(),teacher);
			
			//3. 更新teaList -> Teachers.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt", teaMap);
			System.out.println("Teacher对象修改成功");
			
		}catch(NumberFormatException e) {
			infoWarning.setText("输入的班级信息格式有误，请重新输入");
			infoWarning.setVisible(true);
		}
		
		//4. 弹出提示对话框
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("个人信息修改成功");
		alert.showAndWait();	
	}
	
	//2.6 监听按钮button_submit_account
	public void SubmitAccount() {
		//1. 判断---如果输入的密码和原始密码相同：修改密码
		if(account.getPassword().equals(text_oldPassword.getText())){
			if(text_newPassword.getText().length() < 6){
				hint.setText("密码长度需大于等于6位");	
			}else {
				account.setPassword(text_newPassword.getText());
				hint.setText("密码修改成功");
				//弹出提示对话框
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("账户修改成功");
				alert.showAndWait();
			}
			//3. 更新accList里的Account对象
			accMap.put(account.getId(), account);
			//4. 更新重新写入修改后的密码
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt", accMap);
			
		}else{
			hint.setText("密码不正确，请输入正确密码");
		}
		
		//3. 调整文本框状态为不可选
		text_oldPassword.setDisable(true);
		text_newPassword.setDisable(true);
		
		//弹出提示对话框
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("密码修改成功");
		alert.showAndWait();
	}
	
	
	
	
	
	/**
	 * 3. ScorePane区
	 */
	/**
	 * ---控件区
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
	
	//Button区
	@FXML
	private Button button_score;
	
	//Choice_Box区
	@FXML
	private ChoiceBox<String> choice_Test;
	@FXML
	private ChoiceBox<String> choice_Homework;
	@FXML
	private ChoiceBox<String> choice_Subject;
	
	
	
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
		pane_Profile.setVisible(false);
		pane_Score.setVisible(true);
		pane_Duty.setVisible(false);
		pane_Feedback.setVisible(false);
		
		//设置选择框选项
		choice_Test.setItems(FXCollections.observableArrayList("第一次月考","第二次月考","第三次月考"
				,"第四次月考","第五次月考","第六次月考","第七次月考","第八次月考","第九次月考","第十次月考"));
		choice_Homework.setItems(FXCollections.observableArrayList("第一次作业","第二次作业","第三次作业"
				,"第四次作业","第五次作业","第六次作业","第七次作业","第八次作业","第九次作业","第十次作业"));
		choice_Subject.setItems(FXCollections.observableArrayList("语文","数学","英语","物理",
				"化学","生物","政治","历史","地理"));
		text_Banji.setText(teacher.getClasses()+"");
				
	}
	
	//3.2 录入学生成绩---监听titlegridpane_ScoreScore
	public void ImportScore() {
		//1. 生成数据
		/**
		 * 当前数据为：
		 * stuScoreMap: HashMap<Integer,HashMap<String,StudentTestEntry>>
		 * 为了方便使用，我们提取出EntrySet<Integer,HashMap<String,StudentTestEntry>>
		 */
		//获取所有学生姓名
		ArrayList<String> stuNameList = TeacherService.loadStudentName(teacher);
		
		//2.设置GridPane属性：gridpane_Score的行列数：行-stuScoreMap.size()  列-13
		gridpane_Score.setDisable(false);
		gridpane_Score.setGridLinesVisible(false);
		gridpane_Score.setVgap(3);
		int n = stuIdList.size(); //该老师班上的学生个数 
		text_Score = new TextField[n][13];
		String test = choice_Test.getSelectionModel().getSelectedItem();

	    //3. 添加单元格
		for(int i=0;i<n;i++) {
			for(int j=0;j<13;j++) {
				text_Score[i][j] = new TextField("");
				text_Score[i][j].setAlignment(Pos.CENTER);
				gridpane_Score.add(text_Score[i][j], j, i);
			}
		}
		
		//4. 初始化单元格中的数据
		TeacherService.initializeScorePane(stuScoreMap, text_Score,
				stuIdList, stuNameList, test);
		
		
	}
	
	
	//3.4 提交学生成绩---监听button提交成绩
	public void SubmitScore() {
		//弹出确认对话框
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("提交成绩");
		alert.setContentText("确认提交?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//1. 更新全校成绩单
			/**
			 * 将TextField[][] text_Score，choice_Test test 第几次考试 作为参数传入函数，
			 * 得到该班所有学生第几次考试的成绩
			 */
			stuScoreMap = TeacherService.updateStudentScore(stuScoreMap,text_Score,choice_Test,teacher.getClasses()+"");
			//2. 将新的成绩单写入.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentScore.txt",stuScoreMap);
			//3. UI界面调整
			gridpane_Score.setDisable(true);
			//弹出提示对话框
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("提交成功");
			alert1.showAndWait();
		}else {
			return;
		}

	}
	
	
	//3.4  批改学生作业---监听button批改
	public void ImportHomework() {
		//1. 生成数据
		/**
		 * 当前数据为：
		 * stuHomeworkMap: HashMap<Integer,HashMap<String,StudentHomeworkEntry>>
		 * 为了方便使用，我们提取出EntrySet<Integer,HashMap<String,StudentTestEntry>>
		 */
		//获取所有学生姓名
		ArrayList<String> stuNameList = TeacherService.loadStudentName(teacher);
		
		//2.设置GridPane属性：gridpane_Score的行列数：行-id.size()  列-4
		pane_Homework.setDisable(false);
		text_Knowledge.setDisable(false);
		pane_Homework.setGridLinesVisible(false);
		pane_Homework.setVgap(3);
		int n = stuIdList.size(); //该老师班上的学生个数 
		text_Homework= new TextField[n][4];
		String homework = choice_Homework.getSelectionModel().getSelectedItem();
		String subject = choice_Subject.getSelectionModel().getSelectedItem();
		
		//3. 添加单元格
		for(int i=0;i<n;i++) {		
			//3.1 设置id列
			//获取第i个学生的id
			text_Homework[i][0] = new TextField("");

			//3.2 设置name列
			text_Homework[i][1] = new TextField("");

			//3.3 设置得分列
			text_Homework[i][2] = new TextField("");
			
			//3.4 设置批语列
			text_Homework[i][3] = new TextField("");
			
			//3.5 添加按钮到GridPane
			pane_Homework.add(text_Homework[i][0],0,i);
			pane_Homework.add(text_Homework[i][1],1,i);
			pane_Homework.add(text_Homework[i][2],2,i);
			pane_Homework.add(text_Homework[i][3],3,i);
		}
		
	    //4. 初始化单元格
		TeacherService.initializeHomeworkPane(stuHomeworkMap, text_Homework,
				stuIdList, stuNameList,homework,subject);
		
		
	}
	
	
	//3.5  提交学生作业---监听button_SubmitHomework
	public void SubmitHomework() {
		//1. 弹出确认对话框
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("提交作业");
		alert.setContentText("确认提交?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//1. 更新全校成绩单
			/**
			 * 将TextField[][] text_Homework，choice_Homworkt choice_Subject 作为参数传入函数，
			 * 得到该班所有学生subject科目，homework的得分表
			 */
			stuHomeworkMap = TeacherService.updateStudentHomework(stuHomeworkMap,
					text_Homework,choice_Homework,choice_Subject,text_Knowledge);
			//2. 将新的作业表写入.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentHomework.txt",stuHomeworkMap);
			//4. 调整UI界面
			text_Knowledge.setDisable(true);
			pane_Homework.setDisable(true);
			//弹出提示对话框
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("提交成功");
			alert1.showAndWait();
		}else {
			return;
		}	
	}
	
	
	
	/**
	 * 4. DutyPane区
	 */
	
	/**
	 * ---控件区
	 */
	@FXML
	private Pane pane_Duty;
	
	//TextField区
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

	//Button区
	@FXML
	private Button button_duty;
	
	
	//Table区
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
	 * ---方法区
	 */
	
	//4.1 监听button_duty
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
		
		//固定班级
		text_Class.setText(teacher.getClasses()+"");
		
		//展示该班学生历史出勤记录
		ShowDuty(TeacherService.loadStudentDuty());
	}
	
	//4.2 监听button_AddDuty()
	public void AddDuty() {
		//3. 将按钮变白
		text_Id.setDisable(false);
		text_Name.setDisable(false);
		text_Duty.setDisable(false);
		text_Reason.setDisable(false);
	}
	
	
	//4.3 监听button_SubmitDuty
	public void SubmitDuty() {
		//1. 利用输入的出勤信息，生成一条DutyEntry
		LocalDate date = date_Date.getValue();
		String id = text_Id.getText();
		String banji = text_Class.getText();
		String name = text_Name.getText();
		String duty = text_Duty.getText();
		String reason = text_Reason.getText();
		StudentDutyEntry sde = new StudentDutyEntry(date,id,banji,name,duty,reason);
		
		//2.弹出提示对话框
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("提交出勤记录");
		alert.setContentText("确认提交?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//2.1 更新全校dutyMap
			HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>>
			dutyMap = TeacherService.updateStudentDuty(sde);
			//2.2 将新的dutyMap写入.txt
			TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentDuty.txt", dutyMap);
			//2.3 将出勤信息显示在下面的table中
			ShowDuty(dutyMap);
			//2.4 弹出提示对话框
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("提交成功");
			alert1.showAndWait();
			//2.5 将按钮变灰
			text_Id.setDisable(true);
			text_Name.setDisable(true);
			text_Duty.setDisable(true);
			text_Reason.setDisable(true);
		}
	}

	public void ShowDuty(HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> dutyMap) {
		//1. 根据date，和班级，展示该班的出勤记录
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
	
		//2. 生成表格列表
		for(StudentDutyEntry sde:classDutyList) {
			dutyList.add(sde);
		}
	
		//2.  配置表格，绑定表格的每列
		col_date.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,LocalDate>("date"));
		col_id.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("id"));
		col_class.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("banji"));
		col_name.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("name"));
		col_duty.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("duty"));
		col_reason.setCellValueFactory(new PropertyValueFactory<ArrayList<TeacherDutyEntry>,String>("reason"));
		
		//3. 展示表格
		tab_Duty.setItems(dutyList);
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
		pane_Profile.setVisible(false);
		pane_Score.setVisible(false);
		pane_Duty.setVisible(false);
		pane_Feedback.setVisible(true);
	}
	
	//5.2 监听titledPane_Letters: 获取学生对老师的反馈
	public void ShowLetters() {
		//1. 打开titledPane
		tab_Letters.setExpanded(true);
		
		//1. 生成数据
		ArrayList<StudentFeedbackEntry> feedbacks = TeacherService.loadTeacherFeedback(teacher);
		if(!feedbacks.isEmpty()) {
			//2. 创建控件数组
			TitledPane[] tab_letter = new TitledPane[feedbacks.size()];
			
			//3. 设置Accordion
			for(int i=0;i<feedbacks.size();i++) {
				//3.1 新建TiledPane
				String title = (i+1)+"\t"+feedbacks.get(i).getDate().toString()
					+"\t"+feedbacks.get(i).getName()+"\t\t标题:"+feedbacks.get(i).getTitle();
			
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