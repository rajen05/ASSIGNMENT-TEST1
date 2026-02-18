package questionTwo;

import java.util.Scanner;

/**
 * F28PA | Software Development A | Coursework
 * 
 * The Coursework specification is provided in Canvas. Please read through it in
 * full before you start work.
 * 
 * BookingSoft.java
 *
 * Main program for the BookingSoft Cinema Ticket Booking System.
 *
 * Features:
 * - Browse all available shows
 * - Book MULTIPLE tickets per session (Additional Feature 1)
 * - Visual seat map display showing [ ] or [X] for every seat (Additional
 * Feature 2)
 * - Three custom exceptions: InvalidSeatException, SeatAlreadyBookedException,
 * VenueFullException
 * - Comprehensive input validation for every user entry
 * - Clear, friendly prompts and feedback at every step
 * 
 * @author RAJENDRAN KUMARASAMY
 */

public class BookingSoft {

    // Single shared Scanner to avoid issues with closing/reopening streams
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {

        // There are currently 6 shows offered in 6 different venues.
        Show[] shows = new Show[6];

        shows[0] = new Show("1N", new Film("SING", 1));
        shows[1] = new Show("2W", new Film("THE GRINCH", 2));
        shows[2] = new Show("3E", new Film("BOSS BABY", 3));
        shows[3] = new Show("3S", new Film("YES DAY", 3));
        shows[4] = new Show("1E", new Film("THE KARATE KID", 1));
        shows[5] = new Show("2N", new Film("THE SEA BEAST", 2));

        System.out.println("### Welcome to the Booking System ###\n");
        // ── DO NOT CHANGE THE ABOVE PART OF THE CODE ────────────────────────────

        // Application Main Loop
        boolean running = true;

        while (running) {
            printMainMenu();

            int menuChoice = readIntInRange("Enter your choice: ", 1, 3);

            switch (menuChoice) {

                case 1:
                    // Book ticket(s)
                    bookingFlow(shows);
                    pressEnterToContinue();
                    break;

                case 2:
                    // View all shows with their seat maps
                    viewAllAvailability(shows);
                    pressEnterToContinue();
                    break;

                case 3:
                    // Exit
                    System.out.println("\nThank you for using BookingSoft. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        scanner.close();
    }

    /*
     * Prints the main menu options to the console.
     */
    private static void printMainMenu() {
        System.out.println();
        System.out.println("==================================");
        System.out.println("|        BOOKINGSOFT MENU        |");
        System.out.println("==================================");
        System.out.println("|  1. Book ticket(s)             |");
        System.out.println("|  2. View all shows & seats     |");
        System.out.println("|  3. Exit                       |");
        System.out.println("==================================");
    }

    /*
     * Displays seat availability summary AND visual seat map for every venue.
     * Called from the main menu option 2.
     */
    private static void viewAllAvailability(Show[] shows) {
        System.out.println();
        System.out.println("-------------- SEAT AVAILABILITY FOR ALL VENUES --------------");
        System.out.println("  Legend:  [ ] = Available      [X] = Booked / Occupied");
        System.out.println("--------------------------------------------------------------");

        for (int i = 0; i < shows.length; i++) {
            System.out.println();
            System.out.println("  Show #" + (i + 1) + ":");
            shows[i].printAvailability();
            System.out.println();
            shows[i].printHall();
            System.out.println("--------------------------------------------------------------");
        }
    }

    /*
     * Guides the user through the entire booking process.
     * Steps:
     * 1. Choose a show
     * 2. View the seat map
     * 3. Choose number of tickets
     * 4. Pick seats (with validation)
     * 5. Confirm bookings
     */
    private static void bookingFlow(Show[] shows) {
        System.out.println();
        System.out.println("------------------- TICKET BOOKING -------------------");

        // Step 1: Print the show list inline so user can pick a show
        System.out.println();
        System.out.println("  Available Shows:");
        System.out.println("  +----+-------------------+-----------------+-------+------------+");
        System.out.println("  | #  | Film              | Session         | Venue | Available  |");
        System.out.println("  +----+-------------------+-----------------+-------+------------+");

        for (int i = 0; i < shows.length; i++) {
            Venue v = shows[i].getVenue();
            Film f = shows[i].getFilm();
            int avail = countAvailableSeats(shows[i]);
            int total = v.getNumOfSeats();
            String status = (avail == 0) ? "SOLD OUT" : (avail + "/" + total);
            System.out.printf("  | %-2d | %-17s | %-15s | %-5s | %-10s |%n",
                    (i + 1), f.getTitle(), f.getSession(), v.getID(), status);
        }
        System.out.println("  +----+-------------------+-----------------+-------+------------+");
        System.out.println();

        // Step 2: Ask which show to book — 0 goes back to main menu
        int showChoice = readIntWithBack("  Enter show number (1-" + shows.length + ") or 0 to go back: ",
                1, shows.length);
        if (showChoice == 0) {
            System.out.println("  Returning to main menu...");
            return;
        }
        Show selectedShow = shows[showChoice - 1];

        // Step 3: Check that the chosen show is not sold out
        try {
            checkVenueNotFull(selectedShow);
        } catch (VenueFullException e) {
            System.out.println();
            System.out.println("  !! " + e.getMessage());
            System.out.println("  Please select a different show.");
            bookingFlow(shows); // Go back to show selection
            return;
        }

        // Step 4: Show the visual seat map so the user can see what's free
        System.out.println();
        System.out.println("  Current seat map for your chosen show:");
        selectedShow.printHall();

        // Step 5: Ask how many tickets to book — 0 goes back to show selection
        Venue v = selectedShow.getVenue();
        int rows = v.getNoRows();
        int cols = v.getNoCols();
        int maxCols = v.getNoCols();

        int remaining = countAvailableSeats(selectedShow);
        System.out.println("  How many tickets would you like to book?");
        System.out.println("  (Maximum you can book right now: " + remaining + ")");

        int numTickets = readIntWithBack("  Number of tickets (1-" + remaining + ") or 0 to go back: ",
                1, remaining);
        if (numTickets == 0) {
            System.out.println("  Going back to show selection...");
            bookingFlow(shows); // Go back to show list
            return;
        }

        // Step 6: Book each ticket one by one
        int successCount = 0;

        for (int t = 1; t <= numTickets; t++) {
            System.out.println();
            System.out.println("  -- Ticket " + t + " of " + numTickets + " --");

            boolean ticketBooked = false;

            // Keep asking until this particular ticket is successfully booked or user goes
            // back
            while (!ticketBooked) {

                // Ask for row letter — '0' goes back to show list
                char row = readRowLetterWithBack(selectedShow);
                if (row == '0') {
                    System.out.println("  Going back to show selection...");
                    bookingFlow(shows);
                    return;
                }

                // Ask for seat number — 0 goes back to show list
                int seatNo = readSeatNumberWithBack(selectedShow, maxCols);
                if (seatNo == 0) {
                    System.out.println("  Going back to show selection...");
                    bookingFlow(shows);
                    return;
                }

                // Attempt to validate and book this seat
                try {
                    validateRow(row, rows);
                    validateSeat(seatNo, cols);
                    checkSeatAvailable(selectedShow, row, seatNo);

                    // All checks passed – ask user to confirm before finalising
                    if (confirmBooking(row, seatNo, selectedShow)) {
                        selectedShow.buyTicket(row, seatNo);
                        successCount++;
                        ticketBooked = true;

                        System.out.println("  ✔  Seat " + row + seatNo + " has been successfully booked!");

                        // Show updated seat map so the user can see remaining seats
                        if (t < numTickets) {
                            System.out.println();
                            System.out.println("  Updated seat map:");
                            selectedShow.printHall();
                        }
                    } else {
                        // User said N at confirmation – let them re-pick a seat
                        System.out.println("  Booking cancelled. Please choose a different seat.");
                    }

                } catch (InvalidSeatException e) {
                    System.out.println("  !! Invalid seat: " + e.getMessage());
                    System.out.println("  Please enter a valid seat for this venue.");

                } catch (SeatAlreadyBookedException e) {
                    System.out.println("  !! " + e.getMessage());
                    System.out.println("  Please choose a different seat.");
                }
            }
        }

        // Step 7: Booking session complete – print summary
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("  Booking complete! " + successCount + " ticket(s) booked for:");
        System.out.println("    Film:    " + selectedShow.getFilm().getTitle());
        System.out.println("    Session: " + selectedShow.getFilm().getSession());
        System.out.println("    Venue:   " + selectedShow.getVenue().getID());
        System.out.println("----------------------------------------------");

        // Show the final seat map for this venue after all bookings
        System.out.println();
        System.out.println("  Final seat map for " + selectedShow.getVenue().getID() + ":");
        selectedShow.printHall();

        // Warn if the venue became sold out after this session
        if (selectedShow.getVenue().checkIfVenueIsFull()) {
            System.out.println("  *** This venue is now SOLD OUT! ***");
        }

        // Ask if the user wants to book again for another show
        System.out.println();
        System.out.println("  Would you like to book tickets for another show?");
        System.out.print("  Enter Y for Yes, any other key to return to menu: ");
        String again = scanner.nextLine().trim().toUpperCase();
        if (again.equals("Y")) {
            bookingFlow(shows);
        } else {
            System.out.println("  Returning to main menu...");
        }
    }

    /*
     * Asks the user to confirm a seat booking before it is finalised.
     * Returns true if the user types 'Y', false otherwise.
     */
    private static boolean confirmBooking(char row, int seatNo, Show selectedShow) {
        System.out.println();
        System.out.println("  -------------------------------------------");
        System.out.println("              BOOKING CONFIRMATION           ");
        System.out.println("  -------------------------------------------");
        System.out.println("     Seat    : " + row + seatNo);
        System.out.println("     Film    : " + selectedShow.getFilm().getTitle());
        System.out.println("     Session : " + selectedShow.getFilm().getSession());
        System.out.println("     Venue   : " + selectedShow.getVenue().getID());
        System.out.println("  -------------------------------------------");
        System.out.print("  Confirm this booking? (Y = Yes / N = No): ");

        String input = scanner.nextLine().trim().toUpperCase();

        // Return true only if the user explicitly types Y
        if (input.equals("Y")) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Pauses the program and waits for the user to press Enter.
     */
    private static void pressEnterToContinue() {
        System.out.println();
        System.out.print("  [ Press Enter to return to the main menu ] ");
        scanner.nextLine();
    }

    /*
     * Throws VenueFullException if the venue for the given show has no seats left.
     */
    private static void checkVenueNotFull(Show show) throws VenueFullException {
        if (show.getVenue().checkIfVenueIsFull()) {
            throw new VenueFullException(
                    "Sorry – " + show.getFilm().getTitle()
                            + " at " + show.getVenue().getID()
                            + " is completely SOLD OUT.");
        }
    }

    /*
     * Throws InvalidSeatException if the given row letter is outside the
     * valid range for the show's venue.
     */
    private static void validateRow(char row, int maxRows) throws InvalidSeatException {
        int rowIdx = Venue.rowLetter2Idx(row);

        if (rowIdx < 0 || rowIdx >= maxRows) {
            // Tell the user exactly which rows are valid
            char lastValidRow = Venue.rowIndex2Letter(maxRows - 1);
            throw new InvalidSeatException(
                    "Row '" + row + "' does not exist in this venue. "
                            + "Valid rows are A to " + lastValidRow + ".");
        }
    }

    /*
     * Throws InvalidSeatException if the given seat number is outside the
     * valid column range for the show's venue.
     */
    private static void validateSeat(int seatNo, int maxCols) throws InvalidSeatException {
        if (seatNo < 1 || seatNo > maxCols) {
            throw new InvalidSeatException(
                    "Seat number " + seatNo + " does not exist in this venue. "
                            + "Valid seat numbers are 1 to " + maxCols + ".");
        }
    }

    /*
     * Throws SeatAlreadyBookedException if the specified seat is already occupied.
     */
    private static void checkSeatAvailable(Show show, char row, int seatNo)
            throws SeatAlreadyBookedException {
        if (!show.seatCheckAvailability(row, seatNo)) {
            throw new SeatAlreadyBookedException(
                    "Seat " + row + seatNo + " is already booked. Please choose another seat.");
        }
    }

    /*
     * Reads an integer from the user within [min, max] (inclusive).
     * Keeps prompting until a valid integer in range is entered.
     */
    private static int readIntInRange(String prompt, int min, int max) {
        int value = -1;
        boolean valid = false;

        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Validation 1: Input must not be empty
            if (input.isEmpty()) {
                System.out.println("  !! Input cannot be empty. Please enter a number between "
                        + min + " and " + max + ".");
                continue;
            }

            try {
                value = Integer.parseInt(input);

                // Validation 2: Value must be within the allowed range
                if (value < min || value > max) {
                    System.out.println("  !! Please enter a number between " + min + " and " + max + ".");
                } else {
                    valid = true;
                }

            } catch (NumberFormatException e) {
                // Validation 3: Input must be a valid integer (no letters, decimals, symbols)
                System.out.println("  !! \"" + input + "\" is not a valid number. "
                        + "Please enter an integer between " + min + " and " + max + ".");
            }
        }

        return value;
    }

    /*
     * Reads an integer from the user within [min, max] OR 0 to go back.
     * Returns 0 if the user wants to go back.
     */
    private static int readIntWithBack(String prompt, int min, int max) {
        int value = -1;
        boolean valid = false;

        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Validation: Input must not be empty
            if (input.isEmpty()) {
                System.out.println("  !! Input cannot be empty. Enter 0 to go back or a number between "
                        + min + " and " + max + ".");
                continue;
            }

            try {
                value = Integer.parseInt(input);

                // 0 is the back signal – always accept it
                if (value == 0) {
                    valid = true;

                    // Otherwise must be in the valid range
                } else if (value < min || value > max) {
                    System.out.println("  !! Please enter 0 to go back, or a number between "
                            + min + " and " + max + ".");
                } else {
                    valid = true;
                }

            } catch (NumberFormatException e) {
                System.out.println("  !! \"" + input + "\" is not valid. Enter 0 to go back or a number between "
                        + min + " and " + max + ".");
            }
        }

        return value;
    }

    /*
     * Prompts the user to enter a row letter, or '0' to go back.
     * Validates that input is a single alphabetic character.
     */
    private static char readRowLetterWithBack(Show show) {
        char row = ' ';
        boolean valid = false;

        char firstRow = 'A';
        char lastRow = Venue.rowIndex2Letter(show.getVenue().getNoRows() - 1);

        while (!valid) {
            System.out.print("  Enter row letter (" + firstRow + " to " + lastRow
                    + ") or 0 to go back: ");
            String input = scanner.nextLine().trim().toUpperCase();

            // Validation 4: Input must not be empty
            if (input.isEmpty()) {
                System.out.println("  !! Input cannot be empty. Enter a row letter or 0 to go back.");
                continue;
            }

            // Check for back signal
            if (input.equals("0")) {
                return '0';
            }

            // Validation 5: Must be exactly one character
            if (input.length() != 1) {
                System.out.println("  !! Please enter a single letter (e.g. A, B, C) or 0 to go back.");
                continue;
            }

            // Validation 6: Must be an alphabetic letter
            if (!Character.isLetter(input.charAt(0))) {
                System.out.println("  !! \"" + input + "\" is not a letter. Enter a row letter or 0 to go back.");
                continue;
            }

            row = input.charAt(0);
            valid = true;
        }

        return row;
    }

    /*
     * Prompts the user to enter a seat number, or 0 to go back.
     * Returns 0 if the user wants to go back.
     */
    private static int readSeatNumberWithBack(Show show, int maxCols) {
        int seatNo = -1;
        boolean valid = false;

        while (!valid) {
            System.out.print("  Enter seat number (1 to " + maxCols + ") or 0 to go back: ");
            String input = scanner.nextLine().trim();

            // Validation 7: Input must not be empty
            if (input.isEmpty()) {
                System.out.println("  !! Input cannot be empty. Enter a seat number or 0 to go back.");
                continue;
            }

            try {
                seatNo = Integer.parseInt(input);

                // 0 is the back signal – always accept it
                if (seatNo == 0) {
                    valid = true;

                    // Validation 8: Seat number must be at least 1
                } else if (seatNo < 1) {
                    System.out.println("  !! Seat number must be at least 1. Enter 0 to go back.");
                } else {
                    valid = true;
                }

            } catch (NumberFormatException e) {
                // Validation 9: Input must be numeric
                System.out.println("  !! \"" + input + "\" is not valid. Enter a seat number or 0 to go back.");
            }
        }

        return seatNo;
    }

    /*
     * Counts how many seats in a show's venue are still available.
     */
    private static int countAvailableSeats(Show show) {
        Venue v = show.getVenue();
        int rows = v.getNoRows();
        int cols = v.getNoCols();
        int count = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 1; col <= cols; col++) {
                if (!v.checkOccupied(row, col)) {
                    count++;
                }
            }
        }

        return count;
    }
}