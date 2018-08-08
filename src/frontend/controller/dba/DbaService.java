package frontend.controller.dba;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import backend.entity.Account;
import backend.entity.Student;
import backend.entity.Teacher;
import backend.entry.dba.DbaClassScoreEntry;
import backend.entry.dba.DbaGradeScoreEntry;
import backend.entry.dba.DbaSearchResultEntry;
import backend.entry.dba.DbaTeacherDutyEntry;
import backend.entry.student.StudentFeedbackEntry;
import backend.entry.student.StudentTestEntry;
import backend.entry.teacher.TeacherDutyEntry;
import frontend.controller.student.StudentService;
import frontend.controller.teacher.TeacherService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * DbaController工具类：全部静态方法
 * 目的：帮助DbaController进行除UI之外的功能实现
 */

public class DbaService {
	//1. 属性区
	
	//2. 方法区
	//方法1：查找账号，根据input展示结果
	public static ArrayList<DbaSearchResultEntry> searchResult(HashMap<Integer,Account> accMap,String input,Label warning){	
		//1. 加载所有Account
		HashMap<Integer,Account> resultMap = new HashMap<>();
		ArrayList<DbaSearchResultEntry> result = new ArrayList<>();
		
		//2. 根据input筛选结果
		try {
			//2.1 什么都不输入，展示所有结果
			if(input.equals("")) {
				resultMap = accMap;
			}else {
				int id = Integer.valueOf(input);
				//2.2 找到指定id的Account
				Account acc = accMap.get(id);			
				if(acc!=null) {
					resultMap.put(acc.getId(),acc);
				}else {
					warning.setText("ID为"+id+"的用户不存在");
					warning.setVisible(true);
				}
			}
			//3. 提取出resultList中每个Account的id,name,usertype
			ArrayList<Account> resultList = new ArrayList<>(resultMap.values());
			for(int i=0;i<resultList.size();i++) {
				ArrayList<String> list = new ArrayList<>();
				list.add(resultList.get(i).getId()+"");
				list.add(resultList.get(i).getuserName());
				list.add(resultList.get(i).getUsertype());
				DbaSearchResultEntry dre = new DbaSearchResultEntry(list);
				
				//2. 设置TextField属性
				dre.getText_id().setEditable(true);
				dre.getText_name().setEditable(true);
				dre.getText_usertype().setEditable(true);
				dre.getButton_delete().setOnMouseClicked((e)->{
					DeleteResult(accMap,dre);
				});
				
				dre.getButton_modify().setOnMouseClicked((e)->{
					ModifyResult(accMap,dre);
				});		
				result.add(dre);
			}	
		}catch(NumberFormatException  e) {
			warning.setText("输入的id格式有误，请重新输入");
			warning.setVisible(true);
		}
		return result;
	}
	
	

	//方法2：监听button_删除：删除用户的操作
	private static void DeleteResult(HashMap<Integer,Account> accMap,DbaSearchResultEntry dre) {
		//1. 弹出对话框，让用户进行确认删除
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("删除账户");
		alert.setContentText("确认删除?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//2. 确认删除
			//2.1 删除账户Account
			deleteAccount(accMap,dre);
			//2.2 删除Teacher或Student
			String usertype = dre.getText_usertype().getText();
			if(usertype.equals("学生")) {
				deleteStudent(dre);			
			}else if(usertype.equals("教师")) {
				deleteTeacher(dre);	
			}			
			//弹出提示对话框
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("账户删除成功");
			alert1.showAndWait();
		}else {
			return;
		}
		//3. UI界面控制
		dre.getText_id().setVisible(false);
		dre.getText_name().setVisible(false);
		dre.getText_usertype().setVisible(false);
		dre.getButton_delete().setVisible(false);
		dre.getButton_modify().setVisible(false);
	}
	
	
	//方法3：监听button_修改：修改用户的操作
	private static void ModifyResult(HashMap<Integer,Account> accMap,DbaSearchResultEntry dre) {
		//1. UI界面控制
		dre.getText_id().setEditable(true);
		dre.getText_name().setEditable(true);
		dre.getText_usertype().setEditable(true);
		
		dre.getText_id().setDisable(false);
		dre.getText_name().setDisable(false);
		dre.getText_usertype().setDisable(false);	
		
		//2. 弹出对话框，让用户选择确认修改
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("修改账户");
		alert.setContentText("确认修改?");
		Optional<ButtonType> result = alert.showAndWait();
		
		//3. 修改用户
		if (result.get() == ButtonType.OK){
			//3.2 考虑是Teacher->Student还是Student->Teacher
			//（1）获取原来的用户类型
			int id = Integer.valueOf(dre.getText_id().getText());
			String oldUsertype = accMap.get(id).getUsertype();
			//（2）获取新修改的用户类型
			String newUsertype = dre.getText_usertype().getText();
			
			//（3）根据新旧用户类型，调用相应函数
			if(oldUsertype.equals(newUsertype)) {
				//3.1 修改Account
				modifyAccount(accMap,dre);
			}else if(oldUsertype.equals("学生")&&newUsertype.equals("教师")) {
				//3.1 修改Account
				modifyAccount(accMap,dre);
				studentToTeacher(dre); //学生->教师
				//弹出提示对话框
				Alert alert1 = new Alert(AlertType.INFORMATION);
				System.out.println("账户修改成功");
				alert1.setContentText("账户修改成功");
				alert1.showAndWait();
			}else if(oldUsertype.equals("教师")&&newUsertype.equals("学生")) {
				//3.1 修改Account
				modifyAccount(accMap,dre);
				teacherToStudent(dre); //教师->学生
				//弹出提示对话框
				System.out.println("账户修改成功");
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setContentText("账户修改成功");
				alert2.showAndWait();
			}else {
				System.out.println("账户类型错误，修改失败");
				//弹出提示对话框
				Alert alert3 = new Alert(AlertType.INFORMATION);
				alert3.setContentText("账户类型错误，修改失败");
				alert3.showAndWait();
			}
		}else {
			return;
		}
	}
	
	
	//方法4：从accList中删除指定Id的用户，并更新Account.txt
	public static void deleteAccount(HashMap<Integer,Account> accMap,DbaSearchResultEntry dre){
		//1. 删除指定id的Account
		int deleteId = Integer.valueOf(dre.getText_id().getText());
		accMap.remove(deleteId);
		//2. 更新Account.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt", accMap);
	}
	
	
	//方法5：从accList中修改指定Id的用户，并更新Account.txt
	public static void modifyAccount(HashMap<Integer,Account> accMap,DbaSearchResultEntry dre){
		//1. 修改指定id的Account
		int modifyId = Integer.valueOf(dre.getText_id().getText());
		//遍历accList，找到该Id对应的Account的index
		Account acc = accMap.get(modifyId);
		
		//2. 新建一个Account，用来替换原来的Account
		String username = dre.getText_name().getText();
		String usertype = dre.getText_usertype().getText();
		
		//3. 更新accList
		if(acc!=null) {
			acc.setId(modifyId);
			acc.setuserName(username);
			acc.setUsertype(usertype);
			accMap.put(modifyId, acc);
		}
	
		//4. 更新Account.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt", accMap);
	}
	
	//方法6：将Student修改为Teacher用户
	public static void studentToTeacher(DbaSearchResultEntry dre) {
		//1. 加载当前teaList,stuList
		HashMap<Integer, Student> stuMap = StudentService.loadStudent();
		HashMap<Integer,Teacher> teaMap = TeacherService.loadTeacher();
		//2. 获取被修改的Student对象
		int stu_id = Integer.valueOf(dre.getText_id().getText());
		Student stu = stuMap.get(stu_id);
		//将其从stuList中移除
		if(stu!=null) {
			stuMap.remove(stu_id);
		}
		
		//3. 将Student对象转化成Teacher
		Teacher teacher = new Teacher(stu);
		//将teacher添加到teaMap
		teaMap.put(stu_id, teacher);
		
		//4. 更新Teachers.txt和Students.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt", stuMap);
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt", teaMap);
	}
	
	//方法7：将Teacher修改为Student用户
	public static void teacherToStudent(DbaSearchResultEntry dre) {
		//1. 加载当前teaList,stuList
		HashMap<Integer, Student> stuMap = StudentService.loadStudent();
		HashMap<Integer,Teacher> teaMap = TeacherService.loadTeacher();
		//2. 获取被修改的Teacher对象
		int tea_id = Integer.valueOf(dre.getText_id().getText());
		Teacher teacher = teaMap.get(tea_id);
		//将其从teaList中移除
		if(teacher!=null) {
			teaMap.remove(tea_id);
		}
		
		//3. 将Teacher对象转化成Student
		Student stu= new Student(teacher);
		//将stu添加到stuList
		stuMap.put(tea_id, stu);
		
		//4. 更新Teachers.txt和Students.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt", stuMap);
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt", teaMap);
	}
	
	
	//方法9：从当前Students.txt中删除指定id的Student对象
	public static void deleteStudent(DbaSearchResultEntry dre) {
		//1. 加载当前的stuList
		HashMap<Integer, Student> stuMap = StudentService.loadStudent();
		//2. 获取当前被删除对象的id
		int stu_id = Integer.valueOf(dre.getText_id().getText());
		//3. 将其从stuList中移除
		stuMap.remove(stu_id);
		//4. 更新Students.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt", stuMap);
	}
	
	//方法9：从当前Teachers.txt中删除指定id的Teacher对象
	public static void deleteTeacher(DbaSearchResultEntry dre) {
		//1. 加载当前的teaList
		HashMap<Integer,Teacher> teaMap = TeacherService.loadTeacher();
		//2. 获取当前被删除对象的id
		int tea_id = Integer.valueOf(dre.getText_id().getText());
		//3. 将其从teaList中移除
		teaMap.remove(tea_id);
		//4. 更新Teachers.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt", teaMap);
	}
	
	//方法10：获取全校考试所有批次考试的全校学生成绩单
	public static HashMap<String,ArrayList<StudentTestEntry>> gradeTranscripts() {
		//1. 加载全校学生的所有考试成绩
		HashMap<Integer,HashMap<String,StudentTestEntry>> stuScoreMap = StudentService.loadStudentScore();
		ArrayList<HashMap<String,StudentTestEntry>> stuTranscripts = new ArrayList<>(stuScoreMap.values());
		//2. 统计所有有成绩的考试批次---作为结果的外层key
		LinkedHashSet<String> tests = new LinkedHashSet<>();
		for(int i=0;i<stuTranscripts.size();i++) {
			tests.addAll(stuTranscripts.get(i).keySet());
		}
		
		//3. 重新构建成绩单：HashMap<String test,ArrayList<StudentTestEntry>>
		HashMap<String,ArrayList<StudentTestEntry>> testScores = new HashMap<>();
		/**key---某次考试；ArrayList---属于该次考试的所有学生成绩*/
		for(String test:tests) {
			//3.1 list用来装该次考试的所有学生成绩
			ArrayList<StudentTestEntry> list = new ArrayList<>();
			//3.2 对于每一次考试，从stuTranscripts找到key=当前test的StudentTestEntry，加入list
			for(HashMap<String,StudentTestEntry> map:stuTranscripts) {
				StudentTestEntry ste = map.get(test);
				if(ste!=null) {
					list.add(ste);
				}
			}
			//3.3 将Entry<String test,ArrayList<StudentTestEntry>>加入最后的结果中
			testScores.put(test, list);
		}
		return testScores;
	}
	
	//方法11：根据全校学生某次考试成绩列表，得到相应的统计量(String类型)---按科目分
	public static HashMap<String,DbaGradeScoreEntry> gradeStatistic
		 (HashMap<String,ArrayList<StudentTestEntry>> testScores, String test) {
		//1. 本次考试全校学生成绩单
		ArrayList<StudentTestEntry> stuScoreList=testScores.get(test);
		//2. 将所有科目+总分的所有学生单科成绩从总表中剥离
		/**这10个ArrayList分别对应：score,chinese,math,english,physics,chemistry,biology,
		 * politics,history,geology*/
		ArrayList<Integer> score = new ArrayList<>();
		ArrayList<Integer> chinese = new ArrayList<>();
		ArrayList<Integer> math = new ArrayList<>();
		ArrayList<Integer> english = new ArrayList<>();
		ArrayList<Integer> physics = new ArrayList<>();
		ArrayList<Integer> chemistry = new ArrayList<>();
		ArrayList<Integer> biology = new ArrayList<>();
		ArrayList<Integer> politics = new ArrayList<>();
		ArrayList<Integer> history = new ArrayList<>();
		ArrayList<Integer> geology = new ArrayList<>();
		
		//3. 对每一个学生，将该学生的单科成绩分别添入到对应的单科成绩列表中
		for(StudentTestEntry e:stuScoreList) {
			score.add(strToInteger(e.getScore()));
			chinese.add(strToInteger(e.getChinese()));
			math.add(strToInteger(e.getMath()));
			english.add(strToInteger(e.getEnglish()));
			physics.add(strToInteger(e.getPhysics()));
			chemistry.add(strToInteger(e.getChemistry()));
			biology.add(strToInteger(e.getBiology()));
			politics.add(strToInteger(e.getPolitics()));
			history.add(strToInteger(e.getHistory()));
			geology.add(strToInteger(e.getGeology()));
		}
		
		//4. 获取每个单科成绩的：最高分，最低分，平均分，方差
		HashMap<String,String> scoreStat = listStatistics(score);
		HashMap<String,String> chineseStat = listStatistics(chinese);
		HashMap<String,String> mathStat = listStatistics(math);
		HashMap<String,String> englishStat = listStatistics(english);
		HashMap<String,String> physicsStat = listStatistics(physics);
		HashMap<String,String> chemistryStat = listStatistics(chemistry);
		HashMap<String,String> biologyStat = listStatistics(biology);
		HashMap<String,String> politicsStat = listStatistics(politics);
		HashMap<String,String> historyStat = listStatistics(history);
		HashMap<String,String> geologyStat = listStatistics(geology);
		
		//5. 返回最高分，最低分，平均分，方差对应的DbaGradeScoreEntry			
		//5.1 统计最高分
		DbaGradeScoreEntry max = new DbaGradeScoreEntry("最高分",scoreStat.get("max")
				,chineseStat.get("max"),mathStat.get("max"),englishStat.get("max"),
				physicsStat.get("max"),chemistryStat.get("max"),biologyStat.get("max"),
				politicsStat.get("max"),historyStat.get("max"),geologyStat.get("max"));
		//5.2 统计最低分
		DbaGradeScoreEntry min = new DbaGradeScoreEntry("最低分",scoreStat.get("min")
				,chineseStat.get("min"),mathStat.get("min"),englishStat.get("min"),
				physicsStat.get("min"),chemistryStat.get("min"),biologyStat.get("min"),
				politicsStat.get("min"),historyStat.get("min"),geologyStat.get("min"));
		//5.3 统计平均分
		DbaGradeScoreEntry mean = new DbaGradeScoreEntry("平均分",scoreStat.get("mean")
				,chineseStat.get("mean"),mathStat.get("mean"),englishStat.get("mean"),
				physicsStat.get("mean"),chemistryStat.get("mean"),biologyStat.get("mean"),
				politicsStat.get("mean"),historyStat.get("mean"),geologyStat.get("mean"));	
		//5.4 统计方差
		DbaGradeScoreEntry std = new DbaGradeScoreEntry("方差",scoreStat.get("std")
				,chineseStat.get("std"),mathStat.get("std"),englishStat.get("std"),
				physicsStat.get("std"),chemistryStat.get("std"),biologyStat.get("std"),
				politicsStat.get("std"),historyStat.get("std"),geologyStat.get("std"));
		
		//6. 返回结果
		HashMap<String,DbaGradeScoreEntry> result = new HashMap<>();
		result.put("最高分", max);
		result.put("最低分", min);
		result.put("平均分", mean);
		result.put("方差", std);
		return result;
	}
	
	public static Integer strToInteger(String str) {
		int num = -1;
		if(str==null) {
			num = 0;
		}else {
			try {
				num = Integer.valueOf(str);
			}catch(NumberFormatException e) {
				num = 0;
			}
		}
		return num;
	}
	
	public static HashMap<String,String> listStatistics(ArrayList<Integer> list) {
		//1. 将ArrayList<Integer> -> Integer[]
		Integer[] array = list.toArray(new Integer[list.size()]);
		int max = array[0];
		int min = array[0];
		int sum = 0;
		double mean = 0.0;
		//2. 遍历数组：求max,min,mean
		for(int i=0;i<array.length;i++) {
			sum += array[i];
			if(max<array[i]) {
				max = array[i];
			}
			if(min>array[i]) {
				min = array[i];
			}
		}
		mean = sum/array.length*1.0;
		
		//3. 遍历数组：求方差
		double std = 0.0;
		for(int i=0;i<array.length;i++) {
			std += (array[i]-mean)*(array[i]-mean);
		}
		
		//4. 返回结果
		HashMap<String,String> map = new HashMap<>();
		map.put("max", max+"");
		map.put("min",min+"");
		map.put("mean", mean+"");
		map.put("std", Math.sqrt(std)+"");
		
		return map;
	}

	
	//方法12：获取全校考试所有批次考试的全校学生成绩单的统计分析：最高分，最低分，平均分，方差
	public static HashMap<String,HashMap<String,DbaGradeScoreEntry>> gradeScoreAnalysis() {
		//1. 加载全校学生所有考试批次的成绩单
		HashMap<String,ArrayList<StudentTestEntry>> testScores = gradeTranscripts();
		LinkedHashSet<String> tests = new LinkedHashSet<>(testScores.keySet());
		/**最后的结果为HashMap<考试批次，HashMap<统计量，DbaGradeScoreEntry>>*/
		HashMap<String,HashMap<String,DbaGradeScoreEntry>> analysis = new HashMap<>();
		
		//2. 按照上面的每一次考试的全校学生成绩，分析出每一次考试的全校学生成绩的统计分析
		for(String test:tests) {
			//获取当前考试所有单科的最高分，最低分，平均分，方差
			HashMap<String,DbaGradeScoreEntry> statistics = gradeStatistic(testScores,test);
			analysis.put(test, statistics);	
		}
		return analysis;
		
	}

	//方法13：返回全校学生所有考试批次的成绩单，按照班级分类
	private static HashMap<String, HashMap<String, ArrayList<StudentTestEntry>>> classTranscripts() {
		/**本函数目的：
		 * 将HashMap<String test,ArrayList<StudentTestEntry>> testScores 
		 * -> HashMap<String test,HashMap<String classes, ArrayList<StudentTestEntry>>> classScores
		 */
		/**最终结果：String 1: test; String 2: classes; ArrayList: 该班所有学生本次考试成绩*/
		HashMap<String,HashMap<String,ArrayList<StudentTestEntry>>> classTestScores = new HashMap<>();
		
		//1.加载全校学生成绩，按考试分类	
		HashMap<String,ArrayList<StudentTestEntry>> testScores = gradeTranscripts();
		//获得key1：test考试
		LinkedHashSet<String> tests = new LinkedHashSet<>(testScores.keySet());
		
		//2. 将testScores ->  HashMap<String test,HashMap<String classes, ArrayList<StudentTestEntry>>> 
		for(String test:tests) {
			//2.1 每次考试，全校所有考生的成绩，未分班
			ArrayList<StudentTestEntry> stuScoreList = new ArrayList<>(testScores.get(test));
			
			//2.2 获得key2：classes班级
			LinkedHashSet<String> classes = new LinkedHashSet<>();
			for(StudentTestEntry ste:stuScoreList) {
				classes.add(ste.getClasses());
			}
			
			//2.3 针对每个班级，遍历全校学生，挑出每个班的学生成绩，组成集合
			//(1) 构建HashMap<String classes, ArrayList<StudentTestEntry>>
			HashMap<String,ArrayList<StudentTestEntry>> stuClassScores = new HashMap<>();
			for(String banji:classes) {
				//(2) 构建属于每个班的成绩集合
				ArrayList<StudentTestEntry> stuClassList = new ArrayList<>();
				//(3)遍历全校学生，将List -> HashMap<classes,List>
				for(StudentTestEntry ste:stuScoreList) {
					//如果该生属于此班,将该生此次考试成绩加入此集合			
					if(ste.getClasses().equals(banji)) {		
						stuClassList.add(ste);
					}
				}
				//(4)将最后的HashMap加入最后的结果中
				stuClassScores.put(banji, stuClassList);
			}
			
			//2.4 更新每次考试的分班成绩
			classTestScores.put(test, stuClassScores);	
		}

		//3. 最后返回结果：全校学生所有考试批次的成绩单，按照班级分类	
		return classTestScores;
	}
	
	//方法14：根据全校学生某次考试成绩列表，得到相应的统计量(String类型)---按班级分
	public static HashMap<String/*classes*/,DbaClassScoreEntry> classStatistic
	 (HashMap<String/*classes*/,ArrayList<StudentTestEntry>> classTestScores, String subject) {
		/**最终结果：返回该科目，每个班的统计信息
		 * HashMap<String,DbaClassScoreEntry>  subStatistics
		 * String classes: 班级
		 * DbaClassScoreEntry：每个班的统计信息，max,min,mean,std,count*/
		HashMap<String,DbaClassScoreEntry>  subStatistics = new HashMap<>();
		
		//1. 获取班级集合
		LinkedHashSet<String> classes = new LinkedHashSet<>(classTestScores.keySet());
		
		//2. 根据每个班级计算该科母subject的统计信息：max,min,mean,std,count
		for(String banji:classes) {
			//2.1 加载该班的所有学生成绩
			ArrayList<StudentTestEntry> stuClassList = classTestScores.get(banji);
			//2.2 将该班所有学生的该科成绩提取出来->ArrayList<Integer> list
			ArrayList<Integer> list = new ArrayList<>();
			for(StudentTestEntry ste:stuClassList) {
				switch(subject){
				case "总分":
					list.add(strToInteger(ste.getScore()));
					break;
				case "语文":
					list.add(strToInteger(ste.getChinese()));
					break;
				case "数学":
					list.add(strToInteger(ste.getMath()));
					break;
				case "英语":
					list.add(strToInteger(ste.getEnglish()));
					break;
				case "物理":
					list.add(strToInteger(ste.getPhysics()));
					break;
				case "化学":
					list.add(strToInteger(ste.getChemistry()));
					break;
				case "生物":
					list.add(strToInteger(ste.getBiology()));
					break;
				case "政治":
					list.add(strToInteger(ste.getPolitics()));
					break;
				case "历史":
					list.add(strToInteger(ste.getHistory()));
					break;
				case "地理":
					list.add(strToInteger(ste.getGeology()));
					break;
				}
					
			}
			//2.3 获取该班，该科成绩的max,min,mean,std,count -> DbaClassScoreEntry
			HashMap<String,String> classStat = listStatistics(list);
			String count = stuClassList.size()+"";
			DbaClassScoreEntry dse = new DbaClassScoreEntry(banji,classStat.get("max"),
					classStat.get("min"),classStat.get("mean"),classStat.get("std"),count);
			
			//2.4 将每个班的统计量信息加入map
			subStatistics.put(banji, dse);
		}
		//3. 返回结果
		return subStatistics;
	}
		
	//方法15：获取每次考试，每个科目的班级统计量分析：	
	public static HashMap<String/*test*/,
						HashMap<String/*subject*/,
							HashMap<String/*classes*/,DbaClassScoreEntry>>> classScoreAnalysis() {
		/**
		 * 最后结果：HashMap<String test,
		 * 				HashMap<String subject,
		 * 					HashMap<String classes,DbaClassScoreEntry>>> 
		 */
		HashMap<String,HashMap<String,HashMap<String,DbaClassScoreEntry>>> analysis = new HashMap<>();
		
		//1. 加载全校学生所有考试批次的成绩单，按照班级分类
		HashMap<String/*test*/,HashMap<String/*classes*/,ArrayList<StudentTestEntry>>> testScores = classTranscripts();
		//所有考试集合
		LinkedHashSet<String> tests = new LinkedHashSet<>(testScores.keySet());
	
		//2. 由分班考试成绩，获取每次考试，每个班级，分科目的四个统计量		
		/** 借用函数HashMap<String,DbaClassScoreEntry>  testStatistic
		 * 	(HashMap<String,ArrayList<StudentTestEntry>> testScores, String test){} 
		 * 思路：我们将每次的test,classes固定，就能得到一个HashMap<String,ArrayList<StudentTestEntry>>	
		 *	双层循环，最后可以得到结果
		 */		 
		for(String test:tests) {
			//2.1 每次考试，全校按班级分的学生成绩map
			HashMap<String/*classes*/,ArrayList<StudentTestEntry>> stuClassScores = testScores.get(test);
			
			//2.2 获得key2：subject科目
			LinkedHashSet<String> subject = new LinkedHashSet<>();		
			subject.add("总分");subject.add("语文");subject.add("数学");subject.add("英语");subject.add("物理");
			subject.add("化学");subject.add("生物");subject.add("政治");subject.add("历史");subject.add("地理");
			//2.3 针对每个班级Map，将其传入方法15
			/**
			 * 参数1: HashMap<String classes,ArrayList<StudentTestEntry>> classTestScore
			 * 参数2: String subject
			 */
			HashMap<String/*subject*/,HashMap<String/*classes*/,DbaClassScoreEntry>> classAnalysis = new HashMap<>();
			for(String sub:subject) {
				HashMap<String/*classes*/,DbaClassScoreEntry> classStats = classStatistic(stuClassScores,sub);
				classAnalysis.put(sub,classStats);
			}
			
			//2.4 更新每次考试的按班级分的统计量
			analysis.put(test, classAnalysis);
		}
		//3. 返回结果
		return analysis;
	}

	
	//方法16：加载老师的出勤记录：TeacherDuty.txt -> Map<LocalDate,Map<Integer id,TeacherDutyEntry>>
	public static HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> loadTeacherDuty() {
		/**
		 * 最终结果：Map<LocalDate 出勤时间,TeacherDutyEntry 老师出勤记录>
		 */
		HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> teacherDuty = new HashMap<>();
		//1. 创建输入流
		ObjectInputStream ois = null;
		/**
		 * 记录注册账号是否可以注册成功：
		 * ---true代表账号不存在,注册成功
		 * ---false代表账号存在，注册失败
		 */
		try {
			//1. 判断文件是否存在
			File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/TeacherDuty.txt");
			if(!f.exists()) {
				f.createNewFile();
			}
			//2. 如果TeacherDuty.txt存在:将其中内容读取出来
			if(f.length() > 0){
				ois = new ObjectInputStream(new FileInputStream(f));
				teacherDuty = (HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> )ois.readObject();
				//输出当前所有日期，所有老师的出勤记录
				if(teacherDuty.isEmpty()) {
					System.out.println("出勤记录为空");
				}else {
					for(Map.Entry<LocalDate,HashMap<Integer,TeacherDutyEntry>> e1:teacherDuty.entrySet()) {
						for(Map.Entry<Integer,TeacherDutyEntry> e2:e1.getValue().entrySet()) {
							System.out.println("时间："+e1.getKey()+"出勤记录:"+e2.getValue());
						}
					}
				}
			}
		}catch(EOFException e) {
			System.out.println("当前列表为空");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ois != null){
					ois.close();
				}
			} catch (IOException e) {
				System.out.println("关闭对象输入流错误");
				e.printStackTrace();
			}
		}
		return teacherDuty;	
	}
	
	
	//方法17：加载DutyPane
	public static ObservableList<DbaTeacherDutyEntry> iniObList(LocalDate date) {
		/**
		 * 最终结果：ObservableList<DbaTeacherDutyEntry> result
		 */
		//1. 初始化：加载所有老师的信息->teaList
		ObservableList<DbaTeacherDutyEntry> result = FXCollections.observableArrayList();
		HashMap<Integer,Teacher> teaMap = TeacherService.loadTeacher();
		ArrayList<Teacher> teaList = new ArrayList<>(teaMap.values());
		//2. 加载所有老师的出勤信息
		HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> teacherDuty=loadTeacherDuty();
		HashMap<Integer,TeacherDutyEntry> dateDuty = teacherDuty.get(date);
		//3. 如果当日没有出勤记录,设置默认选项
		if(dateDuty==null) {
			for(Teacher tea:teaList) {
				result.add(new DbaTeacherDutyEntry(tea.getId(),tea.getName(),tea.getClasses()+""));
			}
		}else {
			ArrayList<TeacherDutyEntry> dutyList = new ArrayList<>(dateDuty.values());
			for(TeacherDutyEntry tde:dutyList) {
				DbaTeacherDutyEntry e = new DbaTeacherDutyEntry(tde.getId(),tde.getName(),tde.getClasses()+"");
				if(tde.isMorning()) {
					e.getMorning().setSelected(true);
				}
				if(tde.isNoon()) {
					e.getNoon().setSelected(true);
				}
				if(tde.isEvening()) {
					e.getEvening().setSelected(true);
				}
				e.getNote().setText(tde.getNote());
				result.add(e);
			}
		}
	
		return result;
	}

	//方法17：更新老师的出勤记录 
	public static HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> updateTeacherDuty
		(ObservableList<DbaTeacherDutyEntry> dutyList,LocalDate date) {
		//1. 加载现有的TeacherDuty.txt
		HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> teacherDuty = loadTeacherDuty();
		//2. 新建一个HashMap<Integer,TeacherDutyEntry>
		HashMap<Integer,TeacherDutyEntry> map = teacherDuty.get(date);
		if(map == null) {
			map = new HashMap<>();
		}
		for(int i=0;i<dutyList.size();i++) {
			TeacherDutyEntry tde = new TeacherDutyEntry();
			tde.setId(dutyList.get(i).getId());
			tde.setClasses(dutyList.get(i).getClasses());
			tde.setName(dutyList.get(i).getName());
			tde.setMorning(dutyList.get(i).getMorning().isSelected());
			tde.setNoon(dutyList.get(i).getNoon().isSelected());
			tde.setEvening(dutyList.get(i).getEvening().isSelected());
			tde.setNote(dutyList.get(i).getNote().getText());
			//更新map
			map.put(dutyList.get(i).getId(),tde);
		}
		//更新teacherDuty 
		teacherDuty.put(date, map);
		return teacherDuty;
		
	}


	
//	//方法15：返回全校学生所有考试批次的成绩单，按照科目分类
//	private static HashMap<String, HashMap<String, ArrayList<StudentTestEntry>>> subjectTranscript() {
//		/**本函数目的：
//		 * 将HashMap<String test,ArrayList<StudentTestEntry>> testScores 
//		 * -> HashMap<String test,HashMap<String subject, ArrayList<StudentTestEntry>>> classScores
//		 */
//		/**最终结果：String 1: test; String 2: subject; ArrayList: 该班所有学生本次考试成绩*/
//		HashMap<String,HashMap<String,ArrayList<StudentTestEntry>>> subjectTestScores = new HashMap<>();
//		
//		//1.加载全校学生成绩，按考试分类	
//		HashMap<String,ArrayList<StudentTestEntry>> testScores = gradeTranscripts();
//		//获得key1：test考试
//		LinkedHashSet<String> tests = new LinkedHashSet<>(testScores.keySet());
//		
//		//2. 将testScores ->  HashMap<String test,HashMap<String classes, ArrayList<StudentTestEntry>>> 
//		for(String test:tests) {
//			//2.1 每次考试，全校所有考生的成绩，未分科
//			ArrayList<StudentTestEntry> stuScoreList = new ArrayList<>(testScores.get(test));
//			
//			//2.2 获得key2：subject科目
//			LinkedHashSet<String> subject = new LinkedHashSet<>();
//			subject.add("总分");subject.add("语文");subject.add("数学");subject.add("英语");subject.add("物理");
//			subject.add("化学");subject.add("生物");subject.add("政治");subject.add("历史");subject.add("地理");
//			//2.3 针对每个科目，遍历全校学生，挑出每个科目的学生成绩集合 ---就是全体学生
//			//(1) 构建HashMap<String classes, ArrayList<StudentTestEntry>>
//			HashMap<String,ArrayList<StudentTestEntry>> stuSubjectScores = new HashMap<>();
//			for(String sub:subject) {
//				stuSubjectScores.put(sub,stuScoreList);
//			}
//			//2.4 更新每次考试的分科目成绩
//			subjectTestScores.put(test, stuSubjectScores);	
//		}
//
//		//3. 最后返回结果：全校学生所有考试批次的成绩单，按照科目分类	
//		return subjectTestScores ;
//	}
}
