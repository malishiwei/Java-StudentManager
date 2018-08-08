package backend.entry.teacher;

import java.io.Serializable;

import backend.entry.student.StudentTestEntry;

public class TeacherScoreEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1. 属性区
	private int id;
	private String name;
	private StudentTestEntry test;
	
	//2. 构造方法区
	
	public TeacherScoreEntry() {
		super();
	}
	public TeacherScoreEntry(int id, String name, StudentTestEntry test) {
		super();
		this.id = id;
		this.name = name;
		this.test = test;
	}
	
	//3. 方法区
	@Override
	public String toString() {
		return "TeacherScoreEntry [id=" + id + ", name=" + name + ", test=" + test + "]";
	}
	
	
}
