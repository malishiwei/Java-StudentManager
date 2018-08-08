package backend.entity;

import java.io.Serializable;

public class Account implements Serializable{
	//1. ������
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private int id;
	private String usertype = "ѧ��"; //�˺�Ĭ��ѧ��
	
	public String getuserName() {
		return userName;
	}
	public void setuserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	
	
	//2. ���췽��
	
	public Account() {
		super();
	}
	public Account(String userName, String password, int id, String usertype) {
		super();
		this.userName = userName;
		this.password = password;
		this.id = id;
		this.usertype = usertype;
	}
	
	public Account(String userName, String password, int id) {
		super();
		this.userName = userName;
		this.password = password;
		this.id = id;
	}
	
	//3. ������
	@Override
	public String toString() {
		return "Account [userName=" + userName + ", password=" + password + ", id=" + id + ", usertype=" + usertype
				+ "]";
	}
	
	
}
