import java.awt.GridLayout;
import java.util.Collections;
import java.util.Set;

import javax.swing.*;

public class GUI {
	
	public static void createGUI(Set<String> acts) {
		JFrame frame = new JFrame("Set the available activities and class sizes!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
				
		GridLayout gl = new GridLayout(18, 5);
		frame.setLayout(gl);
		
		for (String name : acts) {
			JLabel label = new JLabel(name);
			frame.add(label);
			
			String[] periods = {"First Period", "Second Period", "Third Period", "Fourth Period"};
			JCheckBox[] boxes = new JCheckBox[periods.length];
			for (int i = 0; i < boxes.length; i++) {
				boxes[i] = new JCheckBox(periods[i]);
				frame.add(boxes[i]);
			}
		}
		
		frame.setVisible(true);
		frame.setResizable(true);
	}
	
	public static void main(String args[]) {
		
//		JFrame frame = new JFrame("My First GUI");
//		
//		GridLayout gl = new GridLayout(1, 5);
//		
//		frame.setLayout(gl);
//		
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(400, 300);
//		
//		JLabel l1 = new JLabel("Adventure Challenge 1");
//		frame.add(l1);
//		
//		String[] periods = {"First Period", "Second Period", "Third Period", "Fourth Period"};
//		
//		JCheckBox[] boxes = new JCheckBox[periods.length];
//		
//		for (int i = 0; i < boxes.length; i++) {
//			boxes[i] = new JCheckBox(periods[i]);
//			frame.add(boxes[i]);
//		}
//		
//		frame.setVisible(true);
//		frame.setResizable(true);
	}

}
