package backend.entry.student;

import java.io.Serializable;
import java.time.LocalDate;

public class StudentDutyEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1. 属性区
	private LocalDate date;
	private String id;
	private String classes;
	private String name;
	private String duty;
	private String reason;
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	//2. 构造方法
	public StudentDutyEntry() {
		super();
	}
	public StudentDutyEntry(LocalDate date, String id, String classes, String name, String duty, String reason) {
		super();
		this.date = date;
		this.id = id;
		this.classes = classes;
		this.name = name;
		this.duty = duty;
		this.reason = reason;
	}
	
	//3.方法区
	@Override
	public String toString() {
		return "StudentDutyEntry [date=" + date + ", id=" + id + ", classes=" + classes + ", name=" + name + ", duty="
				+ duty + ", reason=" + reason + "]";
	}
	
}
