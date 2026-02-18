package questionTwo;

/**
 * Venue.java
 *
 * Represents a cinema screening venue (hall).
 * A venue has a unique ID (e.g. "1E"), a fixed number of rows and columns,
 * and a 2D array of Ticket objects representing every seat.
 *
 * Venue types and their dimensions:
 *   Type E: 7 rows x 7 columns
 *   Type W: 5 rows x 7 columns
 *   Type N: 7 rows x 5 columns
 *   Type S: 9 rows x 9 columns
 *
 * F28PA | Software Development A | Coursework
 */
public class Venue {

    // ─── Data Fields ─────────────────────────────────────────────────────────────

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

    // ─── Constructor ──────────────────────────────────────────────────────────────

    /**
     * Constructs a Venue from its ID string.
     * The last character of the ID is the type letter (E, W, N, S),
     * which determines the row and column count.
     *
     * Example: "1E" -> type 'E' -> 7 rows x 7 cols
     *
     * @param id The venue identifier, e.g. "1E", "2W"
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

    // ─── Getters ─────────────────────────────────────────────────────────────────

    /**
     * @return the unique ID of this venue (e.g. "1E")
     */
    public String getID() {
        return id;
    }

    /**
     * @return number of rows in this venue
     */
    public int getNoRows() {
        return noRows;
    }

    /**
     * @return number of columns (seats per row) in this venue
     */
    public int getNoCols() {
        return noCols;
    }

    /**
     * @return total number of seats (tickets) in this venue
     */
    public int getNumOfSeats() {
        return numOfTickets;
    }

    // ─── Seat Booking ─────────────────────────────────────────────────────────────

    /**
     * Books a seat at the given row index and seat number (1-based column).
     * Marks the corresponding Ticket as occupied and increments the occupied count.
     *
     * @param rowIdx  0-based row index
     * @param seatNo  1-based seat number (will be converted to 0-based column index)
     */
    public void bookASeat(int rowIdx, int seatNo) {
        int colIdx = seatNo - 1; // Convert 1-based seatNo to 0-based column index
        tickets[rowIdx][colIdx].setIsOccupied(true);
        numOfSeatsOccupied++;    // Track how many seats are now booked
    }

    /**
     * Checks whether a specific seat is already occupied.
     *
     * @param rowIdx  0-based row index
     * @param seatNo  1-based seat number
     * @return true if the seat is occupied, false if available
     */
    public boolean checkOccupied(int rowIdx, int seatNo) {
        int colIdx = seatNo - 1;
        return tickets[rowIdx][colIdx].getIsOccupied();
    }

    /**
     * Returns a multi-line string displaying all seat availability in this venue.
     * Each row is labelled with its letter, each column with its number.
     * Available seats are shown as [ ] and booked seats as [X].
     *
     * @return String showing the full seat map of this venue
     */
    public String seatVenueDisplay() {
        StringBuilder sb = new StringBuilder();

        // Print column header numbers
        sb.append("     "); // Left padding to align with row labels
        for (int col = 1; col <= noCols; col++) {
            sb.append(String.format("%-4d", col));
        }
        sb.append("\n");

        // Print a separator line
        sb.append("     ");
        for (int col = 0; col < noCols; col++) {
            sb.append("----");
        }
        sb.append("\n");

        // Print each row: letter label + seat status symbols
        for (int row = 0; row < noRows; row++) {
            char rowLabel = rowIndex2Letter(row);
            sb.append(rowLabel).append("  | ");
            for (int col = 0; col < noCols; col++) {
                if (tickets[row][col].getIsOccupied()) {
                    sb.append("[X] "); // Occupied seat
                } else {
                    sb.append("[ ] "); // Available seat
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Checks whether every seat in the venue has been booked.
     *
     * @return true if the venue is completely full, false otherwise
     */
    public boolean checkIfVenueIsFull() {
        return numOfSeatsOccupied >= numOfTickets;
    }

    // ─── Helper Methods (Provided) ────────────────────────────────────────────────

    /**
     * [Provided] Converts a row letter (char) to a 0-based index number.
     * e.g. 'A' -> 0, 'B' -> 1, 'C' -> 2
     *
     * @param letter The row letter
     * @return 0-based integer index
     */
    public static int rowLetter2Idx(char letter) {
        return (int) (letter) - 65;
    }

    /**
     * [Provided] Converts a 0-based index number to a row letter (char).
     * e.g. 0 -> 'A', 1 -> 'B', 2 -> 'C'
     *
     * @param idx The 0-based row index
     * @return The corresponding row letter
     */
    public static char rowIndex2Letter(int idx) {
        return (char) (idx + 'A');
    }

    // ─── toString ─────────────────────────────────────────────────────────────────

    /**
     * Returns a summary string for this venue.
     * Example: "Venue 1E | Seats: 49 | Booked: 3 | Available: 46"
     *
     * @return formatted venue summary
     */
    @Override
    public String toString() {
        int available = numOfTickets - numOfSeatsOccupied;
        return "Venue " + id
                + " | Seats: "     + numOfTickets
                + " | Booked: "    + numOfSeatsOccupied
                + " | Available: " + available;
    }
}