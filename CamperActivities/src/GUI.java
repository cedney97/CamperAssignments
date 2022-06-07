import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

public class GUI {
	
	private List<String> activities;
	private JCheckBox[][] actChecks;
	private boolean[][] checks;
	private SpinnerNumberModel[][] capacities;
	private Integer[][] caps;
	private String[] periods = {"First Period", "Second Period", "Third Period", "Fourth Period"};
	private Integer MIN = 0;
	private Integer MAX = 20;
	private Integer STEP = 1;
	private Integer VAL = 0;
	
	public GUI(List<String> acts) {
		activities = acts;
		actChecks = new JCheckBox[activities.size()][4];
		checks = new boolean[activities.size()][4];
		capacities = new SpinnerNumberModel[activities.size()][4];
		caps = new Integer[activities.size()][4];
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
		
		Object[] params = {"Check the available activity periods", checkPanel};
		
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
			
			for (int j = 0; j < capacities[0].length; ++j) {
				capacities[i][j] = new SpinnerNumberModel(VAL, MIN, MAX, STEP);
				JSpinner jsp = new JSpinner(capacities[i][j]);
				capacityPanel.add(jsp);
			}
		}
		
		Object[] params = {"Input activity capacities", capacityPanel};
		
		JOptionPane.showMessageDialog(null, params);
		
		for (int i = 0; i < capacities.length; ++i) {
			for (int j = 0; j < capacities[0].length; ++j) {
				caps[i][j] = (Integer) capacities[i][j].getValue();
			}
		}
	}
	
	public Integer[][] getCaps() {
		return this.caps;
	}
}
