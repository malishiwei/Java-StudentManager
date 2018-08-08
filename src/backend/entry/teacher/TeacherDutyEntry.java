package backend.entry.teacher;

import java.io.Serializable;
import java.time.LocalDate;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class TeacherDutyEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1. 属性区
	private Integer id;
	private String name;
	private String classes;
	private boolean morning;
	private boolean noon;
	private boolean evening;
	private String note;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public boolean isMorning() {
		return morning;
	}
	public void setMorning(boolean morning) {
		this.morning = morning;
	}
	public boolean isNoon() {
		return noon;
	}
	public void setNoon(boolean noon) {
		this.noon = noon;
	}
	public boolean isEvening() {
		return evening;
	}
	public void setEvening(boolean evening) {
		this.evening = evening;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	//2. 方法区
	public TeacherDutyEntry(Integer id, String name, String classes, boolean morning, boolean noon, boolean evening,
			String note) {
		super();
		this.id = id;
		this.name = name;
		this.classes = classes;
		this.morning = morning;
		this.noon = noon;
		this.evening = evening;
		this.note = note;
	}
	public TeacherDutyEntry() {
		super();
	}
	public TeacherDutyEntry(Integer id, String name,String classes) {
		super();
		this.id = id;
		this.name = name;
		this.classes = classes;
		
	}
	@Override
	public String toString() {
		return "TeacherDutyEntry [id=" + id + ", name=" + name + ", classes=" + classes + ", morning=" + morning
				+ ", noon=" + noon + ", evening=" + evening + ", note=" + note + "]";
	}
	


	
	
	


	
}
