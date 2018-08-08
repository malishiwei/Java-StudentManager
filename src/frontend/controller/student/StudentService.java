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
 * StudentController�����ࣺȫ����̬����
 * Ŀ�ģ�����StudentController���г�UI֮��Ĺ���ʵ��
 */
public class StudentService {
	
	//����1�����ص�ǰѧ�����ݿ�Students.txt->stuMap
	public static HashMap<Integer,Student> loadStudent() {
		//1. �������������������
		ObjectInputStream ois = null;
		HashMap<Integer,Student> stuMap = new HashMap<Integer,Student>();
		//2. ��.txt -> Map
		try {
			ois = new ObjectInputStream(new FileInputStream("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt"));
			stuMap = (HashMap<Integer,Student>)ois.readObject();
		}catch (FileNotFoundException e) {
				e.printStackTrace();
		}catch(EOFException e) {
			System.out.println("��ǰ�û��б�Ϊ��");
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
	
	
	//����2����ѧ�����ݿ��ȡ��ǰѧ�������index---stuList��index
	public static Student findStudent(HashMap<Integer,Student> stuMap){
		//1. Ѱ��ѧ������
		return stuMap.get(LoginController.id);
	}
	
	
	
	//����3�����ص�ǰAccount.txt->accMap
	public static HashMap<Integer,Account> loadAccount(){
		//1.����������
		ObjectInputStream ois = null;
		HashMap<Integer,Account> accMap = new HashMap<>();
		try {
			ois = new ObjectInputStream(new FileInputStream("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt"));
			accMap = (HashMap<Integer,Account>)ois.readObject();
		}catch(EOFException e) {
			System.out.println("��ǰ�б�Ϊ��");
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
	
	//����4���ӵ�ǰaccMap����ѡ����ǰId��Account
	/**
	 * ����ֵid: -1��ʾû�е�ǰid����id>0��ʾ��ǰID�û���Account.txt�ĽǱ�
	 */
	public static Account findAccount(HashMap<Integer,Account> accMap) {
		return accMap.get(LoginController.id);
	}
	
	//����5�����µ�ǰ���ݿ⣺list->.txt
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
	
	//����6������ȫУ����ѧ���ĳɼ�
	public static HashMap<Integer,HashMap<String,StudentTestEntry>> loadStudentScore(){
		//1. ��������
		ObjectInputStream ois = null;
		HashMap<Integer,HashMap<String,StudentTestEntry>> scoreMap = new HashMap<>();
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\StudentScore.txt");
		//2. �ж��ļ��Ƿ����
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
			System.out.println("��ǰ�б�Ϊ��");
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
	
	//����7�����ص�ǰidѧ���ĳɼ�
	public static HashMap<String,StudentTestEntry> loadScore(Student stu){
		//1. �洢��ǰidѧ���ĳɼ���
		HashMap<String,StudentTestEntry> map = new HashMap<>();
		if(loadStudentScore().get(stu.getId())!=null) {
			map =loadStudentScore().get(stu.getId());
		}
		
		//2. չʾ��ǰѧ���ɼ�
		for(int i=0;i<map.size();i++) {
			System.out.println(new ArrayList<>(map.values()).get(i));
		}
		return map;
	}
	
	//����8����������ѧ������ҵ����
	public static HashMap<Integer,HashMap<HomeworkKey,StudentHomeworkEntry>> loadStudentHomework(){
		//1. ����������
		ObjectInputStream ois = null;
		HashMap<Integer,HashMap<HomeworkKey,StudentHomeworkEntry>> homeworkMap = new HashMap<>();
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database\\StudentHomework.txt");
		
		//2. �ж��ļ��Ƿ����
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
				System.out.println("��ǰ�б�Ϊ��");
			}
		}catch(EOFException e) {
			System.out.println("��ǰ�б�Ϊ��");
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
	
	//����8�����ص�ǰidѧ������ҵ�÷�
	public static HashMap<HomeworkKey,StudentHomeworkEntry> loadHomework(Student stu){
		//1. �洢��ǰidѧ������ҵ��
		HashMap<HomeworkKey,StudentHomeworkEntry> map = new HashMap<>();
		if(!loadStudentHomework().isEmpty()) {
			if(loadStudentHomework().get(stu.getId())!=null) {
				map = loadStudentHomework().get(stu.getId());
			}
			//չʾ��ǰѧ����ҵ
			ArrayList<StudentHomeworkEntry> list = new ArrayList<>(map.values());
			for(StudentHomeworkEntry she:list) {
				System.out.println(she);
			}
		}
		return map;
	}
	
	//����9: ��ѧ������ʦ/У�������д��.txt�ļ�
	public static void Feedback(TextField title, TextArea content,
			Label warning,RadioButton button_Anonymity,Student stu,String receiver) {
		//1. �жϱ���������Ƿ�Ϊ��
		if(title.getText().trim().length()>0 && content.getText().trim().length()<=0){
			warning.setText("�������ݲ���Ϊ��");
			warning.setVisible(true);
			return;
		}else if(title.getText().trim().length()<=0 && content.getText().trim().length()>0){
			warning.setText("���ⲻ��Ϊ��");
			warning.setVisible(true);
			return;
		}
		//2. �����������ݶ���Ϊ�գ������ݺͱ���д��.txt
		//��ȡѧ���ķ������ݣ������װ��FeedbackEntry����
		if(content.getText().trim().length() > 0&&title.getText().trim().length() > 0){
			//3. ����FeedbackEntry����
			int classes = stu.getClasses();
			LocalDate date = LocalDate.now();
			//�ж��Ƿ�����
			String name = null;
			if(button_Anonymity.selectedProperty().get()){
				name = "����";
			}else{
				name =stu.getName();
			}
			String type = receiver;
			String text_title = title.getText().trim();
			String text_content = content.getText().trim();
			StudentFeedbackEntry feedback = new StudentFeedbackEntry(classes,date,name,type
					,text_title,text_content);
			
			//4. ��feedback���뱾���ļ�
			WriteFeedback(feedback);
			System.out.println("��ӷ�����"+feedback);
			
			//5. ���ı�������Ϊ��
			title.setText("");
			content.setText("");
		}
			

	}
	
	//����9����FeedbackEntryд��.txt�ļ�
	public static void WriteFeedback(StudentFeedbackEntry feedback) {
		//1. ����Feebback.txt -> List<StudentFeedbackEntry>
		ObjectInputStream ois = null;
		ArrayList<StudentFeedbackEntry> feedbackList = new ArrayList<StudentFeedbackEntry>();
		// �ж��ļ��Ƿ����
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentFeedback.txt");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//2. �ж��ļ��Ƿ�Ϊ��
		try {
			if(f.length()>0) {
				//3. ����feedbackList
				ois = new ObjectInputStream(new FileInputStream(f));
				feedbackList = (ArrayList<StudentFeedbackEntry>) ois.readObject();
				//����feedback 
				feedbackList.add(feedback);
			}
			else {
				feedbackList.add(feedback);
			}	
		}catch(EOFException e) {
			System.out.println("��ǰ�б�Ϊ��");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//4. ����Feedback.txt
		updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentFeedback.txt",feedbackList);
	}
	
	
}
