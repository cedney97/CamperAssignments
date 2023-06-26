import java.util.ArrayList;
import java.util.LinkedList;

public class Camper implements Comparable<Camper> {

	private ArrayList<Period> schedule;
	private String firstName;
	private String lastName;
	private Date enrollDate;
	private LinkedList<Activity> prefs;
	private int scheduleScore;
	private boolean hasPlaceholder;
	private boolean noPrefs;
	private String id;

	public Camper(String firstName, String lastName, boolean lakePermission, Date enrollDate) {
		this.schedule = new ArrayList<>();
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollDate = enrollDate;
		this.scheduleScore = 0;
		this.hasPlaceholder = false;
		this.id = "";

		for (int i = 0; i < 4; ++i) {
			this.schedule.add(null);
		}
		noPrefs = false;
	}

	public Camper(String firstName, String lastName, Date enrollDate) {
		this.schedule = new ArrayList<>();
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollDate = enrollDate;
		this.scheduleScore = 0;
		this.hasPlaceholder = false;

		for (int i = 0; i < 4; ++i) {
			this.schedule.add(null);
		}
		noPrefs = false;
	}

	public Camper(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.schedule = new ArrayList<>();
		this.enrollDate = null;
		this.prefs = null;
		this.scheduleScore = 0;
		this.hasPlaceholder = false;

		for (int i = 0; i < 4; ++i) {
			this.schedule.add(null);
		}
	}

	public boolean getPlaceholder() {
		return this.hasPlaceholder;
	}

	public void setPlaceholder(boolean p) {
		this.hasPlaceholder = p;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public int getScore() {
		return this.scheduleScore;
	}

	public void setScore(int score) {
		this.scheduleScore = score;
	}

	public void addScore(int add) {
		this.scheduleScore += add;
	}

	public LinkedList<Activity> getPrefs() {
		return this.prefs;
	}

	public void addPrefs(LinkedList<Activity> prefs) {
		if (prefs == null) {
			noPrefs = true;
		}
		this.prefs = prefs;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public ArrayList<Period> getSchedule() {
		return this.schedule;
	}

	public Date getEnrollDate() {
		return this.enrollDate;
	}

	public boolean addPeriod(Period p, int n) {
		if (schedule.get(n - 1) != null) {
			return false;
		}
		schedule.add(n - 1, p);
		return true;
	}

	public boolean enrollCamper(Period p) {
		if (schedule.get(p.getOrder() - 1) == null) {
			if (p.getEnrolled() != p.getCapacity()) {
				if (p.enrollCamper(this)) {
					this.schedule.remove(p.getOrder() - 1);
					this.schedule.add(p.getOrder() - 1, p);
					return true;
				}
			}
		}
		return false;
	}

	public void addSchedule(ArrayList<Period> sched) {
		this.schedule = sched;
	}

	@Override
	public int compareTo(Camper c) {
		int compare = this.enrollDate.compareTo(c.getEnrollDate());
		if (compare != 0) {
			return compare;
		} else {
			int compare2 = this.getLastName().compareTo(c.getLastName());
			if (compare2 != 0) {
				return compare2;
			} else {
				return this.getFirstName().compareTo(c.getFirstName());
			}
		}
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public boolean hasPrefs() {
		return !noPrefs;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}
}
