package backend.entry.student;

import java.io.Serializable;

public class StudentHomeworkEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1. 属性区
	private int id;
	private String name;
	private String homework;
	private String subject;
	private String score;
	private String knowledge;
	private String comment;
	
	public String getHomework() {
		return homework;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
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


	//2. 构造方法
	public StudentHomeworkEntry() {
		super();
	}

	public StudentHomeworkEntry(int id, String name, String homework, String subject, String score, String knowledge,
			String comment) {
		super();
		this.id = id;
		this.name = name;
		this.homework = homework;
		this.subject = subject;
		this.score = score;
		this.knowledge = knowledge;
		this.comment = comment;
	}

	
	
	//3. 方法区
	@Override
	public String toString() {
		return "StudentHomeworkEntry [id=" + id + ", name=" + name + ", homework=" + homework + ", subject=" + subject
				+ ", score=" + score + ", knowledge=" + knowledge + ", comment=" + comment + "]";
	}
	

	
	
}
