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
 * TeacherController�����ࣺȫ����̬����
 * Ŀ�ģ�����TeacherController���г�UI֮��Ĺ���ʵ��
 */
public class TeacherService {
	
	//����1������Teacher.txt -> ArrayList<Teacher> teaMap
	public static HashMap<Integer,Teacher> loadTeacher(){
		//1. �������
		HashMap<Integer,Teacher> teaMap = new HashMap<>();
		ObjectInputStream ois = null;
		//2. �����ļ�����
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//3. �ж��ļ��Ƿ�Ϊ��
		if(f.length()>0) {
			try {
				ois =  new ObjectInputStream(new FileInputStream(f));
				teaMap = (HashMap<Integer,Teacher>)ois.readObject();
			}catch(EOFException e) {
				System.out.println("��ǰ�б�Ϊ��");
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
	
	//����2���ҵ���ǰid��Ӧ��Account
	public static Account findAccount(HashMap<Integer,Account> accMap) {
		return accMap.get(LoginController.id);
	}
	
	//����3���ҵ���ǰid��Ӧ��Teacher���������
	public static Teacher findTeacher(HashMap<Integer,Teacher> teaMap) {
		
		return teaMap.get(LoginController.id);
	}
	
	//����4�����ص�ǰid��Teacher��Ӧ��ѧ��Id�ļ���
	public static ArrayList<Integer> loadStudentId(Teacher teacher){
		//1. �ȴ�ѧ����Students.txt����ȡȫУ����ѧ�����󼯺�
		HashMap<Integer,Student> stuMap = StudentService.loadStudent();
		//2. ��ȡ�༶���ڵ�ǰ��ʦ�༶������ѧ����id
		ArrayList<Integer> stuIdList = new ArrayList<Integer>();
		//��ȡ����ѧ������
		ArrayList<Student> stuList = new ArrayList<>(stuMap.values());
		for(int i=0;i<stuList.size();i++) {
			if(stuList.get(i).getClasses()==teacher.getClasses()) {
				stuIdList.add(stuList.get(i).getId());
			}
		}
		//3. ���ؽ��
		return stuIdList;
	}
	
	//����5�����ص�ǰ��ʦ��Ӧ������ѧ������������
	public static ArrayList<String> loadStudentName(Teacher teacher){
		//1. �ȴ�ѧ����Students.txt����ȡȫУ����ѧ�����󼯺�
		HashMap<Integer,Student> stuMap = StudentService.loadStudent();
		//2. ��ȡ�༶���ڵ�ǰ��ʦ�༶������ѧ��������
		ArrayList<String> stuNameList = new ArrayList<>();
		//��ȡ����ѧ������
		ArrayList<Student> stuList = new ArrayList<>(stuMap.values());
		for(int i=0;i<stuList.size();i++) {
			if(stuList.get(i).getClasses()==teacher.getClasses()) {
				stuNameList.add(stuList.get(i).getName());
			}
		}
		//3. ���ؽ��
		return stuNameList;
	}
		

	//����7����ʦ��ĳ�ο��Ե�ȫ��ɼ�¼��ȫУ�ɼ���
	public static HashMap<Integer,HashMap<String,StudentTestEntry>> updateStudentScore
		(HashMap<Integer,HashMap<String,StudentTestEntry>> stuScoreMap,TextField[][] text,
				ChoiceBox<String> choice_Test, String classes){
						
		//1. ��ȡTextField[][]��ChoiceBox�е�����
		String[][] scores = new String[text.length][13];
		for(int i=0;i<scores.length;i++) {
			for(int j=0;j<13;j++) {
				scores[i][j] = text[i][j].getText();
			}
		}
		String test = (String) choice_Test.getSelectionModel().getSelectedItem();
		
		
		//2. ����test����ȫ��ѧ���ĵ��ο��Գɼ�
		
		//���ѭ������i��ѧ��
		//int id = -1;
		//String name ="";
		//HashMap<String,StudentTestEntry> transcript = new HashMap<>();
		for(int i=0;i<scores.length;i++) {
			//2.1 ��ȡ��i��ѧ����id
			int id = Integer.valueOf(scores[i][0]);
			String name = scores[i][1];
			//2.2 ���µ�i��ѧ���ĳɼ����еĵ��˿���test�ɼ�
			StudentTestEntry testEntry = new StudentTestEntry(
				id,name,classes,test,scores[i][2],scores[i][3],scores[i][4],scores[i][5],scores[i][6]
				,scores[i][7],scores[i][8],scores[i][9],scores[i][10],scores[i][11],scores[i][12]);
			//2.3 ���˴ο��Գɼ���������ĳɼ�����
			/*��ȡ�����ĳɼ���*/
			HashMap<String,StudentTestEntry> transcript = stuScoreMap.get(id);
			if(transcript==null) {
				transcript = new HashMap<>();
			}
			
			//2.4 ����hashmap�����ظ��Ļ��ƣ�����ȫУѧ���ĳɼ���
			transcript.put(test, testEntry);
			stuScoreMap.put(id,transcript);
		}
		
		//3. ���ȫУ����ѧ���ĳɼ���
		//ȫУѧ���ĳɼ���
		if(!stuScoreMap.isEmpty()) {
			ArrayList<Entry<Integer, HashMap<String, StudentTestEntry>>> stuScoreList 
			= new ArrayList<>(stuScoreMap.entrySet());
			for(int i=0;i<stuScoreList.size();i++) {
				HashMap<String, StudentTestEntry> map = stuScoreList.get(i).getValue();
				ArrayList<Entry<String, StudentTestEntry>> list= new ArrayList<>(map.entrySet());
				for(int j=0;j<list.size();j++) {
					System.out.println("ID:"+stuScoreList.get(i).getKey()+"�ɼ���"+list.get(j).getValue());
				}
			}
		}
		return stuScoreMap;
	}
	
	
	//����8�����µ�ǰ���ݿ⣺Map->.txt
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
		
	//����9����ʦ��ĳ��ĳ����ҵ����ȫУ��ҵ��
	public static HashMap<Integer, HashMap<HomeworkKey, StudentHomeworkEntry>> updateStudentHomework(
			HashMap<Integer, HashMap<HomeworkKey, StudentHomeworkEntry>> stuHomeworkMap, TextField[][] text_Homework,
			ChoiceBox<String> choice_Homework, ChoiceBox<String> choice_Subject,TextArea text_Knowledge) {
		
		//1. ��ȡTextField[][]��ChoiceBox�е�����
		String[][] grade = new String[text_Homework.length][4];
		for(int i=0;i<grade.length;i++) {
			for(int j=0;j<4;j++) {
				grade[i][j] = text_Homework[i][j].getText();
			}
		}
		String homework= (String) choice_Homework.getSelectionModel().getSelectedItem();
		String subject = (String) choice_Subject.getSelectionModel().getSelectedItem();
		String knowledge = text_Knowledge.getText();
		//2. ����homework,subject���´˴�ѧ������ҵ��
		//���ѭ������i��ѧ��

		for(int i=0;i<grade.length;i++) {
			//2.1 ��ȡ��i��ѧ����id
			int id = Integer.valueOf(grade[i][0]);
			String name = grade[i][1];
			//2.2 ���µ�i��ѧ���ĳɼ����еĵ��˿���test�ɼ�
			StudentHomeworkEntry homeworkEntry = new StudentHomeworkEntry(
				id,name,homework,subject,grade[i][2],knowledge,grade[i][3]);
			//2.3 ���˴ο��Գɼ���������ĳɼ�����
			HashMap<HomeworkKey,StudentHomeworkEntry> transcript = stuHomeworkMap.get(id);
			if(transcript==null) {
				transcript = new HashMap<>();
			}
			HomeworkKey hk = new HomeworkKey(homework,subject);
			transcript.put(hk, homeworkEntry);
			
			//2.4 ����hashmap�����ظ��Ļ��ƣ�����ȫУѧ���ĳɼ���

			stuHomeworkMap.put(id,transcript);
		}
		//3. ���ȫУ����ѧ������ҵ
		//ȫУѧ������ҵ
		if(!stuHomeworkMap.isEmpty()) {
			ArrayList<Entry<Integer, HashMap<HomeworkKey, StudentHomeworkEntry>>> stuHomeworkList 
			= new ArrayList<>(stuHomeworkMap.entrySet());
			for(int i=0;i<stuHomeworkList.size();i++) {
				HashMap<HomeworkKey, StudentHomeworkEntry> map = stuHomeworkList.get(i).getValue();
				ArrayList<Entry<HomeworkKey, StudentHomeworkEntry>> list= new ArrayList<>(map.entrySet());
				for(int j=0;j<list.size();j++) {
					System.out.println("ID��"+stuHomeworkList.get(i).getKey()+"��ҵ��"+list.get(j).getValue());
				}
			}
		}		
		return stuHomeworkMap;
		
	}
	
	//����10������ȫУѧ���������������У������ʦ��
	public static ArrayList<StudentFeedbackEntry> loadFeedback(){
		//1. �������
		ObjectInputStream ois = null;
		ArrayList<StudentFeedbackEntry> feedbackList = new ArrayList<>();
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentFeedback.txt");
		//2. �ж��ļ��Ƿ����
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
		return feedbackList;
		
	}
	
	//����11�� ���ظð�ѧ���Ը�id��ʦ�����
	public static ArrayList<StudentFeedbackEntry> loadTeacherFeedback(Teacher teacher){
		//1. ����ȫУѧ�������
		ArrayList<StudentFeedbackEntry> feedbacks = loadFeedback();
		ArrayList<StudentFeedbackEntry> teaFeedback = new ArrayList<>();
		//2. �����������ڸ�id��ʦ�İ༶��ѧ������
		for(int i=0;i<feedbacks.size();i++) {
			if(feedbacks.get(i).getClasses()==teacher.getClasses()
					&&feedbacks.get(i).getType().equals("��ʦ")){
				teaFeedback.add(feedbacks.get(i));
			}
		}
		//3. ���ؽ��
		return teaFeedback;
	}
	
	//����12����ʼ��GridPane-Score
	public static void initializeScorePane(HashMap<Integer,HashMap<String,StudentTestEntry>> stuScoreMap,
			TextField[][] text_Score,ArrayList<Integer> stuIdList
			,ArrayList<String> stuNameList,String test) {
		
		for(int i=0;i<stuIdList.size();i++) {
			//1. ����id��
			//��ȡ��i��ѧ����id
			text_Score[i][0].setText(stuIdList.get(i)+"");
			text_Score[i][0].setEditable(false);
			text_Score[i][0].setAlignment(Pos.CENTER);
			text_Score[i][0].setMaxWidth(100);
			//2. ����name��
			text_Score[i][1].setText(stuNameList.get(i));
			text_Score[i][1].setEditable(false);
			text_Score[i][1].setMaxWidth(100);
			text_Score[i][1].setAlignment(Pos.CENTER);
			
			//3. ���÷�����
			for(int j=2;j<13;j++) {
				/*��ȡ��Idѧ����test�ο��Գɼ�*/
				if(!stuScoreMap.isEmpty()) {
					if(stuScoreMap.get(stuIdList.get(i))!=null) {
						StudentTestEntry testScore =stuScoreMap.get(stuIdList.get(i)).get(test);
						/*��ȡ����ĳ�Ƴɼ�*/
						if(testScore!=null) {
							text_Score[i][j].setText(getStudentSubjectScore(testScore, j));
						}
					}
				}else {
					text_Score[i][j].setText("");
				}
				
				//4. �����ı����ʽ
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
		//1. ��ȫУѧ���ɼ�����ѡ����idѧ����ĳ��Test�ɼ�
		
		String res = null;
		//2. ����ĳ�Ƴɼ�
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
	
	//����13����ʼ��GridPane-Homework
	public static void initializeHomeworkPane(HashMap<Integer, HashMap<HomeworkKey, StudentHomeworkEntry>> stuHomeworkMap,
			TextField[][] text_Homework, ArrayList<Integer> stuIdList,
			ArrayList<String> stuNameList, String homework, String subject) {
		
		for(int i=0;i<stuIdList.size();i++) {		
			//3.1 ����id��
			//��ȡ��i��ѧ����id
			text_Homework[i][0].setText(String.valueOf(stuIdList.get(i)));
			text_Homework[i][0].setEditable(false);
			text_Homework[i][0].setAlignment(Pos.CENTER);
			text_Homework[i][0].setMaxWidth(100);
			//3.2 ����name��
			text_Homework[i][1].setText(stuNameList.get(i));
			text_Homework[i][1].setEditable(false);
			text_Homework[i][1].setMaxWidth(100);
			text_Homework[i][1].setAlignment(Pos.CENTER);

			//3.3 ���õ÷���.������
			/*��ȡ��idѧ������homework��subject�ĵ÷֣�����*/
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
			
			//3.4 �����ı����ʽ
			text_Homework[i][2].setAlignment(Pos.CENTER);
			text_Homework[i][2].setMaxWidth(80);	
			
			text_Homework[i][3].setAlignment(Pos.CENTER);
			text_Homework[i][3].setMaxWidth(300);
			text_Homework[i][3].setPrefWidth(290);
			
		}
		
	}
	
	//����14����������ѧ���ĳ�����Ϣ: 
	public static HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> loadStudentDuty() {
		/**���ս����
		 * HashMap<LocalDate date,
		 * 		HashMap<String class,
		 * 			HashMap<Integer id,StudentDutyEntry>>*/		
		//1. �������
		ObjectInputStream ois = null;
		HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> dutyMap = new HashMap<>();
		File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/StudentDuty.txt");
		//2. �ж��ļ��Ƿ����
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
				//���ȫУȫ��ѧ���ĳ��ڼ�¼
				for(Map.Entry<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> m1:dutyMap.entrySet()) {
					for(Map.Entry<String,HashMap<String,StudentDutyEntry>> m2:m1.getValue().entrySet()) {
						for(Map.Entry<String,StudentDutyEntry> m3:m2.getValue().entrySet()) {
							System.out.println("���ڣ�"+m1.getKey()+"���༶��"+m2.getKey()+"�����ڼ�¼:"+m3.getValue());
						}
					}
				}
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
		return dutyMap;
	}
	
	
	//����15������ȫУѧ���ĳ��ڼ�¼
	public static HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> updateStudentDuty(StudentDutyEntry sde){
			
		/**���ս����
		 * HashMap<LocalDate date,
		 * 		HashMap<Integer class,
		 * 			HashMap<Integer id,StudentDutyEntry>>*/	
		//1. ����ȫУѧ���ĳ��ڼ�¼
		HashMap<LocalDate,HashMap<String,HashMap<String,StudentDutyEntry>>> dutyMap = loadStudentDuty();
		//2. �������ڣ���ȡ����ȫУѧ���ĳ��ڼ�¼
		HashMap<String,HashMap<String,StudentDutyEntry>> dayDutyMap = dutyMap.get(sde.getDate());													
		if(dayDutyMap==null) {
			dayDutyMap = new HashMap<>();
		}
		//2.1���ݰ༶����ȡ���գ��ð��ȫ��ѧ�����ڼ�¼
		HashMap<String,StudentDutyEntry> classDutyMap = dayDutyMap.get(sde.getClasses());
		if(classDutyMap==null) {
			classDutyMap = new HashMap<>();
		}
		//2.2 ���ݴ�������ѧ�����ڼ�¼StudentDutyEntry sde����ȫ�����ڼ�¼
		classDutyMap.put(sde.getId(),sde);
		//2.3 ����ȫУ���ڼ�¼
		dayDutyMap.put(sde.getClasses(), classDutyMap);
		//2.4 ����ȫ����¼
		dutyMap.put(sde.getDate(), dayDutyMap);
		return dutyMap;
	}
}
