
public class PriorityCounter {
	private Activity a;
	private int score;
	private Period p;
	
	public PriorityCounter(Activity a, int score) {
		this.a = a;
		this.score = score;
		this.p = null;
	}
	
	public PriorityCounter(Period p, int score) {
		this.p = p;
		this.score = score;
		this.a = null;
	}
	
	public Activity getActivity() {
		return a;
	}
	
	public Period getPeriod() {
		return p;
	}
	
	public int getScore() {
		return score;
	}
}
