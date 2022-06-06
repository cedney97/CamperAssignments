import java.awt.BorderLayout;
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
	private J
	private String[] periods = {"First Period", "Second Period", "Third Period", "Fourth Period"};
	
	public GUI(List<String> acts) {
		activities = acts;
		actChecks = new JCheckBox[activities.size()][4];
		checks = new boolean[activities.size()][4];
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
		
		
	}
}
