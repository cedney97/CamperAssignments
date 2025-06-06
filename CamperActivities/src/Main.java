import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
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
	private static boolean debug = false;

    private final static List<String> ropes = new LinkedList<String>(Arrays.asList("Adventure Challenge 1", "Adventure Challenge 2"));
    private final static List<String> lake = new LinkedList<String>(Arrays.asList("Camping Classics", "Outdoorsmanship", "Wakeboarding", "Waterskiing", "Kayaking"));
    private final static List<String> wakeSki = new LinkedList<String>(Arrays.asList("Wakeboarding", "Waterskiing"));
    private final static List<String> horses = new LinkedList<String>(Arrays.asList("Intro to Horsemanship", "Horsemanship - Beginner", "Horsemanship - Intermediate", "Horsemanship - Advanced"));
	public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
		generateActivities();

		GUI gui = new GUI(activitiesAlpha);

		if (!gui.getAction()) {

			fc.setName("Pick the camper report");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
			fc.setFileFilter(filter);
			fc.setDialogTitle("Choose your camper report file");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			int returnValue = fc.showOpenDialog(null);
			File selectedFile = null;
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fc.getSelectedFile();
				System.out.println("Selected File: " + selectedFile.getName() + "\n");
			} else {
				System.exit(1);
			}

			Scanner camperInfo = new Scanner(selectedFile);
			camperInfo.nextLine(); // Skip first line, it's titles

			gui.chooseActivityPeriods();
			gui.chooseActivityCapicities();

			boolean[][] checks = gui.getChecks();

			Integer[][] caps = gui.getCaps();

			generateActivitiesMap(checks, caps);

			mapCampersToPrefs(camperInfo);

			Collections.sort(campers);

			assignCampers();
			generateCSV();
			camperInfo.close();
		} else {
			fc.setName("Pick the Activity Assignments");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
			fc.setFileFilter(filter);
			fc.setDialogTitle("Choose your Assignment file");

			int returnValue = fc.showOpenDialog(null);
			File selectedFile = null;
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fc.getSelectedFile();
				System.out.println("Selected File: " + selectedFile.getName() + "\n");
			} else {
				System.exit(1);
			}

			generateFullMaxActivities();

			Scanner camperInfo = new Scanner(selectedFile);
			camperInfo.nextLine(); // Skip first line, it's titles
			camperInfo.nextLine(); // There's also one extra line (I put it there haha)
			getCamperSchedules(camperInfo);

			for (String s : activitiesAlpha) {
				DocumentCreator.createRoster(activities.get(s), gui.getWeek());
			}
		}
	}

	public static void getCamperSchedules(Scanner camperInfo) {
		while (camperInfo.hasNextLine()) {
			String line = camperInfo.nextLine();
			Scanner lineScanner = new Scanner(line);
			if (line.equals(",,,,,,,,,")) {
				continue;
			}
			lineScanner.useDelimiter(",");

			String enrollDate = lineScanner.next();
			String lastName = lineScanner.next();
			String firstName = lineScanner.next();

			Scanner scDate = new Scanner(enrollDate);
			scDate.useDelimiter("/");

			Date enroll = new Date(scDate.nextInt(), scDate.nextInt(), scDate.nextInt());

			scDate.close();

			Camper c = new Camper(firstName, lastName, enroll);

			for (int i = 0; i < 4; ++i) {
				String activity = lineScanner.next();
				Activity a = activities.get(activity);
				Period p = a.getPeriod(i);
				c.enrollCamper(p);
			}

			c.setScore(Integer.parseInt(lineScanner.next()));

			campers.add(c);
			lineScanner.close();
		}
	}

	public static void generateFullMaxActivities() {

		for (String s : activitiesAlpha) {
			activities.put(s, new Activity(s, null, 30));
		}
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
			System.out.println(c.getName());
			alreadyWakeSki = false;
			alreadyHorse = false;
			alreadyAC = false;
			if (c.hasPrefs()) {
				LinkedList<Activity> prefs = c.getPrefs();
				ArrayList<PriorityCounter> prefsWithPriority = new ArrayList<PriorityCounter>();
				int priority = prefs.size();
				for (Activity a : prefs) {
					prefsWithPriority.add(new PriorityCounter(a, priority));
					--priority;
				}

				PriorityCounter[] firstFew = {};
				if (prefs.size() >= 4) {
					firstFew = new PriorityCounter[] { prefsWithPriority.get(0), prefsWithPriority.get(1),
							prefsWithPriority.get(2), prefsWithPriority.get(3) };
				} else {
					firstFew = new PriorityCounter[prefs.size()];
					for (int i = 0; i < prefs.size(); ++i) {
						firstFew[i] = prefsWithPriority.get(i);
					}
				}

				// Sort prefs by number of periods
				bubbleSort(firstFew);

				// Go through each activity
				for (PriorityCounter pc : firstFew) {
					enrollCamper(c, pc);
				}

				if (hasNulls(c.getSchedule()) && prefs.size() == 6) {
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
				if (hasNulls(c.getSchedule()) && prefs.size() == 5) {
					PriorityCounter fifth = prefsWithPriority.get(4);
					enrollCamper(c, fifth);
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
			} else {
				ArrayList<Period> sched = c.getSchedule();
				for (int i = 0; i < sched.size(); ++i) {
					Period p = sched.get(i);
					if (p == null) {
						c.setPlaceholder(true);
						sched.remove(i);
						sched.add(i, PLACEHOLDER);
					}
				}
			}
		}
	}

	public static void generateActivitiesMap(boolean[][] checks, Integer[][] caps) {
		for (int i = 0; i < activitiesAlpha.size(); ++i) {
			List<Integer> offs = new LinkedList<>();
			for (int j = 0; j < 4; ++j) {
				if (!checks[i][j]) {
					offs.add(j + 1);
				}
			}

			List<Integer> capc = new LinkedList<>();
			for (int j = 0; j < 4; ++j) {
				if (checks[i][j] && caps[i][j] > 0) {
					capc.add(caps[i][j]);
				}
			}

			activities.put(activitiesAlpha.get(i), new Activity(activitiesAlpha.get(i), offs, capc));
		}
	}

	public static void generateActivities() {
		activitiesAlpha.add("Adventure Challenge 1");
		activitiesAlpha.add("Adventure Challenge 2");
		activitiesAlpha.add("Archery");
		activitiesAlpha.add("Camping Classics");
		activitiesAlpha.add("Crafts");
		activitiesAlpha.add("FC Medley");
		activitiesAlpha.add("Intro to Horsemanship");
		activitiesAlpha.add("Horsemanship - Beginner");
		activitiesAlpha.add("Horsemanship - Intermediate");
		activitiesAlpha.add("Horsemanship - Advanced");
		activitiesAlpha.add("Kayaking");
		activitiesAlpha.add("Outdoorsmanship");
		activitiesAlpha.add("Riflery");
		activitiesAlpha.add("Sports");
		activitiesAlpha.add("Swimming");
		activitiesAlpha.add("Wakeboarding");
		activitiesAlpha.add("Waterskiing");
		activitiesAlpha.add("Wilderness Survival Skills");
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
			String id = lineScanner.next();

			// Get first name, last name
			// Once received, create Camper out of that data
			String lastName = lineScanner.next();
			String firstName = lineScanner.next();
			boolean lakePermission = lineScanner.next().equals("Yes");
			Camper c = new Camper(firstName, lastName, lakePermission, enrollDate);

			// System.out.println(firstName + " " + lastName);

			// Create empty list of preferences for the Camper
			LinkedList<Activity> prefs = new LinkedList<>();

			// Get preferences String from .csv, put into Scanner, and use either ", " or
			// "and " as delimiter
			String prefRaw = lineScanner.nextLine();
			if (prefRaw.length() > 1) {
				prefRaw = prefRaw.substring(2, prefRaw.length() - 1);
				Scanner scPref = new Scanner(prefRaw);
				scPref.useDelimiter(", | and ");

				// Separate each preference and put into list
				while (scPref.hasNext()) {
					String proxy = scPref.next();
					prefs.add(activities.get(proxy));
				}
				scPref.close();
			} else {
				prefs = null;
			}

			// Add Camper and preferences to list and map, close Scanners
			c.addPrefs(prefs);
			c.setID(id);
			campers.add(c);
			lineScanner.close();
			scDate.close();
		}
	}

	public static void printCamperPrefs() {
		for (Camper c : campers) {
			System.out.println(c.getName() + "'s Preferences (Enroll Date = " + c.getEnrollDate() + "):");
			LinkedList<Activity> prefs = c.getPrefs();
			if (prefs != null) {
				Activity head = prefs.getFirst();
				for (Activity a : prefs) {
					if (!a.equals(head)) {
						System.out.print(", ");
					}
					System.out.print(a.getName());
				}
				System.out.println("\n--------------");
			} else {
				System.out.println("No Prefs");
			}
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

        if (ropes.contains(pc.getActivity().getName())) {
            if (alreadyAC) {
                return;
            }
            
			if (pc.getActivity().getName().equals("Adventure Challenge 1")) {
				if (c.getPrefs().contains(activities.get("Adventure Challenge 2"))) {
					pc = new PriorityCounter(activities.get("Adventure Challenge 2"), pc.getScore());
				}
			}
        }

        if (wakeSki.contains(pc.getActivity().getName())) {
			if (alreadyWakeSki) {
				return;
			}
		}

        if (!c.hasLakefrontPermission() && lake.contains(pc.getActivity().getName())) {
            return;
        }

		if (horses.contains(pc.getActivity().getName())) {
			if (alreadyHorse) {
				return;
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
		return (name.equals("Intro to Horsemanship")
				|| name.equals("Horsemanship - Advanced")
				|| name.equals("Horsemanship - Intermediate")
				|| name.equals("Horsemanship - Beginner")) && !alreadyHorse;
	}

	public static void generateCSV() throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter("camperAssignments.csv")) {
			StringBuilder sb = new StringBuilder();
			sb.append("Enroll Date");
			sb.append(",");
			sb.append("CampMinder ID");
			sb.append(",");
			sb.append("Last Name");
			sb.append(",");
			sb.append("First Name");
			sb.append(",");
            sb.append("Has Lake Perms");
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

			if (debug) {
				sb.append(",");
				sb.append("Has Wake & Ski");
				sb.append(",");
				sb.append("Has AC1 & 2");
				sb.append(",");
				sb.append("Has Many Horses");
				sb.append("");
			}

			sb.append("\n\n");

			for (Camper c : campers) {
				sb.append(c.getEnrollDate().toString());
				sb.append(",");
				sb.append(c.getID());
				sb.append(",");
				sb.append(c.getLastName());
				sb.append(",");
				sb.append(c.getFirstName());
				sb.append(",");
                sb.append(c.hasLakefrontPermission() ? "Yes" : "No");
                sb.append(",");
				for (Period p : c.getSchedule()) {
					sb.append(p.getName());
					sb.append(",");
				}
				sb.append(c.getScore());
				sb.append(",");
				sb.append(Math.round(c.getScore() / 18.0 * 10000) / 100.0 + "%");
				sb.append(",");
				sb.append(c.getPlaceholder() ? "Yes" : " ");

				if (debug) {
					int waterCount = 0;
					int acCount = 0;
					int horseCount = 0;
					for (Period p : c.getSchedule()) {
						String name = p.getName();

						if (name.equals("Wakeboarding") || name.equals("Waterskiing")) {
							waterCount++;
						}

						if (name.equals("Adventure Challenge 1") || name.equals("Adventure Challenge 2")) {
							acCount++;
						}

						if (name.equals("Intro to Horsemanship")
								|| name.equals("Horsemanship - Beginner")
								|| name.equals("Horsemanship - Intermediate")
								|| name.equals("Horsemanship - Advanced")) {
							horseCount++;
						}
					}

					sb.append(",");
					sb.append(waterCount > 1 ? "Yes" : " ");
					sb.append(",");
					sb.append(acCount > 1 ? "Yes" : " ");
					sb.append(",");
					sb.append(horseCount > 1 ? "Yes" : " ");

				}

				sb.append("\n");
			}

			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
