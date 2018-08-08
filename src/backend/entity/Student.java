package backend.entity;

import java.io.Serializable;

public class Student extends User implements Serializable{
	//1. 属性区
	private static final long serialVersionUID = 1L;
	
	//2. 构造方法区
	public Student() {
		super();
	}

	public Student(String name, String gender, int classes, int id, String phone, String homeAddress, String contactsName,
			String contactsPhone) {
		super(name, gender, classes, id, phone, homeAddress, contactsName, contactsPhone);
	}
	
	public Student(Teacher teacher) {
		super(teacher.getName(),teacher.getGender(),teacher.getClasses(),teacher.getId(),teacher.getPhone(),
				teacher.getHomeAddress(),teacher.getContactsName(),teacher.getContactsPhone());
	}
	
	
	//3. 方法区
	@Override
	public String toString() {
		return "学生姓名：" + getName() + "\t性别：" + getGender() + "\t班级：" + getClasses()
				+ "\t学号" + getId() + "\t电话：" + getPhone() + "\t家庭住址：" + getHomeAddress()
				+ "\t联系人：" + getContactsName() + "\t联系人电话：" + getContactsPhone()
				;
	}

	
	
}
