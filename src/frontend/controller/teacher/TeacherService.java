package frontend.controller.teacher;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import backend.entity.Account;
import backend.entity.Student;
import backend.entity.Teacher;
import backend.entry.student.HomeworkKey;
import backend.entry.student.StudentDutyEntry;
import backend.entry.student.StudentFeedbackEntry;
import backend.entry.student.StudentHomeworkEntry;
import backend.entry.student.StudentTestEntry;
import frontend.controller.login.LoginController;
import frontend.controller.student.StudentService;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * TeacherController工具类：全部静态方法
 * 目的：帮助TeacherController进行除UI之外的功能实现
 */
public class TeacherService {
	
	//方法1：加载Teacher.txt -> ArrayList<Teacher> teaMap
	public static HashMap<Integer,Teacher> loadTeacher(){
		//1. 定义变量
		HashMap<Integer,Teacher> teaMap = new HashMap<>();
		ObjectInputStream ois = null;
		//2. 创建文件对象
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//3. 判断文件是否为空
		if(f.length()>0) {
			try {
				ois =  new ObjectInputStream(new FileInputStream(f));
				teaMap = (HashMap<Integer,Teacher>)ois.readObject();
			}catch(EOFException e) {
				System.out.println("当前列表为空");
			}  catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}finally {
				if(ois!=null) {
					try {
						ois.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		return teaMap;
	}
	
	//方法2：找到当前id对应的Account
	public static Account findAccount(HashMap<Integer,Account> accMap) {
		return accMap.get(LoginController.id);
	}
	
	//方法3：找到当前id对应的Teacher对象的索引
	public static Teacher findTeacher(HashMap<Integer,Teacher> teaMap) {
		
		return teaMap.get(LoginController.id);
	}
	
	//方法4：返回当前id的Teacher对应的学生Id的集合
	public static ArrayList<Integer> loadStudentId(Teacher teacher){
		//1. 先从学生库Students.txt中提取全校所有学生对象集合
		HashMap<Integer,Student> stuMap = StudentService.loadStudent();
		//2. 获取班级属于当前老师班级的所有学生的id
		ArrayList<Integer> stuIdList = new ArrayList<Integer>();
		//获取所有学生对象
		ArrayList<Student> stuList = new ArrayList<>(stuMap.values());
		for(int i=0;i<stuList.size();i++) {
			if(stuList.get(i).getClasses()==teacher.getClasses()) {
				stuIdList.add(stuList.get(i).getId());
			}
		}
		//3. 返回结果
		return stuIdList;
	}
	
	//方法5：加载当前老师对应的所有学生的姓名集合
	public static ArrayList<String> loadStudentName(Teacher teacher){
		//1. 先从学生库Students.txt中提取全校所有学生对象集合
		HashMap<Integer,Student> stuMap = StudentService.loadStudent();
		//2. 获取班级属于当前老师班级的所有学生的姓名
		ArrayList<String> stuNameList = new ArrayList<>();
		//获取所有学生对象
		ArrayList<Student> stuList = new ArrayList<>(stuMap.values());
		for(int i=0;i<stuList.size();i++) {
			if(stuList.get(i).getClasses()==teacher.getClasses()) {
				stuNameList.add(stuList.get(i).getName());
			}
		}
		//3. 返回结果
		return stuNameList;
	}
		

	//方法7：老师将某次考试的全班成绩录入全校成绩库
	public static HashMap<Integer,HashMap<String,StudentTestEntry>> updateStudentScore
		(HashMap<Integer,HashMap<String,StudentTestEntry>> stuScoreMap,TextField[][] text,
				ChoiceBox<String> choice_Test, String classes){
						
		//1. 提取TextField[][]和ChoiceBox中的数据
		String[][] scores = new String[text.length][13];
		for(int i=0;i<scores.length;i++) {
			for(int j=0;j<13;j++) {
				scores[i][j] = text[i][j].getText();
			}
		}
		String test = (String) choice_Test.getSelectionModel().getSelectedItem();
		
		
		//2. 按照test更新全班学生的当次考试成绩
		
		//外层循环：第i个学生
		//int id = -1;
		//String name ="";
		//HashMap<String,StudentTestEntry> transcript = new HashMap<>();
		for(int i=0;i<scores.length;i++) {
			//2.1 获取第i个学生的id
			int id = Integer.valueOf(scores[i][0]);
			String name = scores[i][1];
			//2.2 更新第i个学生的成绩单中的当此考试test成绩
			StudentTestEntry testEntry = new StudentTestEntry(
				id,name,classes,test,scores[i][2],scores[i][3],scores[i][4],scores[i][5],scores[i][6]
				,scores[i][7],scores[i][8],scores[i][9],scores[i][10],scores[i][11],scores[i][12]);
			//2.3 将此次考试成绩加入该生的成绩单中
			/*获取该生的成绩单*/
			HashMap<String,StudentTestEntry> transcript = stuScoreMap.get(id);
			if(transcript==null) {
				transcript = new HashMap<>();
			}
			
			//2.4 利用hashmap不能重复的机制，更新全校学生的成绩单
			transcript.put(test, testEntry);
			stuScoreMap.put(id,transcript);
		}
		
		//3. 输出全校所有学生的成绩单
		//全校学生的成绩单
		if(!stuScoreMap.isEmpty()) {
			ArrayList<Entry<Integer, HashMap<String, StudentTestEntry>>> stuScoreList 
			= new ArrayList<>(stuScoreMap.entrySet());
			for(int i=0;i<stuScoreList.size();i++) {
				HashMap<String, StudentTestEntry> map = stuScoreList.get(i).getValue();
				ArrayList<Entry<String, StudentTestEntry>> list= new ArrayList<>(map.entrySet());
				for(int j=0;j<list.size();j++) {
					System.out.println("ID:"+stuScoreList.get(i).getKey()+"成绩："+list.get(j).getValue());
				}
			}
		}
		return stuScoreMap;
	}
	
	
	//方法8：更新当前数据库：Map->.txt
	public static void updateData(String file,Map map){
		ObjectOutputStream oos = null;
		File f = new File(file);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(map);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(oos!=null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
		
	//方法9：老师将某科某次作业填入全校作业库
	public static HashMap<Integer, HashMap<HomeworkKey, StudentHomeworkEntry>> updateStudentHomework(
			HashMap<Integer, HashMap<HomeworkKey, StudentHomeworkEntry>> stuHomeworkMap, TextField[][] text_Homework,
			ChoiceBox<String> choice_Homework, ChoiceBox<String> choice_Subject,TextArea text_Knowledge) {
		
		//1. 提取TextField[][]和ChoiceBox中的数据
		String[][] grade = new String[text_Homework.length][4];
		for(int i=0;i<grade.length;i++) {
			for(int j=0;j<4;j++) {
				grade[i][j] = text_Homework[i][j].getText();
			}
		}
		String homework= (String) choice_Homework.getSelectionModel().getSelectedItem();
		String subject = (String) choice_Subject.getSelectionModel().getSelectedItem();
		String knowledge = text_Knowledge.getText();
		//2. 按照homework,subject更新此次学生的作业单
		//外层循环：第i个学生

		for(int i=0;i<grade.length;i++) {
			//2.1 获取第i个学生的id
			int id = Integer.valueOf(grade[i][0]);
			String name = grade[i][1];
			//2.2 更新第i个学生的成绩单中的当此考试test成绩
			StudentHomeworkEntry homeworkEntry = new StudentHomeworkEntry(
				id,name,homework,subject,grade[i][2],knowledge,grade[i][3]);
			//2.3 将此次考试成绩加入该生的成绩单中
			HashMap<HomeworkKey,StudentHomeworkEntry> transcript = stuHomeworkMap.get(id);
			if(transcript==null) {
				transcript = new HashMap<>();
			}
			HomeworkKey hk = new HomeworkKey(homework,subject);
			transcript.put(hk, homeworkEntry);
			
			//2.4 利用hashmap不能重复的机制，更新全校学生的成绩单

			stuHomeworkMap.put(id,transcript);
		}
		//3. 输出全校所有学生的作业
		//全校学生的作业
		if(!stuHomeworkMap.isEmpty()) {
			ArrayList<Entry<Integer, HashMap<HomeworkKey, StudentHomeworkEntry>>> stuHomeworkList 
			= new ArrayList<>(stuHomeworkMap.entrySet());
			for(int i=0;i<stuHomeworkList.size();i++) {
				HashMap<HomeworkKey, StudentHomeworkEntry> map = stuHomeworkList.get(i).getValue();
				ArrayList<Entry<HomeworkKey, StudentHomeworkEntry>> list= new ArrayList<>(map.entrySet());
				for(int j=0;j<list.size();j++) {
					System.out.println("ID："+stuHomeworkList.get(i).getKey()+"作业："+list.get(j).getValue());
				}
			}
		}		
		return stuHomeworkMap;
		
	}
	
	//方法10：加载全校学生的意见（包括对校长和老师）
	public static ArrayList<StudentFeedbackEntry> loadFeedback(){
		//1. 定义变量
		ObjectInputStream ois = null;
		ArrayList<StudentFeedbackEntry> feedbackList = new ArrayList<>();
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentFeedback.txt");
		//2. 判断文件是否存在
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//3. StudentFeedback.txt-> feedbackList
		try {
			if(f.length()>0) {
				ois = new ObjectInputStream(new FileInputStream(f));
				feedbackList = (ArrayList<StudentFeedbackEntry>)ois.readObject();
			}else {
				System.out.println("当前列表为空");
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
			if(ois!=null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return feedbackList;
		
	}
	
	//方法11： 加载该班学生对该id老师的意见
	public static ArrayList<StudentFeedbackEntry> loadTeacherFeedback(Teacher teacher){
		//1. 加载全校学生的意见
		ArrayList<StudentFeedbackEntry> feedbacks = loadFeedback();
		ArrayList<StudentFeedbackEntry> teaFeedback = new ArrayList<>();
		//2. 从中跳出属于该id老师的班级的学生反馈
		for(int i=0;i<feedbacks.size();i++) {
			if(feedbacks.get(i).getClasses()==teacher.getClasses()
					&&feedbacks.get(i).getType().equals("教师")){
				teaFeedback.add(feedbacks.get(i));
			}
		}
		//3. 返回结果
		return teaFeedback;
	}
	
	//方法12：初始化GridPane-Score
	public static void initializeScorePane(HashMap<Integer,HashMap<String,StudentTestEntry>> stuScoreMap,
			TextField[][] text_Score,ArrayList<Integer> stuIdList
			,ArrayList<String> stuNameList,String test) {
		
		for(int i=0;i<stuIdList.size();i++) {
			//1. 设置id列
			//获取第i个学生的id
			text_Score[i][0].setText(stuIdList.get(i)+"");
			text_Score[i][0].setEditable(false);
			text_Score[i][0].setAlignment(Pos.CENTER);
			text_Score[i][0].setMaxWidth(100);
			//2. 设置name列
			text_Score[i][1].setText(stuNameList.get(i));
			text_Score[i][1].setEditable(false);
			text_Score[i][1].setMaxWidth(100);
			text_Score[i][1].setAlignment(Pos.CENTER);
			
			//3. 设置分数列
			for(int j=2;j<13;j++) {
				/*获取该Id学生第test次考试成绩*/
				if(!stuScoreMap.isEmpty()) {
					if(stuScoreMap.get(stuIdList.get(i))!=null) {
						StudentTestEntry testScore =stuScoreMap.get(stuIdList.get(i)).get(test);
						/*获取具体某科成绩*/
						if(testScore!=null) {
							text_Score[i][j].setText(getStudentSubjectScore(testScore, j));
						}
					}
				}else {
					text_Score[i][j].setText("");
				}
				
				//4. 设置文本框格式
				text_Score[i][j].setAlignment(Pos.CENTER);
				if(j!=12) {
					text_Score[i][j].setMaxWidth(38);
				}else {
					text_Score[i][j].setMaxWidth(45);
				}
			}
		}
	}
	
	public static String getStudentSubjectScore(StudentTestEntry testScore,int j) {		
		//1. 从全校学生成绩中挑选出该id学生的某次Test成绩
		
		String res = null;
		//2. 返回某科成绩
		switch(j) {
			case 2:
				res = testScore.getScore();
				break;
			case 3:
				res =  testScore.getChinese();
				break;
			case 4:
				res = testScore.getMath();
				break;
			case 5:
				res =  testScore.getEnglish();
				break;
			case 6:
				res = testScore.getPhysics();
				break;
			case 7:
				res = testScore.getChemistry();
				break;
			case 8:
				res = testScore.getBiology();
				break;
			case 9:
				res =  testScore.getPolitics();
				break;
			case 10:
				res =  testScore.getHistory();
				break;
			case 11:
				res =  testScore.getGeology();
				break;
			case 12:
				res = testScore.getRank();
				break;
				
		}
		return res;
	}
	
	//方法13：初始化GridPane-Homework
	public static void initializeHomeworkPane(HashMap<Integer, HashMap<HomeworkKey, StudentHomeworkEntry>> stuHomeworkMap,
			TextField[][] text_Homework, ArrayList<Integer> stuIdList,
			ArrayList<String> stuNameList, String homework, String subject) {
		
		for(int i=0;i<stuIdList.size();i++) {		
			//3.1 设置id列
			//获取第i个学生的id
			text_Homework[i][0].setText(String.valueOf(stuIdList.get(i)));
			text_Homework[i][0].setEditable(false);
			text_Homework[i][0].setAlignment(Pos.CENTER);
			text_Homework[i][0].setMaxWidth(100);
			//3.2 设置name列
			text_Homework[i][1].setText(stuNameList.get(i));
			text_Homework[i][1].setEditable(false);
			text_Homework[i][1].setMaxWidth(100);
			text_Homework[i][1].setAlignment(Pos.CENTER);

			//3.3 设置得分列.批语列
			/*获取该id学生，该homework该subject的得分，批语*/
			HomeworkKey hk = new HomeworkKey(homework,subject);
			if(!stuHomeworkMap.isEmpty()) {
				if(stuHomeworkMap.get(stuIdList.get(i))!=null) {
					StudentHomeworkEntry she = stuHomeworkMap.get(stuIdList.get(i)).get(hk);
					if(she!=null) {
						text_Homework[i][2].setText(she.getScore());
						text_Homework[i][3].setText(she.getComment());
					}
				}
			}else {			
				text_Homework[i][2].setText("");
				text_Homework[i][3].setText("");
			}
			
			//3.4 设置文本框格式
			text_Homework[i][2].setAlignment(Pos.CENTER);
			text_Homework[i][2].setMaxWidth(80);	
			
			text_Homework[i][3].setAlignment(Pos.CENTER);
			text_Homework[i][3].setMaxWidth(300);
			text_Homework[i][3].setPrefWidth(290);
			
		}
		
	}
	
	//方法14：加载所有学生的出勤信息: 
	public static HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> loadStudentDuty() {
		/**最终结果：
		 * HashMap<LocalDate date,
		 * 		HashMap<String class,
		 * 			HashMap<Integer id,StudentDutyEntry>>*/		
		//1. 定义变量
		ObjectInputStream ois = null;
		HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> dutyMap = new HashMap<>();
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentDuty.txt");
		//2. 判断文件是否存在
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//3. StudentDuty.txt-> dutyList
		try {
			if(f.length()>0) {
				ois = new ObjectInputStream(new FileInputStream(f));
				dutyMap = (HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>>)ois.readObject();
				//输出全校全体学生的出勤记录
				for(Map.Entry<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> m1:dutyMap.entrySet()) {
					for(Map.Entry<String,HashMap<String,StudentDutyEntry>> m2:m1.getValue().entrySet()) {
						for(Map.Entry<String,StudentDutyEntry> m3:m2.getValue().entrySet()) {
							System.out.println("日期："+m1.getKey()+"，班级："+m2.getKey()+"，出勤记录:"+m3.getValue());
						}
					}
				}
			}else {
				System.out.println("当前列表为空");
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
			if(ois!=null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dutyMap;
	}
	
	
	//方法15：更新全校学生的出勤记录
	public static HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> updateStudentDuty(StudentDutyEntry sde){
			
		/**最终结果：
		 * HashMap<LocalDate date,
		 * 		HashMap<Integer class,
		 * 			HashMap<Integer id,StudentDutyEntry>>*/	
		//1. 加载全校学生的出勤记录
		HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> dutyMap = loadStudentDuty();
		//2. 根据日期，获取当日全校学生的出勤记录
		HashMap<String,HashMap<String,StudentDutyEntry>> dayDutyMap = dutyMap.get(sde.getDate());													
		if(dayDutyMap==null) {
			dayDutyMap = new HashMap<>();
		}
		//2.1根据班级，获取当日，该班的全体学生出勤记录
		HashMap<String,StudentDutyEntry> classDutyMap = dayDutyMap.get(sde.getClasses());
		if(classDutyMap==null) {
			classDutyMap = new HashMap<>();
		}
		//2.2 根据传进来的学生出勤记录StudentDutyEntry sde更新全部出勤记录
		classDutyMap.put(sde.getId(),sde);
		//2.3 更新全校出勤记录
		dayDutyMap.put(sde.getClasses(), classDutyMap);
		//2.4 更新全部记录
		dutyMap.put(sde.getDate(), dayDutyMap);
		return dutyMap;
	}
}
