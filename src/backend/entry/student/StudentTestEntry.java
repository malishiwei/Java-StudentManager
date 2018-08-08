package backend.entry.student;

import java.io.Serializable;

public class StudentTestEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1. 属性区
	private int id;
	private String name;
	private String classes;
	private String test;
	private String score;
	private String chinese;
	private String math;
	private String english;
	private String physics;
	private String chemistry;
	private String biology;
	private String politics;
	private String history;
	private String geology;
	private String rank;
	//接口区
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getMath() {
		return math;
	}

	public void setMath(String math) {
		this.math = math;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getPhysics() {
		return physics;
	}

	public void setPhysics(String physics) {
		this.physics = physics;
	}

	public String getChemistry() {
		return chemistry;
	}

	public void setChemistry(String chemistry) {
		this.chemistry = chemistry;
	}

	public String getBiology() {
		return biology;
	}

	public void setBiology(String biology) {
		this.biology = biology;
	}

	public String getPolitics() {
		return politics;
	}

	public void setPolitics(String politics) {
		this.politics = politics;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getGeology() {
		return geology;
	}

	public void setGeology(String geology) {
		this.geology = geology;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}
	

	//2. 构造方法
	public StudentTestEntry() {
		super();
	}

	public StudentTestEntry(int id, String name, String classes, String test, String score, String chinese, String math,
			String english, String physics, String chemistry, String biology, String politics, String history,
			String geology, String rank) {
		super();
		this.id = id;
		this.name = name;
		this.classes = classes;
		this.test = test;
		this.score = score;
		this.chinese = chinese;
		this.math = math;
		this.english = english;
		this.physics = physics;
		this.chemistry = chemistry;
		this.biology = biology;
		this.politics = politics;
		this.history = history;
		this.geology = geology;
		this.rank = rank;
	}


	//3. 方法区
	@Override
	public String toString() {
		return "StudentTestEntry [id=" + id + ", name=" + name + ", classes=" + classes + ", test=" + test + ", score="
				+ score + ", chinese=" + chinese + ", math=" + math + ", english=" + english + ", physics=" + physics
				+ ", chemistry=" + chemistry + ", biology=" + biology + ", politics=" + politics + ", history="
				+ history + ", geology=" + geology + ", rank=" + rank + "]";
	}

	
	
}
