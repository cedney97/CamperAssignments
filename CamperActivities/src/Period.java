
public class Period implements Comparable<Period>{
	private String name;
	private int capacity;
	private int enrolled;
	private Camper[] roster;
	private int order;
	
	public Period (String name, int capacity, int enrolled, int order) {
		this.name = name;
		this.capacity = capacity;
		this.enrolled = enrolled;
		roster = new Camper[capacity];
		this.order = order;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public int getEnrolled() {
		return this.enrolled;
	}
	
	public int getRemaining() {
		return this.capacity - this.enrolled;
	}
	
	public int getOrder() {
		return order;
	}
	
	public boolean enrollCamper(Camper toEnroll) {
		
		for (int i = 0; i < roster.length; ++i) {
			Camper c = roster[i];
			if (toEnroll.equals(c)) {
				return false;
			}
			if (c == null) {
				roster[i] = toEnroll;
				++this.enrolled;
				return true;
			}
		}
		return false;
	}
	
	public Camper[] getRoster() {
		return this.roster;
	}
	
	@Override
	public int compareTo(Period p) {
		if (this.getEnrolled() < p.getEnrolled()) {
			return 1;
		}
		if (this.getEnrolled() > p.getEnrolled()) {
			return -1;
		}
		return 0;
	}
}
