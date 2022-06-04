
public class Date implements Comparable<Date> {
	private int day;
	private int month;
	private int year;
	
	public Date(int month, int day, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;
	}

	@Override
	public int compareTo(Date d) {
		if (this.year < d.getYear()) {
			return -1;
		}
		if (this.year > d.getYear()) {
			return 1;
		}
		if (this.month < d.getMonth()) {
			return -1;
		}
		if (this.month > d.getMonth()) {
			return 1;
		}
		if (this.day < d.getDay()) {
			return -1;
		}
		if (this.day > d.getDay()) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return month + "/" + day + "/" + year;
	}
}
