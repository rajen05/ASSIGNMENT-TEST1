package questionOne;

/**
 * F28PA | Software Development A | Coursework
 *
 * The Coursework specification is provided in Canvas. Please read through it in
 * full before you start work.
 *
 * @author RAJENDRAN KUMARASAMY
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AthleteAnalysis {
	public static void main(String[] args) {

		// ── Scanner: single shared input reader ──────────────────────────────
		// We create one Scanner object to read input from the keyboard (System.in).
		// This will be used throughout the manual data entry section.
		Scanner sc = new Scanner(System.in);

		// ────────────────────────────────────────────────────────────────────
		// WELCOME BANNER
		// ────────────────────────────────────────────────────────────────────
		// A clear, user-friendly start screen that lists all valid input formats.
		System.out.println("----------------------------------------------------");
		System.out.println("|        ATHLETE DATA ANALYSIS SYSTEM              |");
		System.out.println("|  Enter athlete records; analysis follows after.  |");
		System.out.println("----------------------------------------------------");
		System.out.println("  • Name    : letters and spaces only");
		System.out.println("  • Gender  : M or F");
		System.out.println("  • Age     : 10 – 100");
		System.out.println("  • Height  : 100 – 250 cm");
		System.out.println("  • Weight  : 30  – 250 kg");
		System.out.println("  • Sport   : letters and spaces only");
		System.out.println("  • Medal   : Gold / Silver / Bronze / None\n");

		// ────────────────────────────────────────────────────────────────────
		// DATA STRUCTURES: Parallel Arrays & Count
		// We use "Parallel Arrays" to store data. This means that the data for
		// a single athlete is split across multiple arrays at the same index.
		// Example:
		// names[0] is the name of the first athlete.
		// ages[0] is the age of the first athlete.
		// names[1] is the name of the second athlete, and so on.
		// ────────────────────────────────────────────────────────────────────
		String[] names;
		String[] genders;
		int[] ages;
		int[] heights;
		int[] weights;
		String[] sports;
		String[] medals;
		int n;

		// --------------------------------------------------------------------
		// INPUT PHASE: Select Data Source
		// We determine whether to read from a file or keyboard.
		// --------------------------------------------------------------------
		String filename = null;

		// 1. Check Command Args (Fast Testing)
		if (args.length > 0) {
			filename = args[0];
		}
		// 2. Interactive Menu
		else {
			boolean fileSelected = false;
			while (!fileSelected) {
				System.out.println("Select Data Source:");
				System.out.println("  1. Import from text file");
				System.out.println("  2. Enter manually");
				System.out.print("Choice: ");

				String input = sc.nextLine().trim();

				if (input.equals("1")) {
					// Inner loop for filename entry validation
					while (true) {
						System.out.print("Enter filename (e.g. data.txt) or type 'back' to return: ");
						String enteredName = sc.nextLine().trim();

						if (enteredName.equalsIgnoreCase("back")) {
							break; // Go back to main menu loop
						}

						if (enteredName.isEmpty()) {
							System.out.println("  x Filename cannot be empty.");
							continue;
						}

						File f = new File(enteredName);
						if (f.exists() && !f.isDirectory()) {
							filename = enteredName;
							fileSelected = true;
							break; // File is valid, break inner loop
						} else {
							System.out.println("  x File '" + enteredName + "' not found. Please try again.");
						}
					}
					// If a valid file was selected in the inner loop, break the outer loop too
					if (fileSelected)
						break;

				} else if (input.equals("2")) {
					filename = null; // Manual Mode
					break;
				} else {
					System.out.println("  x Invalid. Enter 1 or 2.");
				}
				System.out.println();
			}
		}

		// --------------------------------------------------------------------
		// EXECUTE: File Mode or Manual Mode
		// --------------------------------------------------------------------
		if (filename != null) {

			// -- FILE MODE ---------------------------------------------------
			// Load athlete records from the file 'filename'.
			// First line of file = count (n).
			// Subsequent lines = comma-separated athlete data.
			// ----------------------------------------------------------------
			System.out.println("Loading data from file: " + filename + "\n");

			Scanner fileSc;
			try {
				fileSc = new Scanner(new File(filename));
			} catch (FileNotFoundException e) {
				System.out.println("Error: Could not find file '" + filename + "'.");
				System.out.println("Ensure the file is in the project root folder.");
				sc.close();
				return; // Exit program if file is missing
			}

			// ── Step 1: Read 'n' (Count) from the first line ────────────────
			if (!fileSc.hasNextLine()) {
				System.out.println("Error: File is empty.");
				fileSc.close();
				sc.close();
				return;
			}

			String firstLine = fileSc.nextLine().trim();
			try {
				// The sample file format is "20,,,", so we split by comma and take the first
				// part.
				// parseInteger converts the string "20" into the number 20.
				n = Integer.parseInt(firstLine.split(",")[0].trim());
			} catch (NumberFormatException e) {
				System.out.println("Error: First line must start with athlete count.");
				fileSc.close();
				sc.close();
				return;
			}

			// Validate that the count is positive
			if (n < 1) {
				System.out.println("Error: Athlete count must be >= 1.");
				fileSc.close();
				sc.close();
				return;
			}

			// ── Step 2: Initialize Arrays ───────────────────────────────────
			// Now that we know 'n', we can allocate the exact memory needed for our arrays.
			names = new String[n];
			genders = new String[n];
			ages = new int[n];
			heights = new int[n];
			weights = new int[n];
			sports = new String[n];
			medals = new String[n];

			// ── Step 3: Parse Rows ──────────────────────────────────────────
			int loaded = 0; // Tracks how many valid athletes we have successfully stored
			int lineNum = 1; // Keeps track of file line number for error reporting

			while (fileSc.hasNextLine()) {
				String line = fileSc.nextLine().trim();
				lineNum++;

				// Skip empty lines in the file
				if (line.isEmpty())
					continue;

				// Split the line by commas to get individual fields
				String[] parts = line.split(",");
				if (parts.length != 7) {
					System.out.println("Skipping line " + lineNum + ": expected 7 fields.");
					continue;
				}

				// Extract raw string values
				String nameVal = parts[0].trim();
				String genderVal = parts[1].trim().toUpperCase(); // Force Upper Case for consistency
				String ageStr = parts[2].trim();
				String heightStr = parts[3].trim();
				String weightStr = parts[4].trim();
				String sportVal = parts[5].trim();
				String medalVal = parts[6].trim();

				// ── Validation Checks for File Data ──
				// Even though data comes from a file, we must validate it to prevent crashes.
				if (nameVal.isEmpty())
					continue;
				if (!genderVal.equals("M") && !genderVal.equals("F"))
					continue;

				int ageVal, heightVal, weightVal;
				try {
					ageVal = Integer.parseInt(ageStr);
					heightVal = Integer.parseInt(heightStr);
					weightVal = Integer.parseInt(weightStr);
				} catch (NumberFormatException e) {
					continue; // Skip line if numbers are invalid
				}

				// Check logical ranges
				if (ageVal < 10 || ageVal > 100)
					continue;
				if (heightVal < 100 || heightVal > 250)
					continue;
				if (weightVal < 30 || weightVal > 250)
					continue;
				if (sportVal.isEmpty())
					continue;

				// Check medal validity
				if (!medalVal.equalsIgnoreCase("Gold") && !medalVal.equalsIgnoreCase("Silver") &&
						!medalVal.equalsIgnoreCase("Bronze") && !medalVal.equalsIgnoreCase("None")) {
					continue;
				}

				// If we passed all checks, store the data into the arrays at index 'loaded'
				names[loaded] = nameVal;
				genders[loaded] = genderVal;
				ages[loaded] = ageVal;
				heights[loaded] = heightVal;
				weights[loaded] = weightVal;

				// Normalise sports and medals using Title Case (e.g. "swimming" -> "Swimming")
				sports[loaded] = toTitleCase(sportVal);
				medals[loaded] = toTitleCase(medalVal);

				loaded++; // Move to the next index

				// If we have filled all arrays, stop reading even if file has more lines
				if (loaded == n)
					break;
			}
			fileSc.close();

			// Update 'n' to the actual number of loaded athletes.
			// This handles cases where some lines in the file were invalid/skipped.
			n = loaded;
			if (n == 0) {
				System.out.println("No valid records found in file.");
				sc.close();
				return;
			}
			System.out.println("Loaded " + n + " valid athlete record(s).\n");

		} else {

			// ────────────────────────────────────────────────────────────────
			// MANUAL MODE - User enters data via keyboard
			// ────────────────────────────────────────────────────────────────

			// ── Step 1: Ask how many athletes (N) ──────────────────────────
			// usage of while(true) creates an infinite loop that only breaks
			// when valid input is received. This is "Defensive Programming".
			while (true) {
				System.out.print("How many athletes would you like to enter? ");
				String raw = sc.nextLine().trim();

				// Validation 1: Check for empty input
				if (raw.isEmpty()) {
					System.out.println("  ✗ Input cannot be blank.\n");
					continue;
				}
				// Validation 2: Check if it's a number
				try {
					n = Integer.parseInt(raw);
				} catch (NumberFormatException e) {
					System.out.println("  ✗ Not a valid whole number.\n");
					continue;
				}
				// Validation 3: Check reasonable range (1 to 1000)
				if (n < 1 || n > 1000) {
					System.out.println("  ✗ Must be between 1 and 1000.\n");
					continue;
				}
				break; // Input is valid, exit the loop
			}

			// ── Step 2: Initialize Arrays ──────────────────────────────────
			// Now we create the arrays with size 'n'.
			names = new String[n];
			genders = new String[n];
			ages = new int[n];
			heights = new int[n];
			weights = new int[n];
			sports = new String[n];
			medals = new String[n];

			// ── Step 3: Collect Data Loop ──────────────────────────────────
			System.out.println("\n--- Please enter details for each athlete ---\n");

			// Loop from 0 to n-1 to fill every slot in our arrays
			for (int i = 0; i < n; i++) {
				System.out.println("Athlete #" + (i + 1));

				// ── Name Validation ──
				// Must not be blank and must match logic for letters/spaces only.
				while (true) {
					System.out.print("  Name    : ");
					String input = sc.nextLine().trim();
					if (input.isEmpty()) {
						System.out.println("  ✗ Cannot be blank.");
						continue;
					}
					// Regex "[a-zA-Z ]+" means "one or more letters or spaces"
					if (!input.matches("[a-zA-Z ]+")) {
						System.out.println("  ✗ Letters only.");
						continue;
					}
					names[i] = input;
					break;
				}

				// ── Gender Validation ──
				// Only accept M or F (case-insensitive).
				while (true) {
					System.out.print("  Gender  (M/F): ");
					String input = sc.nextLine().trim();
					if (input.equalsIgnoreCase("M") || input.equalsIgnoreCase("F")) {
						genders[i] = input.toUpperCase(); // Normalize to "M" or "F"
						break;
					}
					System.out.println("  ✗ Enter M or F.");
				}

				// ── Age Validation ──
				// Must be int, range 10-100.
				while (true) {
					System.out.print("  Age     : ");
					try {
						int val = Integer.parseInt(sc.nextLine().trim());
						if (val < 10 || val > 100) {
							System.out.println("  ✗ 10-100 only.");
							continue;
						}
						ages[i] = val;
						break;
					} catch (NumberFormatException e) {
						System.out.println("  ✗ valid number required.");
					}
				}

				// ── Height Validation ──
				// Must be int, range 100-250 cm.
				while (true) {
					System.out.print("  Height  (cm): ");
					try {
						int val = Integer.parseInt(sc.nextLine().trim());
						if (val < 100 || val > 250) {
							System.out.println("  ✗ 100-250 cm only.");
							continue;
						}
						heights[i] = val;
						break;
					} catch (NumberFormatException e) {
						System.out.println("  ✗ valid number required.");
					}
				}

				// ── Weight Validation ──
				// Must be int, range 30-250 kg.
				while (true) {
					System.out.print("  Weight  (kg): ");
					try {
						int val = Integer.parseInt(sc.nextLine().trim());
						if (val < 30 || val > 250) {
							System.out.println("  ✗ 30-250 kg only.");
							continue;
						}
						weights[i] = val;
						break;
					} catch (NumberFormatException e) {
						System.out.println("  ✗ valid number required.");
					}
				}

				// ── Sport Validation ──
				// Letters only. We store it in Title Case for consistency.
				while (true) {
					System.out.print("  Sport   : ");
					String input = sc.nextLine().trim();
					if (input.isEmpty()) {
						System.out.println("  ✗ Cannot be blank.");
						continue;
					}
					if (!input.matches("[a-zA-Z ]+")) {
						System.out.println("  ✗ Letters only.");
						continue;
					}
					sports[i] = toTitleCase(input);
					break;
				}

				// ── Medal Validation ──
				// Specific set of allowed words.
				while (true) {
					System.out.print("  Medal   (Gold/Silver/Bronze/None): ");
					String input = sc.nextLine().trim();
					// Check against allowed values (ignoring case)
					if (input.equalsIgnoreCase("Gold") || input.equalsIgnoreCase("Silver") ||
							input.equalsIgnoreCase("Bronze") || input.equalsIgnoreCase("None")) {
						medals[i] = toTitleCase(input);
						break;
					}
					System.out.println("  ✗ Invalid entry.");
				}
				System.out.println(); // Blank line for readability
			}
		}

		// ────────────────────────────────────────────────────────────────────
		// STEP 4 – Run all ten analysis tasks and print results
		// ────────────────────────────────────────────────────────────────────
		System.out.println("\n----------------------------------------------------");
		System.out.println("|           A T H L E T E   A N A L Y S I S        |");
		System.out.println("----------------------------------------------------\n");

		// We pass the arrays and 'n' to each method to perform specific tasks.
		printAthleteTable(names, genders, ages, heights, weights, sports, medals, n);
		printGenderRatio(genders, n);

		// Re-use the same method 'printStatsPerGender' for Age, Height, and Weight
		printStatsPerGender("2. AGE (years)", ages, genders, n);
		printStatsPerGender("3. HEIGHT (cm)   ", heights, genders, n);
		printStatsPerGender("4. WEIGHT (kg)   ", weights, genders, n);

		printOldestAndYoungest(names, genders, ages, n);
		printUniqueSports(sports, n);
		printMedalsByGender(genders, medals, n);
		printMedalsByGenderAndSport(genders, sports, medals, n);

		sc.close();
		System.out.println("Program finished. Goodbye!");
	}

	// ------------------------------------------------------------------------
	// STATIC HELPER METHODS
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// TASK 10
	// Prints all athlete records as a formatted table.
	// We use System.out.printf to align columns perfectly.
	// ------------------------------------------------------------------------
	private static void printAthleteTable(String[] names, String[] genders,
										  int[] ages, int[] heights, int[] weights,
										  String[] sports, String[] medals, int n) {
		System.out.println("----------------------------------- ATHLETE TABLE ------------------------------------");
		// %-15s means "a string, left-aligned, taking up 15 characters width"
		System.out.printf("%-4s %-15s %-7s %-5s %-11s %-11s %-15s %-8s%n",
				"#", "Name", "Gender", "Age", "Height(cm)", "Weight(kg)", "Sport", "Medal");
		System.out.println("-------------------------------------------------------------------------------------");
		for (int i = 0; i < n; i++) {
			System.out.printf("%-4d %-15s %-7s %-5d %-11d %-11d %-15s %-8s%n",
					(i + 1), names[i], genders[i], ages[i],
					heights[i], weights[i], sports[i], medals[i]);
		}
		System.out.println("-------------------------------------------------------------------------------------\n");
	}

	// ------------------------------------------------------------------------
	// TASK 1
	// Counts male and female athletes, then prints each count and percentage.
	// Percentage is calculated as (count / total) * 100.
	// ------------------------------------------------------------------------
	private static void printGenderRatio(String[] genders, int n) {
		int males = 0, females = 0;
		// Linear scan through genders array
		for (int i = 0; i < n; i++) {
			if ("M".equals(genders[i]))
				males++;
			else
				females++;
		}

		// Guard against division by zero (if n is 0)
		// We multiply by 100.0 to force floating-point arithmetic (double)
		double malePct = (n > 0) ? (males * 100.0 / n) : 0;
		double femalePct = (n > 0) ? (females * 100.0 / n) : 0;

		System.out.println("─── 1. GENDER RATIO ──────────────────────────────────────────────");
		System.out.printf("  Male   : %d athlete(s)  (%.1f%%)%n", males, malePct);
		System.out.printf("  Female : %d athlete(s)  (%.1f%%)%n", females, femalePct);
		System.out.println();
	}

	// ------------------------------------------------------------------------
	// TASKS 2, 3, 4
	// Calculates mean and population standard deviation per gender.
	//
	// Algorithm (2-Pass approach):
	// 1. Pass 1: Sum all values to calculate the Mean.
	// 2. Pass 2: Sum the squared difference of each value from the Mean.
	// 3. Calculate Sqrt(SumSqDiff / Count).
	// ------------------------------------------------------------------------
	private static void printStatsPerGender(String label, int[] values,
											String[] genders, int n) {
		System.out.println("─── " + label + " ──────────────────────────────────────");

		// Run the loop twice: once for "M", once for "F"
		for (String targetGender : new String[] { "M", "F" }) {
			String gLabel = "M".equals(targetGender) ? "Male" : "Female";
			int count = 0;
			double sum = 0;

			// Pass 1: Calculate Sum and Count
			for (int i = 0; i < n; i++) {
				if (targetGender.equals(genders[i])) {
					sum += values[i];
					count++;
				}
			}

			// Handle edge case where no athletes of this gender exist
			if (count == 0) {
				System.out.println("  " + gLabel + " : No data available.");
				continue;
			}

			double mean = sum / count;
			double sumSqDiff = 0;

			// Pass 2: Calculate Sum of Squared Differences
			// Formula component: Σ(x - mean)^2
			for (int i = 0; i < n; i++) {
				if (targetGender.equals(genders[i])) {
					double diff = values[i] - mean;
					sumSqDiff += diff * diff;
				}
			}

			// Final Standard Deviation Formula: σ = sqrt( Σ(x - mean)^2 / N )
			double stdDev = Math.sqrt(sumSqDiff / count);

			System.out.printf("  %-7s → Count: %d | Mean: %.2f | Std Dev: %.4f%n",
					gLabel, count, mean, stdDev);
		}
		System.out.println();
	}

	// ------------------------------------------------------------------------
	// TASKS 5 & 6
	// Finds the oldest and youngest athlete for each gender.
	// We iterate through the array, keeping track of the index of the best match
	// found so far.
	// ------------------------------------------------------------------------
	private static void printOldestAndYoungest(String[] names, String[] genders,
											   int[] ages, int n) {
		System.out.println("─── 5 & 6. OLDEST & YOUNGEST PER GENDER ─────────────────────────");

		for (String targetGender : new String[] { "M", "F" }) {
			String gLabel = "M".equals(targetGender) ? "Male" : "Female";
			int oldestIdx = -1; // -1 acts as a "not found yet" flag
			int youngestIdx = -1;

			for (int i = 0; i < n; i++) {
				if (targetGender.equals(genders[i])) {
					// Initialize if it's the first match, or update if we find someone older
					if (oldestIdx == -1 || ages[i] > ages[oldestIdx]) {
						oldestIdx = i;
					}
					// Initialize if it's the first match, or update if we find someone younger
					if (youngestIdx == -1 || ages[i] < ages[youngestIdx]) {
						youngestIdx = i;
					}
				}
			}

			if (oldestIdx == -1) {
				System.out.println("  " + gLabel + " : No data available.");
			} else {
				System.out.printf("  %-7s Oldest   → %-15s (Age %d)%n",
						gLabel, names[oldestIdx], ages[oldestIdx]);
				System.out.printf("  %-7s Youngest → %-15s (Age %d)%n",
						gLabel, names[youngestIdx], ages[youngestIdx]);
			}
		}
		System.out.println();
	}

	// ------------------------------------------------------------------------
	// TASK 7
	// Builds a de-duplicated list of sports and prints counts.
	// Since specific Collection classes (like Set) might be restricted,
	// we use a manual "check-before-add" algorithm with arrays.
	// ------------------------------------------------------------------------
	private static void printUniqueSports(String[] sports, int n) {
		System.out.println("─── 7. UNIQUE SPORTS ─────────────────────────────────────────────");
		String[] uniqueSports = new String[n]; // Max possible unique sports = n
		int uniqueCount = 0;

		for (int i = 0; i < n; i++) {
			boolean alreadySeen = false;
			// Check if current sport is already in our unique list
			for (int j = 0; j < uniqueCount; j++) {
				if (uniqueSports[j].equalsIgnoreCase(sports[i])) {
					alreadySeen = true;
					break;
				}
			}
			// If not seen, add it to the list
			if (!alreadySeen) {
				uniqueSports[uniqueCount] = sports[i];
				uniqueCount++;
			}
		}

		System.out.println("  Total unique sports: " + uniqueCount);
		for (int i = 0; i < uniqueCount; i++) {
			System.out.println("    " + (i + 1) + ". " + uniqueSports[i]);
		}
		System.out.println();
	}

	// ------------------------------------------------------------------------
	// TASK 8
	// Prints medal totals per gender (all sports combined).
	// We use simple counters to tally up Gold, Silver, Bronze, and None.
	// ------------------------------------------------------------------------
	private static void printMedalsByGender(String[] genders, String[] medals, int n) {
		System.out.println("─── 8. MEDALS PER GENDER  (all sports combined) ──────────────────");
		System.out.printf("  %-10s %-8s %-8s %-8s %-8s%n",
				"Gender", "Gold", "Silver", "Bronze", "None");
		System.out.println("  ──────────────────────────────────────────────");

		for (String targetGender : new String[] { "M", "F" }) {
			String gLabel = "M".equals(targetGender) ? "Male" : "Female";
			int gold = 0, silver = 0, bronze = 0, none = 0;

			for (int i = 0; i < n; i++) {
				if (targetGender.equals(genders[i])) {
					switch (medals[i]) {
						case "Gold":
							gold++;
							break;
						case "Silver":
							silver++;
							break;
						case "Bronze":
							bronze++;
							break;
						default:
							none++;
							break;
					}
				}
			}
			System.out.printf("  %-10s %-8d %-8d %-8d %-8d%n",
					gLabel, gold, silver, bronze, none);
		}
		System.out.println();
	}

	// ------------------------------------------------------------------------
	// TASK 9
	// For each unique sport, prints a per-gender medal breakdown.
	// Uses the same unique-list building logic as Task 7, then iterates
	// over each unique sport to count medals for that specific sport.
	// ------------------------------------------------------------------------
	private static void printMedalsByGenderAndSport(String[] genders, String[] sports,
													String[] medals, int n) {
		System.out.println("─── 9. MEDALS PER GENDER  PER SPORT ─────────────────────────────");

		// 1. Build Unique Sports List
		String[] uniqueSports = new String[n];
		int uniqueCount = 0;
		for (int i = 0; i < n; i++) {
			boolean seen = false;
			for (int j = 0; j < uniqueCount; j++) {
				if (uniqueSports[j].equalsIgnoreCase(sports[i])) {
					seen = true;
					break;
				}
			}
			if (!seen)
				uniqueSports[uniqueCount++] = sports[i];
		}

		// 2. Iterate through each sport and print its statistics
		for (int s = 0; s < uniqueCount; s++) {
			System.out.println("  Sport: " + uniqueSports[s]);
			System.out.printf("    %-10s %-8s %-8s %-8s %-8s%n",
					"Gender", "Gold", "Silver", "Bronze", "None");
			System.out.println("    ────────────────────────────────────────");

			for (String targetGender : new String[] { "M", "F" }) {
				String gLabel = "M".equals(targetGender) ? "Male" : "Female";
				int gold = 0, silver = 0, bronze = 0, none = 0;

				for (int i = 0; i < n; i++) {
					// Condition: Must match BOTH the gender AND the current unique sport
					if (targetGender.equals(genders[i]) &&
							uniqueSports[s].equalsIgnoreCase(sports[i])) {
						switch (medals[i]) {
							case "Gold":
								gold++;
								break;
							case "Silver":
								silver++;
								break;
							case "Bronze":
								bronze++;
								break;
							default:
								none++;
								break;
						}
					}
				}
				System.out.printf("    %-10s %-8d %-8d %-8d %-8d%n",
						gLabel, gold, silver, bronze, none);
			}
			System.out.println();
		}
	}

	// ------------------------------------------------------------------------
	// UTILITY
	// Converts a string to Title Case (e.g. "sWiMMinG" becomes "Swimming").
	// Used to normalize inputs so that "swimming" and "Swimming" are treated as
	// identical.
	// ------------------------------------------------------------------------
	private static String toTitleCase(String input) {
		String[] words = input.toLowerCase().split("\\s+");
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			if (!word.isEmpty()) {
				// Upper case first char + Lower case rest of word
				sb.append(Character.toUpperCase(word.charAt(0)))
						.append(word.substring(1))
						.append(" ");
			}
		}
		return sb.toString().trim();
	}
}