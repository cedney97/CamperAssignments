A program catered specifically for Frontier Camp activity assignments, as a replacement for CampMinder activity assignment software.
This algorithm has two functions:
- Assign campers to activites, given a .csv file with the campers' preferences, returning a new .csv file. This final .csv file will include each camper, followed by their four activity periods, as well as a Quality Score (more on this in the details below), or
- Generate rosters based on the returned .csv file.

This program will not do both at once, however, so that the user can have the ability to edit or correct any of the activites, as well as check the compatibility of the lineup.

The specific outline of the algorithm is catered toward Frontier Camp's overall activity period setup and is detailed below:
- Go through each camper and map them to their 6 preferences
- Then, for each camper, assign a priority score to each of the preferences, the first preference being 6, the second being 5, and so forth
- The program will then look through the first 4 activity preferences, with the priority of being assigned to classes with less periods
- For each of these activities, the program will check a few things
  - If the activity is Adventure Challenge 1 or Adventure Challenge 2:
    - The program will check to see if the camper is already assigned to another Adventure Challenge class. If not and the current activity is Adventure Challenge 1, the program'll look to see if the camper also preferred Adventure Challenge 2. If so, the program will attempt to assign the camper to Adventure Challenge 2. If the camper did not prefer Adventure Challenge 2, the program will attempt to assign them to Adventure Challenge 1. If the camper is already assigned to Adventure Challenge 1 or 2, this activity will be skipped
  - If the activity is Horsemanship Beginner, Intermediate, or Advanced:
    - A similar method is done for Horsemanship as with Adventure Challenge. The program will prefer whichever Horsemanship level was preferred highest, however, instead of the highest level. If they prefer multiple Horsemanship levels, only the one with the highest priority score will be assigned.
  - If the activity is Wakeboarding or Waterskiing:
    - A similar method is done for Wake/Ski as with Horsemanship. The program will prefer the choice of Wakeboarding or Waterskiing is done first. If they choose both, they only get assigned to the activity with the highest priority score.
- After these checks, the program will attempt to enroll the camper in one of the activity's periods, with priority for the periods with less campers 
  - If the period is full, the camper will not be assigned to that period
  - If every period is full, the program will move on to the next activity
-If the camper is assigned to a period of that activity, the activity is added to their schedule. In addition to this, the priority score for the activity is added to the camper's overall Quality Score. This score is the accumulation of the priority scores of all the activities they are assigned to. This score, as well as a percentage out of 18 (the best Quality Score), will be recorded in the final .csv file returned and denotes how successful the algorithm was in assigning the camper to their preferred activities.
