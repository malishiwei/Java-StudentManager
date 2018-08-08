package backend.entry.dba;


import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class DbaSearchResultEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1. 属性区
	private TextField text_id;
	private TextField text_name;
	private TextField text_usertype;
	private Button button_delete;
	public Button getButton_delete() {
		return button_delete;
	}
	public void setButton_delete(Button button_delete) {
		this.button_delete = button_delete;
	}
	public Button getButton_modify() {
		return button_modify;
	}
	public void setButton_modify(Button button_modify) {
		this.button_modify = button_modify;
	}
	private Button button_modify;
	
	public TextField getText_id() {
		return text_id;
	}
	public void setText_id(TextField text_id) {
		this.text_id = text_id;
	}

	public TextField getText_name() {
		return text_name;
	}

	public void setText_name(TextField text_name) {
		this.text_name = text_name;
	}

	public TextField getText_usertype() {
		return text_usertype;
	}

	public void setText_usertype(TextField text_usertype) {
		this.text_usertype = text_usertype;
	}

	//2. 构造方法
	public DbaSearchResultEntry() {
		this.text_id = new TextField("");
		this.text_name = new TextField("");
		this.text_usertype = new TextField("");
		this.button_delete = new Button("删除",
				new ImageView("file:/StudentManager/src/ui/images/delete.jpg"));
		this.button_modify = new Button("修改",
				new ImageView("file:/StudentManager/src/ui/images/modify.jpg"));
	}
	public DbaSearchResultEntry(ArrayList<String> list) {
		this.text_id = new TextField(list.get(0));
		this.text_name = new TextField(list.get(1));
		this.text_usertype = new TextField(list.get(2));
		this.button_delete = new Button("删除",
				new ImageView("file:/StudentManager/src/ui/images/delete.jpg"));
		this.button_modify = new Button("修改",
				new ImageView("file:/StudentManager/src/ui/images/modify.jpg"));
	}
	public DbaSearchResultEntry(TextField text_id, TextField text_name, TextField text_usertype, Button button_delete,
			Button button_modify) {
		super();
		this.text_id = text_id;
		this.text_name = text_name;
		this.text_usertype = text_usertype;
		this.button_delete = button_delete;
		this.button_modify = button_modify;
	}
	


	
}
