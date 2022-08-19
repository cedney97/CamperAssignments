A program catered specifically for Frontier Camp activity assignments, as a replacement for CampMinder activity assignment software.
This algorithm has two functions:
- Assign campers to activites, given a .csv file with the campers' preferences, returning a new .csv file. This final .csv file will include each camper, followed by their four activity periods, as well as a Quality Score (more on this in the details below), or
- Generate rosters based on the returned .csv file.

This program will not do both at once, however, so that the user can have the ability to edit or correct any of the activites, as well as check the compatibility of the lineup.

The specific outline of the algorithm is catered toward Frontier Camp's overall activity period setup and is detailed below:
- Go through each camper and map them to their 6 preferences,
- Then, for each camper, assign a priority score to each of the preferences, the first preference being 6, the second being 5, and so forth,
- The program will then look through the first 4 activity preferences, with the priority of being assigned to classes with less periods.
- For each of these activities, the program will check a few things
  - If the activity is Adventure Challenge 1 or Adventure Challenge 2:
    - jkjk
