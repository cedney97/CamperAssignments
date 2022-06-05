import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.*;

public class GUI {
	
	private JFrame frame;
	private JCheckBox[][] actChecks;
	private boolean[][] checks;
	private JButton submit;
	
	public GUI() {
		frame = new JFrame("For Camper Assignment!");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
		frame.setVisible(false);
		frame.setResizable(true);
	}
	
	public void chooseActivityPeriods(Set<String> acts) {
		List<String> activities = new LinkedList<>();
		
		for (String s : acts) {
			activities.add(s);
		}
		
		Collections.sort(activities);
	
		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		
		frame.setTitle("Check the Activity Periods Available!");
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(0, 5));
	
		String[] periods = {"First Period", "Second Period", "Third Period", "Fourth Period"};
		
		actChecks = new JCheckBox[activities.size()][periods.length];
		
		for (int i = 0; i < activities.size(); ++i) {
			JLabel label = new JLabel(activities.get(i));
			centerPanel.add(label);
			
			JCheckBox[] temp = new JCheckBox[4];
			for (int j = 0; j < 4; j++) {
				temp[j] = new JCheckBox(periods[j]);
				temp[j].setSelected(true);
				actChecks[i][j] = temp[j];
				centerPanel.add(temp[j]);
			}
		}
		
		contentPane.add(centerPanel, BorderLayout.PAGE_START);
		
		JPanel footerPanel = new JPanel();
		
		submit = new JButton("Submit");
		
		footerPanel.add(submit);
		contentPane.add(footerPanel, BorderLayout.PAGE_END);
		
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		frame.add(submit);

		frame.setVisible(true);
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.out.println(actChecks[0][0].isSelected());
			}
		});
	}
	
	public boolean[][] getChecks() {
		return this.checks;
	}

}
