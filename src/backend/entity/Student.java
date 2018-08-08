package backend.entity;

import java.io.Serializable;

public class Student extends User implements Serializable{
	//1. ������
	private static final long serialVersionUID = 1L;
	
	//2. ���췽����
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
	
	
	//3. ������
	@Override
	public String toString() {
		return "ѧ��������" + getName() + "\t�Ա�" + getGender() + "\t�༶��" + getClasses()
				+ "\tѧ��" + getId() + "\t�绰��" + getPhone() + "\t��ͥסַ��" + getHomeAddress()
				+ "\t��ϵ�ˣ�" + getContactsName() + "\t��ϵ�˵绰��" + getContactsPhone()
				;
	}

	
	
}
