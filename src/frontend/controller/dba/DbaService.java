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
 * DbaController�����ࣺȫ����̬����
 * Ŀ�ģ�����DbaController���г�UI֮��Ĺ���ʵ��
 */

public class DbaService {
	//1. ������
	
	//2. ������
	//����1�������˺ţ�����inputչʾ���
	public static ArrayList<DbaSearchResultEntry> searchResult(HashMap<Integer,Account> accMap,String input,Label warning){	
		//1. ��������Account
		HashMap<Integer,Account> resultMap = new HashMap<>();
		ArrayList<DbaSearchResultEntry> result = new ArrayList<>();
		
		//2. ����inputɸѡ���
		try {
			//2.1 ʲô�������룬չʾ���н��
			if(input.equals("")) {
				resultMap = accMap;
			}else {
				int id = Integer.valueOf(input);
				//2.2 �ҵ�ָ��id��Account
				Account acc = accMap.get(id);			
				if(acc!=null) {
					resultMap.put(acc.getId(),acc);
				}else {
					warning.setText("IDΪ"+id+"���û�������");
					warning.setVisible(true);
				}
			}
			//3. ��ȡ��resultList��ÿ��Account��id,name,usertype
			ArrayList<Account> resultList = new ArrayList<>(resultMap.values());
			for(int i=0;i<resultList.size();i++) {
				ArrayList<String> list = new ArrayList<>();
				list.add(resultList.get(i).getId()+"");
				list.add(resultList.get(i).getuserName());
				list.add(resultList.get(i).getUsertype());
				DbaSearchResultEntry dre = new DbaSearchResultEntry(list);
				
				//2. ����TextField����
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
			warning.setText("�����id��ʽ��������������");
			warning.setVisible(true);
		}
		return result;
	}
	
	

	//����2������button_ɾ����ɾ���û��Ĳ���
	private static void DeleteResult(HashMap<Integer,Account> accMap,DbaSearchResultEntry dre) {
		//1. �����Ի������û�����ȷ��ɾ��
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("ɾ���˻�");
		alert.setContentText("ȷ��ɾ��?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//2. ȷ��ɾ��
			//2.1 ɾ���˻�Account
			deleteAccount(accMap,dre);
			//2.2 ɾ��Teacher��Student
			String usertype = dre.getText_usertype().getText();
			if(usertype.equals("ѧ��")) {
				deleteStudent(dre);			
			}else if(usertype.equals("��ʦ")) {
				deleteTeacher(dre);	
			}			
			//������ʾ�Ի���
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setContentText("�˻�ɾ���ɹ�");
			alert1.showAndWait();
		}else {
			return;
		}
		//3. UI�������
		dre.getText_id().setVisible(false);
		dre.getText_name().setVisible(false);
		dre.getText_usertype().setVisible(false);
		dre.getButton_delete().setVisible(false);
		dre.getButton_modify().setVisible(false);
	}
	
	
	//����3������button_�޸ģ��޸��û��Ĳ���
	private static void ModifyResult(HashMap<Integer,Account> accMap,DbaSearchResultEntry dre) {
		//1. UI�������
		dre.getText_id().setEditable(true);
		dre.getText_name().setEditable(true);
		dre.getText_usertype().setEditable(true);
		
		dre.getText_id().setDisable(false);
		dre.getText_name().setDisable(false);
		dre.getText_usertype().setDisable(false);	
		
		//2. �����Ի������û�ѡ��ȷ���޸�
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("�޸��˻�");
		alert.setContentText("ȷ���޸�?");
		Optional<ButtonType> result = alert.showAndWait();
		
		//3. �޸��û�
		if (result.get() == ButtonType.OK){
			//3.2 ������Teacher->Student����Student->Teacher
			//��1����ȡԭ�����û�����
			int id = Integer.valueOf(dre.getText_id().getText());
			String oldUsertype = accMap.get(id).getUsertype();
			//��2����ȡ���޸ĵ��û�����
			String newUsertype = dre.getText_usertype().getText();
			
			//��3�������¾��û����ͣ�������Ӧ����
			if(oldUsertype.equals(newUsertype)) {
				//3.1 �޸�Account
				modifyAccount(accMap,dre);
			}else if(oldUsertype.equals("ѧ��")&&newUsertype.equals("��ʦ")) {
				//3.1 �޸�Account
				modifyAccount(accMap,dre);
				studentToTeacher(dre); //ѧ��->��ʦ
				//������ʾ�Ի���
				Alert alert1 = new Alert(AlertType.INFORMATION);
				System.out.println("�˻��޸ĳɹ�");
				alert1.setContentText("�˻��޸ĳɹ�");
				alert1.showAndWait();
			}else if(oldUsertype.equals("��ʦ")&&newUsertype.equals("ѧ��")) {
				//3.1 �޸�Account
				modifyAccount(accMap,dre);
				teacherToStudent(dre); //��ʦ->ѧ��
				//������ʾ�Ի���
				System.out.println("�˻��޸ĳɹ�");
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setContentText("�˻��޸ĳɹ�");
				alert2.showAndWait();
			}else {
				System.out.println("�˻����ʹ����޸�ʧ��");
				//������ʾ�Ի���
				Alert alert3 = new Alert(AlertType.INFORMATION);
				alert3.setContentText("�˻����ʹ����޸�ʧ��");
				alert3.showAndWait();
			}
		}else {
			return;
		}
	}
	
	
	//����4����accList��ɾ��ָ��Id���û���������Account.txt
	public static void deleteAccount(HashMap<Integer,Account> accMap,DbaSearchResultEntry dre){
		//1. ɾ��ָ��id��Account
		int deleteId = Integer.valueOf(dre.getText_id().getText());
		accMap.remove(deleteId);
		//2. ����Account.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt", accMap);
	}
	
	
	//����5����accList���޸�ָ��Id���û���������Account.txt
	public static void modifyAccount(HashMap<Integer,Account> accMap,DbaSearchResultEntry dre){
		//1. �޸�ָ��id��Account
		int modifyId = Integer.valueOf(dre.getText_id().getText());
		//����accList���ҵ���Id��Ӧ��Account��index
		Account acc = accMap.get(modifyId);
		
		//2. �½�һ��Account�������滻ԭ����Account
		String username = dre.getText_name().getText();
		String usertype = dre.getText_usertype().getText();
		
		//3. ����accList
		if(acc!=null) {
			acc.setId(modifyId);
			acc.setuserName(username);
			acc.setUsertype(usertype);
			accMap.put(modifyId, acc);
		}
	
		//4. ����Account.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Account.txt", accMap);
	}
	
	//����6����Student�޸�ΪTeacher�û�
	public static void studentToTeacher(DbaSearchResultEntry dre) {
		//1. ���ص�ǰteaList,stuList
		HashMap<Integer, Student> stuMap = StudentService.loadStudent();
		HashMap<Integer,Teacher> teaMap = TeacherService.loadTeacher();
		//2. ��ȡ���޸ĵ�Student����
		int stu_id = Integer.valueOf(dre.getText_id().getText());
		Student stu = stuMap.get(stu_id);
		//�����stuList���Ƴ�
		if(stu!=null) {
			stuMap.remove(stu_id);
		}
		
		//3. ��Student����ת����Teacher
		Teacher teacher = new Teacher(stu);
		//��teacher��ӵ�teaMap
		teaMap.put(stu_id, teacher);
		
		//4. ����Teachers.txt��Students.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt", stuMap);
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt", teaMap);
	}
	
	//����7����Teacher�޸�ΪStudent�û�
	public static void teacherToStudent(DbaSearchResultEntry dre) {
		//1. ���ص�ǰteaList,stuList
		HashMap<Integer, Student> stuMap = StudentService.loadStudent();
		HashMap<Integer,Teacher> teaMap = TeacherService.loadTeacher();
		//2. ��ȡ���޸ĵ�Teacher����
		int tea_id = Integer.valueOf(dre.getText_id().getText());
		Teacher teacher = teaMap.get(tea_id);
		//�����teaList���Ƴ�
		if(teacher!=null) {
			teaMap.remove(tea_id);
		}
		
		//3. ��Teacher����ת����Student
		Student stu= new Student(teacher);
		//��stu��ӵ�stuList
		stuMap.put(tea_id, stu);
		
		//4. ����Teachers.txt��Students.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt", stuMap);
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt", teaMap);
	}
	
	
	//����9���ӵ�ǰStudents.txt��ɾ��ָ��id��Student����
	public static void deleteStudent(DbaSearchResultEntry dre) {
		//1. ���ص�ǰ��stuList
		HashMap<Integer, Student> stuMap = StudentService.loadStudent();
		//2. ��ȡ��ǰ��ɾ�������id
		int stu_id = Integer.valueOf(dre.getText_id().getText());
		//3. �����stuList���Ƴ�
		stuMap.remove(stu_id);
		//4. ����Students.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Students.txt", stuMap);
	}
	
	//����9���ӵ�ǰTeachers.txt��ɾ��ָ��id��Teacher����
	public static void deleteTeacher(DbaSearchResultEntry dre) {
		//1. ���ص�ǰ��teaList
		HashMap<Integer,Teacher> teaMap = TeacherService.loadTeacher();
		//2. ��ȡ��ǰ��ɾ�������id
		int tea_id = Integer.valueOf(dre.getText_id().getText());
		//3. �����teaList���Ƴ�
		teaMap.remove(tea_id);
		//4. ����Teachers.txt
		TeacherService.updateData("E:\\eclipse_workspace\\StudentManagerSubmission\\database/Teachers.txt", teaMap);
	}
	
	//����10����ȡȫУ�����������ο��Ե�ȫУѧ���ɼ���
	public static HashMap<String,ArrayList<StudentTestEntry>> gradeTranscripts() {
		//1. ����ȫУѧ�������п��Գɼ�
		HashMap<Integer,HashMap<String,StudentTestEntry>> stuScoreMap = StudentService.loadStudentScore();
		ArrayList<HashMap<String,StudentTestEntry>> stuTranscripts = new ArrayList<>(stuScoreMap.values());
		//2. ͳ�������гɼ��Ŀ�������---��Ϊ��������key
		LinkedHashSet<String> tests = new LinkedHashSet<>();
		for(int i=0;i<stuTranscripts.size();i++) {
			tests.addAll(stuTranscripts.get(i).keySet());
		}
		
		//3. ���¹����ɼ�����HashMap<String test,ArrayList<StudentTestEntry>>
		HashMap<String,ArrayList<StudentTestEntry>> testScores = new HashMap<>();
		/**key---ĳ�ο��ԣ�ArrayList---���ڸôο��Ե�����ѧ���ɼ�*/
		for(String test:tests) {
			//3.1 list����װ�ôο��Ե�����ѧ���ɼ�
			ArrayList<StudentTestEntry> list = new ArrayList<>();
			//3.2 ����ÿһ�ο��ԣ���stuTranscripts�ҵ�key=��ǰtest��StudentTestEntry������list
			for(HashMap<String,StudentTestEntry> map:stuTranscripts) {
				StudentTestEntry ste = map.get(test);
				if(ste!=null) {
					list.add(ste);
				}
			}
			//3.3 ��Entry<String test,ArrayList<StudentTestEntry>>�������Ľ����
			testScores.put(test, list);
		}
		return testScores;
	}
	
	//����11������ȫУѧ��ĳ�ο��Գɼ��б��õ���Ӧ��ͳ����(String����)---����Ŀ��
	public static HashMap<String,DbaGradeScoreEntry> gradeStatistic
		 (HashMap<String,ArrayList<StudentTestEntry>> testScores, String test) {
		//1. ���ο���ȫУѧ���ɼ���
		ArrayList<StudentTestEntry> stuScoreList=testScores.get(test);
		//2. �����п�Ŀ+�ֵܷ�����ѧ�����Ƴɼ����ܱ��а���
		/**��10��ArrayList�ֱ��Ӧ��score,chinese,math,english,physics,chemistry,biology,
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
		
		//3. ��ÿһ��ѧ��������ѧ���ĵ��Ƴɼ��ֱ����뵽��Ӧ�ĵ��Ƴɼ��б���
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
		
		//4. ��ȡÿ�����Ƴɼ��ģ���߷֣���ͷ֣�ƽ���֣�����
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
		
		//5. ������߷֣���ͷ֣�ƽ���֣������Ӧ��DbaGradeScoreEntry			
		//5.1 ͳ����߷�
		DbaGradeScoreEntry max = new DbaGradeScoreEntry("��߷�",scoreStat.get("max")
				,chineseStat.get("max"),mathStat.get("max"),englishStat.get("max"),
				physicsStat.get("max"),chemistryStat.get("max"),biologyStat.get("max"),
				politicsStat.get("max"),historyStat.get("max"),geologyStat.get("max"));
		//5.2 ͳ����ͷ�
		DbaGradeScoreEntry min = new DbaGradeScoreEntry("��ͷ�",scoreStat.get("min")
				,chineseStat.get("min"),mathStat.get("min"),englishStat.get("min"),
				physicsStat.get("min"),chemistryStat.get("min"),biologyStat.get("min"),
				politicsStat.get("min"),historyStat.get("min"),geologyStat.get("min"));
		//5.3 ͳ��ƽ����
		DbaGradeScoreEntry mean = new DbaGradeScoreEntry("ƽ����",scoreStat.get("mean")
				,chineseStat.get("mean"),mathStat.get("mean"),englishStat.get("mean"),
				physicsStat.get("mean"),chemistryStat.get("mean"),biologyStat.get("mean"),
				politicsStat.get("mean"),historyStat.get("mean"),geologyStat.get("mean"));	
		//5.4 ͳ�Ʒ���
		DbaGradeScoreEntry std = new DbaGradeScoreEntry("����",scoreStat.get("std")
				,chineseStat.get("std"),mathStat.get("std"),englishStat.get("std"),
				physicsStat.get("std"),chemistryStat.get("std"),biologyStat.get("std"),
				politicsStat.get("std"),historyStat.get("std"),geologyStat.get("std"));
		
		//6. ���ؽ��
		HashMap<String,DbaGradeScoreEntry> result = new HashMap<>();
		result.put("��߷�", max);
		result.put("��ͷ�", min);
		result.put("ƽ����", mean);
		result.put("����", std);
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
		//1. ��ArrayList<Integer> -> Integer[]
		Integer[] array = list.toArray(new Integer[list.size()]);
		int max = array[0];
		int min = array[0];
		int sum = 0;
		double mean = 0.0;
		//2. �������飺��max,min,mean
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
		
		//3. �������飺�󷽲�
		double std = 0.0;
		for(int i=0;i<array.length;i++) {
			std += (array[i]-mean)*(array[i]-mean);
		}
		
		//4. ���ؽ��
		HashMap<String,String> map = new HashMap<>();
		map.put("max", max+"");
		map.put("min",min+"");
		map.put("mean", mean+"");
		map.put("std", Math.sqrt(std)+"");
		
		return map;
	}

	
	//����12����ȡȫУ�����������ο��Ե�ȫУѧ���ɼ�����ͳ�Ʒ�������߷֣���ͷ֣�ƽ���֣�����
	public static HashMap<String,HashMap<String,DbaGradeScoreEntry>> gradeScoreAnalysis() {
		//1. ����ȫУѧ�����п������εĳɼ���
		HashMap<String,ArrayList<StudentTestEntry>> testScores = gradeTranscripts();
		LinkedHashSet<String> tests = new LinkedHashSet<>(testScores.keySet());
		/**���Ľ��ΪHashMap<�������Σ�HashMap<ͳ������DbaGradeScoreEntry>>*/
		HashMap<String,HashMap<String,DbaGradeScoreEntry>> analysis = new HashMap<>();
		
		//2. ���������ÿһ�ο��Ե�ȫУѧ���ɼ���������ÿһ�ο��Ե�ȫУѧ���ɼ���ͳ�Ʒ���
		for(String test:tests) {
			//��ȡ��ǰ�������е��Ƶ���߷֣���ͷ֣�ƽ���֣�����
			HashMap<String,DbaGradeScoreEntry> statistics = gradeStatistic(testScores,test);
			analysis.put(test, statistics);	
		}
		return analysis;
		
	}

	//����13������ȫУѧ�����п������εĳɼ��������հ༶����
	private static HashMap<String, HashMap<String, ArrayList<StudentTestEntry>>> classTranscripts() {
		/**������Ŀ�ģ�
		 * ��HashMap<String test,ArrayList<StudentTestEntry>> testScores 
		 * -> HashMap<String test,HashMap<String classes, ArrayList<StudentTestEntry>>> classScores
		 */
		/**���ս����String 1: test; String 2: classes; ArrayList: �ð�����ѧ�����ο��Գɼ�*/
		HashMap<String,HashMap<String,ArrayList<StudentTestEntry>>> classTestScores = new HashMap<>();
		
		//1.����ȫУѧ���ɼ��������Է���	
		HashMap<String,ArrayList<StudentTestEntry>> testScores = gradeTranscripts();
		//���key1��test����
		LinkedHashSet<String> tests = new LinkedHashSet<>(testScores.keySet());
		
		//2. ��testScores ->  HashMap<String test,HashMap<String classes, ArrayList<StudentTestEntry>>> 
		for(String test:tests) {
			//2.1 ÿ�ο��ԣ�ȫУ���п����ĳɼ���δ�ְ�
			ArrayList<StudentTestEntry> stuScoreList = new ArrayList<>(testScores.get(test));
			
			//2.2 ���key2��classes�༶
			LinkedHashSet<String> classes = new LinkedHashSet<>();
			for(StudentTestEntry ste:stuScoreList) {
				classes.add(ste.getClasses());
			}
			
			//2.3 ���ÿ���༶������ȫУѧ��������ÿ�����ѧ���ɼ�����ɼ���
			//(1) ����HashMap<String classes, ArrayList<StudentTestEntry>>
			HashMap<String,ArrayList<StudentTestEntry>> stuClassScores = new HashMap<>();
			for(String banji:classes) {
				//(2) ��������ÿ����ĳɼ�����
				ArrayList<StudentTestEntry> stuClassList = new ArrayList<>();
				//(3)����ȫУѧ������List -> HashMap<classes,List>
				for(StudentTestEntry ste:stuScoreList) {
					//����������ڴ˰�,�������˴ο��Գɼ�����˼���			
					if(ste.getClasses().equals(banji)) {		
						stuClassList.add(ste);
					}
				}
				//(4)������HashMap�������Ľ����
				stuClassScores.put(banji, stuClassList);
			}
			
			//2.4 ����ÿ�ο��Եķְ�ɼ�
			classTestScores.put(test, stuClassScores);	
		}

		//3. ��󷵻ؽ����ȫУѧ�����п������εĳɼ��������հ༶����	
		return classTestScores;
	}
	
	//����14������ȫУѧ��ĳ�ο��Գɼ��б��õ���Ӧ��ͳ����(String����)---���༶��
	public static HashMap<String/*classes*/,DbaClassScoreEntry> classStatistic
	 (HashMap<String/*classes*/,ArrayList<StudentTestEntry>> classTestScores, String subject) {
		/**���ս�������ظÿ�Ŀ��ÿ�����ͳ����Ϣ
		 * HashMap<String,DbaClassScoreEntry>  subStatistics
		 * String classes: �༶
		 * DbaClassScoreEntry��ÿ�����ͳ����Ϣ��max,min,mean,std,count*/
		HashMap<String,DbaClassScoreEntry>  subStatistics = new HashMap<>();
		
		//1. ��ȡ�༶����
		LinkedHashSet<String> classes = new LinkedHashSet<>(classTestScores.keySet());
		
		//2. ����ÿ���༶����ÿ�ĸsubject��ͳ����Ϣ��max,min,mean,std,count
		for(String banji:classes) {
			//2.1 ���ظð������ѧ���ɼ�
			ArrayList<StudentTestEntry> stuClassList = classTestScores.get(banji);
			//2.2 ���ð�����ѧ���ĸÿƳɼ���ȡ����->ArrayList<Integer> list
			ArrayList<Integer> list = new ArrayList<>();
			for(StudentTestEntry ste:stuClassList) {
				switch(subject){
				case "�ܷ�":
					list.add(strToInteger(ste.getScore()));
					break;
				case "����":
					list.add(strToInteger(ste.getChinese()));
					break;
				case "��ѧ":
					list.add(strToInteger(ste.getMath()));
					break;
				case "Ӣ��":
					list.add(strToInteger(ste.getEnglish()));
					break;
				case "����":
					list.add(strToInteger(ste.getPhysics()));
					break;
				case "��ѧ":
					list.add(strToInteger(ste.getChemistry()));
					break;
				case "����":
					list.add(strToInteger(ste.getBiology()));
					break;
				case "����":
					list.add(strToInteger(ste.getPolitics()));
					break;
				case "��ʷ":
					list.add(strToInteger(ste.getHistory()));
					break;
				case "����":
					list.add(strToInteger(ste.getGeology()));
					break;
				}
					
			}
			//2.3 ��ȡ�ð࣬�ÿƳɼ���max,min,mean,std,count -> DbaClassScoreEntry
			HashMap<String,String> classStat = listStatistics(list);
			String count = stuClassList.size()+"";
			DbaClassScoreEntry dse = new DbaClassScoreEntry(banji,classStat.get("max"),
					classStat.get("min"),classStat.get("mean"),classStat.get("std"),count);
			
			//2.4 ��ÿ�����ͳ������Ϣ����map
			subStatistics.put(banji, dse);
		}
		//3. ���ؽ��
		return subStatistics;
	}
		
	//����15����ȡÿ�ο��ԣ�ÿ����Ŀ�İ༶ͳ����������	
	public static HashMap<String/*test*/,
						HashMap<String/*subject*/,
							HashMap<String/*classes*/,DbaClassScoreEntry>>> classScoreAnalysis() {
		/**
		 * �������HashMap<String test,
		 * 				HashMap<String subject,
		 * 					HashMap<String classes,DbaClassScoreEntry>>> 
		 */
		HashMap<String,HashMap<String,HashMap<String,DbaClassScoreEntry>>> analysis = new HashMap<>();
		
		//1. ����ȫУѧ�����п������εĳɼ��������հ༶����
		HashMap<String/*test*/,HashMap<String/*classes*/,ArrayList<StudentTestEntry>>> testScores = classTranscripts();
		//���п��Լ���
		LinkedHashSet<String> tests = new LinkedHashSet<>(testScores.keySet());
	
		//2. �ɷְ࿼�Գɼ�����ȡÿ�ο��ԣ�ÿ���༶���ֿ�Ŀ���ĸ�ͳ����		
		/** ���ú���HashMap<String,DbaClassScoreEntry>  testStatistic
		 * 	(HashMap<String,ArrayList<StudentTestEntry>> testScores, String test){} 
		 * ˼·�����ǽ�ÿ�ε�test,classes�̶������ܵõ�һ��HashMap<String,ArrayList<StudentTestEntry>>	
		 *	˫��ѭ���������Եõ����
		 */		 
		for(String test:tests) {
			//2.1 ÿ�ο��ԣ�ȫУ���༶�ֵ�ѧ���ɼ�map
			HashMap<String/*classes*/,ArrayList<StudentTestEntry>> stuClassScores = testScores.get(test);
			
			//2.2 ���key2��subject��Ŀ
			LinkedHashSet<String> subject = new LinkedHashSet<>();		
			subject.add("�ܷ�");subject.add("����");subject.add("��ѧ");subject.add("Ӣ��");subject.add("����");
			subject.add("��ѧ");subject.add("����");subject.add("����");subject.add("��ʷ");subject.add("����");
			//2.3 ���ÿ���༶Map�����䴫�뷽��15
			/**
			 * ����1: HashMap<String classes,ArrayList<StudentTestEntry>> classTestScore
			 * ����2: String subject
			 */
			HashMap<String/*subject*/,HashMap<String/*classes*/,DbaClassScoreEntry>> classAnalysis = new HashMap<>();
			for(String sub:subject) {
				HashMap<String/*classes*/,DbaClassScoreEntry> classStats = classStatistic(stuClassScores,sub);
				classAnalysis.put(sub,classStats);
			}
			
			//2.4 ����ÿ�ο��Եİ��༶�ֵ�ͳ����
			analysis.put(test, classAnalysis);
		}
		//3. ���ؽ��
		return analysis;
	}

	
	//����16��������ʦ�ĳ��ڼ�¼��TeacherDuty.txt -> Map<LocalDate,Map<Integer id,TeacherDutyEntry>>
	public static HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> loadTeacherDuty() {
		/**
		 * ���ս����Map<LocalDate ����ʱ��,TeacherDutyEntry ��ʦ���ڼ�¼>
		 */
		HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> teacherDuty = new HashMap<>();
		//1. ����������
		ObjectInputStream ois = null;
		/**
		 * ��¼ע���˺��Ƿ����ע��ɹ���
		 * ---true�����˺Ų�����,ע��ɹ�
		 * ---false�����˺Ŵ��ڣ�ע��ʧ��
		 */
		try {
			//1. �ж��ļ��Ƿ����
			File f = new File("E:\\eclipse_workspace\\StudentManagerSubmission\\database/TeacherDuty.txt");
			if(!f.exists()) {
				f.createNewFile();
			}
			//2. ���TeacherDuty.txt����:���������ݶ�ȡ����
			if(f.length() > 0){
				ois = new ObjectInputStream(new FileInputStream(f));
				teacherDuty = (HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> )ois.readObject();
				//�����ǰ�������ڣ�������ʦ�ĳ��ڼ�¼
				if(teacherDuty.isEmpty()) {
					System.out.println("���ڼ�¼Ϊ��");
				}else {
					for(Map.Entry<LocalDate,HashMap<Integer,TeacherDutyEntry>> e1:teacherDuty.entrySet()) {
						for(Map.Entry<Integer,TeacherDutyEntry> e2:e1.getValue().entrySet()) {
							System.out.println("ʱ�䣺"+e1.getKey()+"���ڼ�¼:"+e2.getValue());
						}
					}
				}
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
			try {
				if(ois != null){
					ois.close();
				}
			} catch (IOException e) {
				System.out.println("�رն�������������");
				e.printStackTrace();
			}
		}
		return teacherDuty;	
	}
	
	
	//����17������DutyPane
	public static ObservableList<DbaTeacherDutyEntry> iniObList(LocalDate date) {
		/**
		 * ���ս����ObservableList<DbaTeacherDutyEntry> result
		 */
		//1. ��ʼ��������������ʦ����Ϣ->teaList
		ObservableList<DbaTeacherDutyEntry> result = FXCollections.observableArrayList();
		HashMap<Integer,Teacher> teaMap = TeacherService.loadTeacher();
		ArrayList<Teacher> teaList = new ArrayList<>(teaMap.values());
		//2. ����������ʦ�ĳ�����Ϣ
		HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> teacherDuty=loadTeacherDuty();
		HashMap<Integer,TeacherDutyEntry> dateDuty = teacherDuty.get(date);
		//3. �������û�г��ڼ�¼,����Ĭ��ѡ��
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

	//����17��������ʦ�ĳ��ڼ�¼ 
	public static HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> updateTeacherDuty
		(ObservableList<DbaTeacherDutyEntry> dutyList,LocalDate date) {
		//1. �������е�TeacherDuty.txt
		HashMap<LocalDate,HashMap<Integer,TeacherDutyEntry>> teacherDuty = loadTeacherDuty();
		//2. �½�һ��HashMap<Integer,TeacherDutyEntry>
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
			//����map
			map.put(dutyList.get(i).getId(),tde);
		}
		//����teacherDuty 
		teacherDuty.put(date, map);
		return teacherDuty;
		
	}


	
//	//����15������ȫУѧ�����п������εĳɼ��������տ�Ŀ����
//	private static HashMap<String, HashMap<String, ArrayList<StudentTestEntry>>> subjectTranscript() {
//		/**������Ŀ�ģ�
//		 * ��HashMap<String test,ArrayList<StudentTestEntry>> testScores 
//		 * -> HashMap<String test,HashMap<String subject, ArrayList<StudentTestEntry>>> classScores
//		 */
//		/**���ս����String 1: test; String 2: subject; ArrayList: �ð�����ѧ�����ο��Գɼ�*/
//		HashMap<String,HashMap<String,ArrayList<StudentTestEntry>>> subjectTestScores = new HashMap<>();
//		
//		//1.����ȫУѧ���ɼ��������Է���	
//		HashMap<String,ArrayList<StudentTestEntry>> testScores = gradeTranscripts();
//		//���key1��test����
//		LinkedHashSet<String> tests = new LinkedHashSet<>(testScores.keySet());
//		
//		//2. ��testScores ->  HashMap<String test,HashMap<String classes, ArrayList<StudentTestEntry>>> 
//		for(String test:tests) {
//			//2.1 ÿ�ο��ԣ�ȫУ���п����ĳɼ���δ�ֿ�
//			ArrayList<StudentTestEntry> stuScoreList = new ArrayList<>(testScores.get(test));
//			
//			//2.2 ���key2��subject��Ŀ
//			LinkedHashSet<String> subject = new LinkedHashSet<>();
//			subject.add("�ܷ�");subject.add("����");subject.add("��ѧ");subject.add("Ӣ��");subject.add("����");
//			subject.add("��ѧ");subject.add("����");subject.add("����");subject.add("��ʷ");subject.add("����");
//			//2.3 ���ÿ����Ŀ������ȫУѧ��������ÿ����Ŀ��ѧ���ɼ����� ---����ȫ��ѧ��
//			//(1) ����HashMap<String classes, ArrayList<StudentTestEntry>>
//			HashMap<String,ArrayList<StudentTestEntry>> stuSubjectScores = new HashMap<>();
//			for(String sub:subject) {
//				stuSubjectScores.put(sub,stuScoreList);
//			}
//			//2.4 ����ÿ�ο��Եķֿ�Ŀ�ɼ�
//			subjectTestScores.put(test, stuSubjectScores);	
//		}
//
//		//3. ��󷵻ؽ����ȫУѧ�����п������εĳɼ��������տ�Ŀ����	
//		return subjectTestScores ;
//	}
}
