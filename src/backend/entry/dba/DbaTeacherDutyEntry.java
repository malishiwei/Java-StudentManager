package backend.entry.dba;

import java.io.Serializable;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class DbaTeacherDutyEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1. 属性区
	private Integer id;
	private String name;
	private String classes;
	private CheckBox morning;
	private CheckBox noon;
	private CheckBox evening;
	private TextField note;
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
	public CheckBox getMorning() {
		return morning;
	}
	public void setMorning(CheckBox morning) {
		this.morning = morning;
	}
	public CheckBox getNoon() {
		return noon;
	}
	public void setNoon(CheckBox noon) {
		this.noon = noon;
	}
	public CheckBox getEvening() {
		return evening;
	}
	public void setEvening(CheckBox evening) {
		this.evening = evening;
	}
	public TextField getNote() {
		return note;
	}
	public void setNote(TextField note) {
		this.note = note;
	}
	
	//2. 方法区
	public DbaTeacherDutyEntry(Integer id, String name,String classes) {
		super();
		this.id = id;
		this.name = name;
		this.classes = classes;
		this.morning = new CheckBox();
		this.noon = new CheckBox();
		this.evening = new CheckBox();
		this.note = new TextField();
	}
	
	public DbaTeacherDutyEntry(Integer id, String name, String classes, CheckBox morning, CheckBox noon, CheckBox evening,
			TextField note) {
		super();
		this.id = id;
		this.name = name;
		this.classes = classes;
		this.morning = morning;
		this.noon = noon;
		this.evening = evening;
		this.note = note;
	}
	
	
	
}
