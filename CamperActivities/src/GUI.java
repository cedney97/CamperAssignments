import java.awt.GridLayout;
import java.util.List;

import javax.swing.*;

public class GUI {

	private List<String> activities;
	private JCheckBox[][] actChecks;
	private boolean[][] checks;
	private SpinnerNumberModel[][] capacities;
	private Integer[][] caps;
	private String[] periods = { "First Period", "Second Period", "Third Period", "Fourth Period" };
	private Integer MIN = 0;
	private Integer MAX = 30;
	private Integer STEP = 1;
	private Integer VAL = 12;
	private boolean rosters;
	private int week;

	public GUI(List<String> acts) {
		activities = acts;
		actChecks = new JCheckBox[activities.size()][4];
		checks = new boolean[activities.size()][4];
		capacities = new SpinnerNumberModel[activities.size()][4];
		caps = new Integer[activities.size()][4];
		for (int i = 0; i < caps.length; ++i) {
			for (int j = 0; j < caps[0].length; ++j) {
				caps[i][j] = MAX;
			}
		}
		rosters = true;
		chooseAction();
		chooseWeek();
	}

	public boolean getAction() {
		return rosters;
	}

	public void chooseWeek() {
		JSpinner jsp = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		Object[] params = { "What week is it?", jsp };
		JOptionPane.showMessageDialog(null, params);

		week = (int) jsp.getValue();
	}

	public void chooseAction() {
		Object[] options = { "Assignments"/* , "Rosters" */ };

		int n = JOptionPane.showOptionDialog(null,
				"Would you like to assign campers or generate rosters?\nNOTE: Generating roster function will be added later.",
				"Choose Program Usage", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);
		rosters = n == 1;
	}

	public void chooseActivityPeriods() {
		JPanel checkPanel = new JPanel();
		checkPanel.setLayout(new GridLayout(0, 5));

		for (int i = 0; i < activities.size(); ++i) {
			JLabel label = new JLabel(activities.get(i));
			checkPanel.add(label);

			JCheckBox[] temp = new JCheckBox[4];
			for (int j = 0; j < 4; j++) {
				temp[j] = new JCheckBox(periods[j]);
				temp[j].setSelected(true);
				actChecks[i][j] = temp[j];
				checkPanel.add(temp[j]);
			}
		}

		Object[] params = { "Check the available activity periods", checkPanel };

		JOptionPane.showMessageDialog(null, params);

		for (int i = 0; i < activities.size(); ++i) {
			for (int j = 0; j < periods.length; ++j) {
				checks[i][j] = actChecks[i][j].isSelected();
			}
		}
	}

	public boolean[][] getChecks() {
		return this.checks;
	}

	public void chooseActivityCapicities() {
		JPanel capacityPanel = new JPanel();
		capacityPanel.setLayout(new GridLayout(0, 5));

		for (int i = 0; i < capacities.length; ++i) {
			JLabel label = new JLabel(activities.get(i));
			capacityPanel.add(label);

			VAL = getVal(activities.get(i));

			for (int j = 0; j < capacities[0].length; ++j) {
				if (!checks[i][j]) {
					JLabel empty = new JLabel(" ");
					capacityPanel.add(empty);
				} else {
					capacities[i][j] = new SpinnerNumberModel(VAL, MIN, MAX, STEP);
					JSpinner jsp = new JSpinner(capacities[i][j]);
					capacityPanel.add(jsp);

				}
			}
		}

		Object[] params = { "Input activity capacities", capacityPanel };

		JOptionPane.showMessageDialog(null, params);

		for (int i = 0; i < capacities.length; ++i) {
			for (int j = 0; j < capacities[0].length; ++j) {
				if (checks[i][j]) {
					caps[i][j] = (Integer) capacities[i][j].getValue();
				}
			}
		}
	}

	public Integer[][] getCaps() {
		return this.caps;
	}

	public int getVal(String name) {
		if (name.equals("Adventure Challenge 1")) {
			return 12;
		}
		if (name.equals("Adventure Challenge 2")) {
			return 12;
		}
		if (name.equals("Archery")) {
			return 16;
		}
		if (name.equals("Camping Classics")) {
			return 16;
		}
		if (name.equals("Crafts")) {
			return 16;
		}
		if (name.equals("FC Medley")) {
			return 24;
		}
		if (name.equals("Horsemanship - Beginner")) {
			return 10;
		}
		if (name.equals("Horsemanship - Intermediate")) {
			return 10;
		}
		if (name.equals("Horsemanship - Advanced")) {
			return 10;
		}
		if (name.equals("Kayaking")) {
			return 9;
		}
		if (name.equals("Outdoorsmanship")) {
			return 14;
		}
		if (name.equals("Riflery")) {
			return 16;
		}
		if (name.equals("Sports")) {
			return 30;
		}
		if (name.equals("Swimming")) {
			return 16;
		}
		if (name.equals("Wakeboarding")) {
			return 8;
		}
		if (name.equals("Waterskiing")) {
			return 8;
		}
		if (name.equals("Wilderness Survival Skills")) {
			return 16;
		}
		if (name.equals("Woodworking")) {
			return 12;
		}
		return 0;
	}

	public int getWeek() {
		return week;
	}
}
