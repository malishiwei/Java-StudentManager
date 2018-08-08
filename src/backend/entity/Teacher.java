package backend.entity;

import java.io.Serializable;

public class Teacher extends User implements Serializable{
	private static final long serialVersionUID = 1L;

	public Teacher() {
		super();
	}

	public Teacher(String name, String gender, int classes, int id, String phone, String homeAddress, String contactsName,
			String contactsPhone) {
		super(name, gender, classes, id, phone, homeAddress, contactsName, contactsPhone);
	}
	
	public Teacher(Student stu) {
		super(stu.getName(),stu.getGender(),stu.getClasses(),stu.getId(),stu.getPhone(),
				stu.getHomeAddress(),stu.getContactsName(),stu.getContactsPhone());
	}
	
	//3. 方法区
	@Override
	public String toString() {
		return "教师姓名：" + getName() + "\t性别：" + getGender() + "\t班级：" + getClasses()
				+ "\t学号" + getId() + "\t电话：" + getPhone() + "\t家庭住址：" + getHomeAddress()
				+ "\t联系人：" + getContactsName() + "\t联系人电话：" + getContactsPhone()
				;
	}
	
	
}
