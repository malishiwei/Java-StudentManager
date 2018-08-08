package backend.entity;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String gender;
	private int classes;
	private int id;
	private String phone;
	private String homeAddress;
	private String contactsName;
	private String contactsPhone;
	
	public User() {
		super();
	}
	public User(String name, String gender, int classes, int id, String phone, String homeAddress, String contactsName,
			String contactsPhone) {
		super();
		this.name = name;
		this.gender = gender;
		this.classes = classes;
		this.id = id;
		this.phone = phone;
		this.homeAddress = homeAddress;
		this.contactsName = contactsName;
		this.contactsPhone = contactsPhone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getClasses() {
		return classes;
	}
	public void setClasses(int classes) {
		this.classes = classes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getContactsName() {
		return contactsName;
	}
	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	public String getContactsPhone() {
		return contactsPhone;
	}
	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}
	
	
}
