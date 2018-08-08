package backend.entry.student;

import java.io.Serializable;

public class HomeworkKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//1. 属性区
	private String homework;
	
	private String subject;
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
	
	
	//2. 构造方法
	public HomeworkKey() {
		super();
	}
	public HomeworkKey(String homework, String subject) {
		super();
		this.homework = homework;
		this.subject = subject;
	}
	
	
	//3. 重写equals方法
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((homework == null) ? 0 : homework.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		
		HomeworkKey other = (HomeworkKey) obj;
		if (homework == null) {
			if (other.homework != null)
				return false;
		} else if (!homework.equals(other.homework))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
}
