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
	
	//3. ������
	@Override
	public String toString() {
		return "��ʦ������" + getName() + "\t�Ա�" + getGender() + "\t�༶��" + getClasses()
				+ "\tѧ��" + getId() + "\t�绰��" + getPhone() + "\t��ͥסַ��" + getHomeAddress()
				+ "\t��ϵ�ˣ�" + getContactsName() + "\t��ϵ�˵绰��" + getContactsPhone()
				;
	}
	
	
}
