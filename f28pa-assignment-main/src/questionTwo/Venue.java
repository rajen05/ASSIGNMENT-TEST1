package questionTwo;

/*
 * Venue.java
 *
 * Represents a cinema screening venue (hall).
 * A venue has a unique ID, lines/rows, and a list of tickets.
 *
 * Venue types:
 *   Type E: 7x7
 *   Type W: 5x7
 *   Type N: 7x5
 *   Type S: 9x9
 *
 * F28PA | Software Development A | Coursework
 */
public class Venue {

    /** Unique venue identifier, e.g. "1E", "2W" (number + type letter) */
    private String id;

    /** Number of rows in this venue */
    private int noRows;

    /** Number of columns (seats per row) in this venue */
    private int noCols;

    /** 2D array of Ticket objects; tickets[row][col] represents each seat */
    private Ticket[][] tickets;

    /** Total number of tickets that exist in this venue (noRows * noCols) */
    private int numOfTickets;

    /** Count of how many seats have been booked so far */
    private int numOfSeatsOccupied;

    /*
     * Constructs a Venue from its ID string (e.g. "1E").
     * The last character (E, W, N, S) determines the size.
     */
    public Venue(String id) {
        this.id = id;
        this.numOfSeatsOccupied = 0;

        // Extract the type letter from the last character of the ID
        char venueType = id.charAt(id.length() - 1);

        // Set dimensions based on venue type
        switch (venueType) {
            case 'E':
                noRows = 7;
                noCols = 7;
                break;
            case 'W':
                noRows = 5;
                noCols = 7;
                break;
            case 'N':
                noRows = 7;
                noCols = 5;
                break;
            case 'S':
                noRows = 9;
                noCols = 9;
                break;
            default:
                // Default fallback (should not happen with valid input)
                noRows = 5;
                noCols = 5;
                break;
        }

        // Total number of tickets = rows x columns
        numOfTickets = noRows * noCols;

        // Initialise the 2D array and create a Ticket for every seat
        tickets = new Ticket[noRows][noCols];
        for (int row = 0; row < noRows; row++) {
            for (int col = 0; col < noCols; col++) {
                // All seats start as unoccupied (false)
                tickets[row][col] = new Ticket(row, col, false);
            }
        }
    }

    public String getID() {
        return id;
    }

    public int getNoRows() {
        return noRows;
    }

    public int getNoCols() {
        return noCols;
    }

    public int getNumOfSeats() {
        return numOfTickets;
    }

    /*
     * Books a seat at the given row index and seat number.
     * Marks the ticket as occupied.
     */
    public void bookASeat(int rowIdx, int seatNo) {
        int colIdx = seatNo - 1; // Convert 1-based seatNo to 0-based column index
        tickets[rowIdx][colIdx].setIsOccupied(true);
        numOfSeatsOccupied++; // Track how many seats are now booked
    }

    /*
     * Checks if a specific seat is already occupied.
     */
    public boolean checkOccupied(int rowIdx, int seatNo) {
        int colIdx = seatNo - 1;
        return tickets[rowIdx][colIdx].getIsOccupied();
    }

    /*
     * Returns a string illustrating the seat layout.
     * Available = [ ], Booked = [X].
     */
    public String seatVenueDisplay() {
        String display = "";

        // Print column header numbers
        display += "     "; // Left padding to align with row labels
        for (int col = 1; col <= noCols; col++) {
            display += String.format("%-4d", col);
        }
        display += "\n";

        // Print a separator line
        display += "     ";
        for (int col = 0; col < noCols; col++) {
            display += "----";
        }
        display += "\n";

        // Print each row: letter label + seat status symbols
        for (int row = 0; row < noRows; row++) {
            char rowLabel = rowIndex2Letter(row);
            display += rowLabel + "  | ";
            for (int col = 0; col < noCols; col++) {
                if (tickets[row][col].getIsOccupied()) {
                    display += "[X] "; // Occupied seat
                } else {
                    display += "[ ] "; // Available seat
                }
            }
            display += "\n";
        }

        return display;
    }

    /*
     * Returns true if every seat in the venue is booked.
     */
    public boolean checkIfVenueIsFull() {
        return numOfSeatsOccupied >= numOfTickets;
    }

    /*
     * Helper to convert row letter to index (e.g. 'A' -> 0).
     */
    public static int rowLetter2Idx(char letter) {
        return (int) (letter) - 65;
    }

    /*
     * Helper to convert index to row letter (e.g. 0 -> 'A').
     */
    public static char rowIndex2Letter(int idx) {
        return (char) (idx + 'A');
    }

    @Override
    public String toString() {
        int available = numOfTickets - numOfSeatsOccupied;
        return "Venue " + id
                + " | Seats: " + numOfTickets
                + " | Booked: " + numOfSeatsOccupied
                + " | Available: " + available;
    }
}