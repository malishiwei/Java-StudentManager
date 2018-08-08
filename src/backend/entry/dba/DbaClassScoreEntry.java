package backend.entry.dba;

import java.io.Serializable;

public class DbaClassScoreEntry implements Serializable{	
	//1. 属性区
	private static final long serialVersionUID = 1L;
	private String classes;
	private String mean;
	private String max;
	private String min;
	private String std;
	private String count;
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public String getMean() {
		return mean;
	}
	public void setMean(String mean) {
		this.mean = mean;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getStd() {
		return std;
	}
	public void setStd(String std) {
		this.std = std;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	
	
	//2. 构造方法区
	public DbaClassScoreEntry() {
		super();
	}
	public DbaClassScoreEntry(String classes, String max, String min,  String mean,String std, String count) {
		super();
		this.classes = classes;
		this.mean = mean;
		this.max = max;
		this.min = min;
		this.std = std;
		this.count = count;
	}
	
	

	
}
