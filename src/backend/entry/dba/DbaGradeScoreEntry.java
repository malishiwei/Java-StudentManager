package backend.entry.dba;

import java.io.Serializable;

public class DbaGradeScoreEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//1， 属性区
	private String statistics;
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
	
	

	public String getStatistics() {
		return statistics;
	}

	public void setStatistics(String statistics) {
		this.statistics = statistics;
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

	//2. 构造方法
	public DbaGradeScoreEntry() {
		super();	
	}

	public DbaGradeScoreEntry(String statistics, String score, String chinese, String math, String english, String physics,
			String chemistry, String biology, String politics, String history, String geology) {
		super();
		this.statistics = statistics;
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
	}

	public DbaGradeScoreEntry(String statistics) {
		super();
		this.statistics = statistics;
	}



	


	
}
