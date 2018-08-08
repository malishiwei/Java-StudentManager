package backend.entry.teacher;

import java.io.Serializable;

public class TeacherHomeworkEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1. ������
	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	//2. ���췽����
	public TeacherHomeworkEntry(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public TeacherHomeworkEntry() {
		super();
	}
}
