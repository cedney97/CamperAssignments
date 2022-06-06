import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	private final static JFileChooser fc = new JFileChooser();
	private static Map<String, Activity> activities = new HashMap<>();
	private static List<String> activitiesAlpha = new LinkedList<>();
	private static LinkedList<Camper> campers = new LinkedList<>();
	private static Period PLACEHOLDER = new Period(" ", 0, 0, 0);
	private static boolean alreadyWakeSki = false;
	private static boolean alreadyHorse = false;
	private static boolean alreadyAC = false;

	public static void main(String[] args) throws FileNotFoundException {
		fc.setName("Pick the camper report");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
		fc.setFileFilter(filter);
		fc.setDialogTitle("Choose your camper report file");

		int returnValue = fc.showOpenDialog(null);
		File selectedFile = null;
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = fc.getSelectedFile();
			System.out.println("Selected File: " + selectedFile.getName() + "\n");
		} else {
			System.exit(1);
		}

		Scanner camperInfo = new Scanner(selectedFile);
//		camperInfo.nextLine(); // Skip first line, it's titles

		generateActivities();
		mapCampersToPrefs(camperInfo);
		
		GUI gui = new GUI(activitiesAlpha);
		gui.chooseActivityPeriods();
		
		boolean[][] checks = gui.getChecks();
		for (int i = 0; i < checks.length; ++i) {
			for (int j = 0; j < checks[0].length; ++j) {
				System.out.print(checks[i][j] + "\t");
			}
			System.out.println();
		}

		Collections.sort(campers);

//		printCamperPrefs();

		assignCampers();
//		printCamperSchedules();
		generateCSV();
		camperInfo.close();
	}

	public static void printCamperSchedules() {
		for (Camper c : campers) {
			System.out.println(c.getName() + "'s Schedule:");
			for (int i = 0; i < c.getSchedule().size(); ++i) {
				Period p = c.getSchedule().get(i);
				if (i != 0) {
					System.out.print(",\t\t");
				}
				System.out.print(p.getName());
			}
			System.out.println("\tWith Score: " + c.getScore() + "\n--------------");
		}
	}

	public static void assignCampers() {
		for (Camper c : campers) {
			alreadyWakeSki = false;
			alreadyHorse = false;
			alreadyAC = false;
			LinkedList<Activity> prefs = c.getPrefs();
			ArrayList<PriorityCounter> prefsWithPriority = new ArrayList<PriorityCounter>();
			int priority = 6;
			for (Activity a : prefs) {
				prefsWithPriority.add(new PriorityCounter(a, priority));
				--priority;
			}

			PriorityCounter[] firstFour = { prefsWithPriority.get(0), prefsWithPriority.get(1),
					prefsWithPriority.get(2), prefsWithPriority.get(3) };

			// Sort prefs by number of periods
			bubbleSort(firstFour);

			// Go through each activity
			for (PriorityCounter pc : firstFour) {
				enrollCamper(c, pc);
			}

			if (hasNulls(c.getSchedule())) {
				PriorityCounter fifth = prefsWithPriority.get(4);
				PriorityCounter sixth = prefsWithPriority.get(5);

				if (fifth.getActivity().getTotalPeriods() > sixth.getActivity().getTotalPeriods()) {
					enrollCamper(c, fifth);
					if (hasNulls(c.getSchedule())) {
						enrollCamper(c, sixth);
					}
				} else {
					enrollCamper(c, sixth);
					if (hasNulls(c.getSchedule())) {
						enrollCamper(c, fifth);
					}
				}
			}

			ArrayList<Period> sched = c.getSchedule();
			for (int i = 0; i < sched.size(); ++i) {
				Period p = sched.get(i);
				if (p == null) {
					c.setPlaceholder(true);
					sched.remove(i);
					sched.add(i, PLACEHOLDER);
				}
			}

			c.addSchedule(sched);
//			System.out.println(c.getName() + " Done");
		}
	}

	public static void generateActivities() {
		activities.put("Adventure Challenge 1", new Activity("Adventure Challenge 1", Arrays.asList(1, 2), 12));
		activitiesAlpha.add("Adventure Challenge 1");
		activities.put("Adventure Challenge 2", new Activity("Adventure Challenge 2", null, 12));
		activitiesAlpha.add("Adventure Challenge 2");
		activities.put("Archery", new Activity("Archery", null, 12));
		activitiesAlpha.add("Archery");
		activities.put("Camping Classics",
				new Activity("Camping Classics", Arrays.asList(3, 4), Arrays.asList(12, 16)));
		activitiesAlpha.add("Camping Classics");
		activities.put("Crafts", new Activity("Crafts", Arrays.asList(1, 2), 12));
		activitiesAlpha.add("Crafts");
		activities.put("FC Medley", new Activity("FC Medley", null, Arrays.asList(12, 16, 12, 12)));
		activitiesAlpha.add("FC Medley");
		activities.put("Horsemanship - Beginner",
				new Activity("Horsemanship - Beginner", Arrays.asList(4), Arrays.asList(10, 9, 10)));
		activitiesAlpha.add("Horsemanship - Beginner");
		activities.put("Horsemanship - Intermediate",
				new Activity("Horsemanship - Intermediate", Arrays.asList(4), Arrays.asList(6, 6, 10)));
		activitiesAlpha.add("Horsemanship - Intermediate");
		activities.put("Horsemanship - Advanced", new Activity("Horsemanship - Advanced", null, 8));
		activitiesAlpha.add("Horsemanship - Advanced");
		activities.put("Kayaking", new Activity("Kayaking", null, 10));
		activitiesAlpha.add("Kayaking");
		activities.put("Outdoorsmanship", new Activity("Outdoorsmanship", Arrays.asList(1, 3), 12));
		activitiesAlpha.add("Outdoorsmanship");
		activities.put("Riflery", new Activity("Riflery", null, Arrays.asList(16, 12, 16, 12)));
		activitiesAlpha.add("Riflery");
		activities.put("Sports", new Activity("Sports", Arrays.asList(1, 3), 24));
		activitiesAlpha.add("Sports");
		activities.put("Swimming", new Activity("Swimming", null, 10));
		activitiesAlpha.add("Swimming");
		activities.put("Wakeboarding", new Activity("Wakeboarding", null, 9));
		activitiesAlpha.add("Wakeboarding");
		activities.put("Waterskiing", new Activity("Waterskiing", null, 6));
		activitiesAlpha.add("Waterskiing");
		activities.put("Wilderness Survival Skills",
				new Activity("Wilderness Survival Skills", null, Arrays.asList(12, 12, 16, 16)));
		activitiesAlpha.add("Wilderness Survival Skills");
		activities.put("Woodworking", new Activity("Woodworking", null, Arrays.asList(16, 16, 8, 8)));
		activitiesAlpha.add("Woodworking");
	}

	public static void mapCampersToPrefs(Scanner camperInfo) {

		// While there are campers (lines) in the csv file
		while (camperInfo.hasNextLine()) {
			// Get line contents and put into Scanner
			String line = camperInfo.nextLine();
			Scanner lineScanner = new Scanner(line);

			// Use comma as delimiter
			lineScanner.useDelimiter(",");

			// Get date, put into String, which is put into a Scanner
			String dateRaw = lineScanner.next();
			Scanner scDate = new Scanner(dateRaw);
			scDate.useDelimiter("/");

			// Put data from Scanner into a Date object, to be used with the Camper
			Date enrollDate = new Date(scDate.nextInt(), scDate.nextInt(), scDate.nextInt());

			// Skip CampMinder ID
			lineScanner.next();

			// Get first name, last name, and lake permission
			// Once received, create Camper out of that data
			String lastName = lineScanner.next();
			String firstName = lineScanner.next();
			String lakePermission = lineScanner.next();
			Camper c = new Camper(firstName, lastName, lakePermission.equals("Yes"), enrollDate);

			// Create empty list of preferences for the Camper
			LinkedList<Activity> prefs = new LinkedList<>();

			// Get preferences String from .csv, put into Scanner, and use either ", " or
			// "and " as delimiter
			String prefRaw = lineScanner.nextLine();
			prefRaw = prefRaw.substring(2, prefRaw.length() - 1);
			Scanner scPref = new Scanner(prefRaw);
			scPref.useDelimiter(", | and ");

			// Separate each preference and put into list
			while (scPref.hasNext()) {
				prefs.add(activities.get(scPref.next()));
			}

			// Add Camper and preferences to list and map, close Scanners
			c.addPrefs(prefs);
			campers.add(c);
			lineScanner.close();
			scPref.close();
			scDate.close();
		}
	}

	public static void printCamperPrefs() {
		for (Camper c : campers) {
			System.out.println(c.getName() + "'s Preferences (Enroll Date = " + c.getEnrollDate() + "):");
			LinkedList<Activity> prefs = c.getPrefs();
			Activity head = prefs.getFirst();
			for (Activity a : prefs) {
				if (!a.equals(head)) {
					System.out.print(", ");
				}
				System.out.print(a.getName());
			}
			System.out.println("\n--------------");
		}
	}

	public static void bubbleSort(PriorityCounter[] a) {
		int n = a.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (a[j].getActivity().getTotalPeriods() > a[j + 1].getActivity().getTotalPeriods()) {
					PriorityCounter temp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = temp;
				}
			}
		}
	}

	public static boolean hasNulls(ArrayList<Period> a) {
		for (Period p : a) {
			if (p == null) {
				return true;
			}
		}
		return false;
	}

	public static void enrollCamper(Camper c, PriorityCounter pc) {
		if (checkWakeSki(pc.getActivity().getName()) || checkHorse(pc.getActivity().getName())) {

			if (pc.getActivity().getName().equals("Adventure Challenge 2") && alreadyAC) {
				return;
			}

			if (pc.getActivity().getName().equals("Adventure Challenge 1")) {
				if (c.getPrefs().contains(activities.get("Adventure Challenge 2"))) {
					pc = new PriorityCounter(activities.get("Adventure Challenge 2"), pc.getScore());
				}
			}

			ArrayList<Period> avails = pc.getActivity().getPeriodsLowToHighEnroll();

			boolean enrolled = false;
			for (Period p : avails) {
				enrolled = c.enrollCamper(p);
				if (enrolled) {
					break;
				}
			}
			if (enrolled) {
				c.addScore(pc.getScore());
			}
		} else {
			ArrayList<Period> avails = pc.getActivity().getPeriodsLowToHighEnroll();

			int i = 0;
			boolean enrolled = false;
			while (!enrolled && i < avails.size()) {
				Period p = avails.get(i);
				enrolled = c.enrollCamper(p);
				++i;
			}
			if (enrolled) {
				c.addScore(pc.getScore());
			}
		}

		if (checkWakeSki(pc.getActivity().getName())) {
			alreadyWakeSki = true;
		}

		if (checkAC(pc.getActivity().getName())) {
			alreadyAC = true;
		}

		if (checkHorse(pc.getActivity().getName())) {
			alreadyHorse = true;
		}
	}

	public static boolean checkWakeSki(String name) {
		return (name.equals("Wakeboarding") || name.equals("Waterskiing")) && !alreadyWakeSki;
	}

	public static boolean checkAC(String name) {
		return (name.equals("Adventure Challenge 1") || name.equals("Adventure Challenge 2")) && !alreadyAC;
	}

	public static boolean checkHorse(String name) {
		return (name.equals("Horsemanship - Advanced") || name.equals("Horsemanship - Intermediate")
				|| name.equals("Horsemanship - Beginner")) && !alreadyHorse;
	}

	public static void generateCSV() throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter("camperAssignments.csv")) {
			StringBuilder sb = new StringBuilder();
			sb.append("Last Name");
			sb.append(",");
			sb.append("First Name");
			sb.append(",");
			sb.append("Activity 1");
			sb.append(",");
			sb.append("Activity 2");
			sb.append(",");
			sb.append("Activity 3");
			sb.append(",");
			sb.append("Activity 4");
			sb.append(",");
			sb.append("Quality Score");
			sb.append(",");
			sb.append("Quality Score as %");
			sb.append(",");
			sb.append("Has Empty Activity?");
			sb.append("\n\n");

			for (Camper c : campers) {
				sb.append(c.getLastName());
				sb.append(",");
				sb.append(c.getFirstName());
				sb.append(",");
				for (Period p : c.getSchedule()) {
					if (p.equals(PLACEHOLDER)) {
						sb.append(p.getName());
					} else {
						sb.append(p.getName().substring(0, p.getName().length() - 2));
					}
					sb.append(",");
				}
				sb.append(c.getScore());
				sb.append(",");
				sb.append(Math.round(c.getScore() / 18.0 * 10000) / 100.0 + "%");
				sb.append(",");
				sb.append(c.getPlaceholder() ? "Yes" : " ");
				sb.append("\n");
			}

			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
