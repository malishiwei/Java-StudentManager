package frontend.controller.student;


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
import java.util.List;


import backend.entity.Account;
import backend.entity.Student;
import backend.entry.student.HomeworkKey;
import backend.entry.student.StudentFeedbackEntry;
import backend.entry.student.StudentHomeworkEntry;
import backend.entry.student.StudentTestEntry;
import frontend.controller.login.LoginController;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;



/**
 * StudentController工具类：全部静态方法
 * 目的：帮助StudentController进行除UI之外的功能实现
 */
public class StudentService {
	
	//方法1：加载当前学生数据库Students.txt->stuMap
	public static HashMap<Integer,Student> loadStudent() {
		//1. 定义变量并创建输入流
		ObjectInputStream ois = null;
		HashMap<Integer,Student> stuMap = new HashMap<Integer,Student>();
		//2. 将.txt -> Map
		try {
			ois = new ObjectInputStream(new FileInputStream("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt"));
			stuMap = (HashMap<Integer,Student>)ois.readObject();
		}catch (FileNotFoundException e) {
				e.printStackTrace();
		}catch(EOFException e) {
			System.out.println("当前用户列表为空");
		}  catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stuMap;
	}
	
	
	//方法2：从学生数据库获取当前学生对象的index---stuList的index
	public static Student findStudent(HashMap<Integer,Student> stuMap){
		//1. 寻找学生对象
		return stuMap.get(LoginController.id);
	}
	
	
	
	//方法3：加载当前Account.txt->accMap
	public static HashMap<Integer,Account> loadAccount(){
		//1.创建输入流
		ObjectInputStream ois = null;
		HashMap<Integer,Account> accMap = new HashMap<>();
		try {
			ois = new ObjectInputStream(new FileInputStream("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt"));
			accMap = (HashMap<Integer,Account>)ois.readObject();
		}catch(EOFException e) {
			System.out.println("当前列表为空");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} finally{	
			if(ois!=null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return accMap;
	}
	
	//方法4：从当前accMap中挑选出当前Id的Account
	/**
	 * 返回值id: -1表示没有当前id对象，id>0表示当前ID用户在Account.txt的角标
	 */
	public static Account findAccount(HashMap<Integer,Account> accMap) {
		return accMap.get(LoginController.id);
	}
	
	//方法5：更新当前数据库：list->.txt
	public static void updateData(String file,List list){
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(list);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//方法6：加载全校所有学生的成绩
	public static HashMap<Integer,HashMap<String,StudentTestEntry>> loadStudentScore(){
		//1. 创建变量
		ObjectInputStream ois = null;
		HashMap<Integer,HashMap<String,StudentTestEntry>> scoreMap = new HashMap<>();
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\StudentScore.txt");
		//2. 判断文件是否存在
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//3. StudentScore.txt->scoreMap
		try {
			if(f.length()>0) {
				ois = new ObjectInputStream(new FileInputStream(f));
				scoreMap = (HashMap<Integer,HashMap<String,StudentTestEntry>>)ois.readObject();
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
		return scoreMap;
	}
	
	//方法7：加载当前id学生的成绩
	public static HashMap<String,StudentTestEntry> loadScore(Student stu){
		//1. 存储当前id学生的成绩单
		HashMap<String,StudentTestEntry> map = new HashMap<>();
		if(loadStudentScore().get(stu.getId())!=null) {
			map =loadStudentScore().get(stu.getId());
		}
		
		//2. 展示当前学生成绩
		for(int i=0;i<map.size();i++) {
			System.out.println(new ArrayList<>(map.values()).get(i));
		}
		return map;
	}
	
	//方法8：加载所有学生的作业集合
	public static HashMap<Integer,HashMap<HomeworkKey,StudentHomeworkEntry>> loadStudentHomework(){
		//1. 创建输入流
		ObjectInputStream ois = null;
		HashMap<Integer,HashMap<HomeworkKey,StudentHomeworkEntry>> homeworkMap = new HashMap<>();
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\StudentHomework.txt");
		
		//2. 判断文件是否存在
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//3. StudentHomework.txt->homeworkMap
		try {
			if(f.length()>0) {
				ois = new ObjectInputStream(new FileInputStream(f));
				homeworkMap = (HashMap<Integer,HashMap<HomeworkKey,StudentHomeworkEntry>>)ois.readObject();
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
		return homeworkMap;
	}
	
	//方法8：加载当前id学生的作业得分
	public static HashMap<HomeworkKey,StudentHomeworkEntry> loadHomework(Student stu){
		//1. 存储当前id学生的作业表
		HashMap<HomeworkKey,StudentHomeworkEntry> map = new HashMap<>();
		if(!loadStudentHomework().isEmpty()) {
			if(loadStudentHomework().get(stu.getId())!=null) {
				map = loadStudentHomework().get(stu.getId());
			}
			//展示当前学生作业
			ArrayList<StudentHomeworkEntry> list = new ArrayList<>(map.values());
			for(StudentHomeworkEntry she:list) {
				System.out.println(she);
			}
		}
		return map;
	}
	
	//方法9: 将学生对老师/校长的意见写入.txt文件
	public static void Feedback(TextField title, TextArea content,
			Label warning,RadioButton button_Anonymity,Student stu,String receiver) {
		//1. 判断标题和内容是否为空
		if(title.getText().trim().length()>0 && content.getText().trim().length()<=0){
			warning.setText("反馈内容不得为空");
			warning.setVisible(true);
			return;
		}else if(title.getText().trim().length()<=0 && content.getText().trim().length()>0){
			warning.setText("标题不得为空");
			warning.setVisible(true);
			return;
		}
		//2. 如果标题和内容都不为空，则将内容和标题写入.txt
		//提取学生的反馈内容，将其封装成FeedbackEntry对象
		if(content.getText().trim().length() > 0&&title.getText().trim().length() > 0){
			//3. 构建FeedbackEntry对象
			int classes = stu.getClasses();
			LocalDate date = LocalDate.now();
			//判断是否匿名
			String name = null;
			if(button_Anonymity.selectedProperty().get()){
				name = "匿名";
			}else{
				name =stu.getName();
			}
			String type = receiver;
			String text_title = title.getText().trim();
			String text_content = content.getText().trim();
			StudentFeedbackEntry feedback = new StudentFeedbackEntry(classes,date,name,type
					,text_title,text_content);
			
			//4. 将feedback加入本地文件
			WriteFeedback(feedback);
			System.out.println("添加反馈："+feedback);
			
			//5. 将文本框设置为空
			title.setText("");
			content.setText("");
		}
			

	}
	
	//方法9：将FeedbackEntry写入.txt文件
	public static void WriteFeedback(StudentFeedbackEntry feedback) {
		//1. 加载Feebback.txt -> List<StudentFeedbackEntry>
		ObjectInputStream ois = null;
		ArrayList<StudentFeedbackEntry> feedbackList = new ArrayList<StudentFeedbackEntry>();
		// 判断文件是否存在
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentFeedback.txt");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//2. 判断文件是否为空
		try {
			if(f.length()>0) {
				//3. 加载feedbackList
				ois = new ObjectInputStream(new FileInputStream(f));
				feedbackList = (ArrayList<StudentFeedbackEntry>) ois.readObject();
				//增加feedback 
				feedbackList.add(feedback);
			}
			else {
				feedbackList.add(feedback);
			}	
		}catch(EOFException e) {
			System.out.println("当前列表为空");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//4. 更新Feedback.txt
		updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentFeedback.txt",feedbackList);
	}
	
	
}
