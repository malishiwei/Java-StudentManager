package backend.entry.student;

import java.io.Serializable;
import java.time.LocalDate;

public class StudentFeedbackEntry implements Serializable{

	private static final long serialVersionUID = 1L;
	//1. 属性区
	private int classes;
	private LocalDate date;
	private String name;
	private String type;
	private String title;
	private String content;
	
	public int getClasses() {
		return classes;
	}
	public void setClasses(int classes) {
		this.classes = classes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	//2. 构造方法
	
	public StudentFeedbackEntry() {
		super();
	}
	public StudentFeedbackEntry(int classes, LocalDate date, String name, String type, String title, String content) {
		super();
		this.classes = classes;
		this.date = date;
		this.name = name;
		this.type = type;
		this.title = title;
		this.content = content;
	}
	
	
	//3. 方法区
	@Override
	public String toString() {
		return "StudentFeedbackEntry [classes=" + classes + ", date=" + date + ", name=" + name + ", type=" + type + ", title="
				+ title + ", content=" + content + "]";
	}

}
