import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activity {
	private Period[] periods;
	private String name;
	
	public Activity(String name, List<Integer> offPeriods, int individualCapacity) {
		this.name = name;
		this.periods = new Period[4];
		
		for (int i = 0; i < periods.length; ++i) {
			periods[i] = new Period(name + " " + (i + 1), individualCapacity, 0, i + 1);
		}

		if (offPeriods != null) {
			for (int p : offPeriods) {
				periods[p - 1] = null;
			}
		}
	}

	public Activity(String name, List<Integer> offPeriods, List<Integer> capacities) {
		this.name = name;
		this.periods = new Period[4];

		int capCounter = 0;
		for (int i = 0; i < periods.length; ++i) {
			if (offPeriods != null) {
				if (!offPeriods.contains(i + 1)) {
					periods[i] = new Period(name + " " + (i + 1), capacities.get(capCounter), 0, i + 1);
					++capCounter;
				}
			} else {
				periods[i] = new Period(name + " " + (i + 1), capacities.get(capCounter), 0, i + 1);
				++capCounter;
			}
		}
	}

	public boolean hasPeriod(int period) {
		return periods[period] != null;
	}
	
	public Period getPeriod(int n) {
		return periods[n];
	}
	
	public Period[] getPeriods() {
		return this.periods;
	}
	
	public int getTotalPeriods() {
		int counter = 0;
		for (Period p : periods) {
			if (p != null) {
				++counter;
			}
		}
		return counter;
	}
	
	public String getName() {
		return this.name;
	}

	public ArrayList<Period> getPeriodsLowToHighEnroll() {
		ArrayList<Period> periods = new ArrayList<>();
		for (Period p : this.periods) {
			if (p != null) {
				periods.add(p);
			}
		}
		Collections.sort(periods);
		
		return periods;
	}
}
