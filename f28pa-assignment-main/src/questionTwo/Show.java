package questionTwo;

/**
 * Show.java
 *
 * Represents a cinema show: a specific film being screened in a specific venue.
 * Show aggregates a Venue (composition relationship in the UML) and holds a Film.
 *
 * Key responsibilities:
 *   - Buying (booking) a ticket for a seat in this show's venue
 *   - Checking seat availability
 *   - Displaying the hall layout and availability information
 *
 * F28PA | Software Development A | Coursework
 */
public class Show {

    // ─── Data Fields ─────────────────────────────────────────────────────────────

    /** The film being screened in this show */
    private Film filmName;

    /** The venue where this show is being screened */
    private Venue venueID;

    // ─── Constructor ──────────────────────────────────────────────────────────────

    /**
     * Constructs a Show by creating a Venue from the given ID string
     * and associating it with the provided Film.
     *
     * @param venueID  The venue identifier string, e.g. "1E", "2W"
     * @param filmName The Film object to be screened at this show
     */
    public Show(String venueID, Film filmName) {
        // Create the Venue object using the provided ID
        this.venueID  = new Venue(venueID);
        this.filmName = filmName;
    }

    // ─── Ticket Booking ───────────────────────────────────────────────────────────

    /**
     * Books a ticket for the given row letter and seat number in this show's venue.
     * Converts the row letter to a 0-based index then delegates to Venue.bookASeat().
     *
     * @param row    The row letter (e.g. 'A', 'B', 'C')
     * @param seatNo The seat number (1-based, e.g. 1, 2, 3)
     */
    public void buyTicket(char row, int seatNo) {
        int rowIdx = Venue.rowLetter2Idx(row); // Convert letter to 0-based index
        venueID.bookASeat(rowIdx, seatNo);
    }

    /**
     * Checks whether a specific seat in this show's venue is available.
     *
     * @param row    The row letter (e.g. 'A')
     * @param seatNo The seat number (1-based)
     * @return true if the seat is available (not occupied), false if already booked
     */
    public boolean seatCheckAvailability(char row, int seatNo) {
        int rowIdx = Venue.rowLetter2Idx(row);
        return !venueID.checkOccupied(rowIdx, seatNo);
    }

    // ─── Display Methods ─────────────────────────────────────────────────────────

    /**
     * Prints the visual seat map (hall layout) for this show's venue.
     * Shows each seat as [ ] (available) or [X] (booked).
     */
    public void printHall() {
        System.out.println("  Venue: " + venueID.getID()
                + "  |  Film: " + filmName.getTitle()
                + "  |  Session: " + filmName.getSession());
        System.out.println();
        System.out.println(venueID.seatVenueDisplay());
    }

    /**
     * Prints a summary of seat availability for this show.
     * Shows how many seats are booked and how many remain available.
     */
    public void printAvailability() {
        int total     = venueID.getNumOfSeats();
        int noRows    = venueID.getNoRows();
        int noCols    = venueID.getNoCols();

        // Count how many available seats remain by checking each one
        int available = 0;
        for (int row = 0; row < noRows; row++) {
            for (int col = 1; col <= noCols; col++) {
                if (!venueID.checkOccupied(row, col)) {
                    available++;
                }
            }
        }
        int booked = total - available;

        System.out.println("  Film: "        + filmName.getTitle()
                + "  |  Session: "           + filmName.getSession()
                + "  |  Venue: "             + venueID.getID());
        System.out.println("  Total Seats: " + total
                + "  |  Booked: "            + booked
                + "  |  Available: "         + available);

        // Alert user if the venue is completely sold out
        if (venueID.checkIfVenueIsFull()) {
            System.out.println("  *** SOLD OUT ***");
        }
    }

    /**
     * Returns a location info string describing this show's venue and seat capacity.
     *
     * @return formatted location information string
     */
    public String locationInfo() {
        return "Venue " + venueID.getID()
                + " | " + venueID.getNoRows() + " rows x " + venueID.getNoCols() + " cols"
                + " | Total Seats: " + venueID.getNumOfSeats();
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────

    /**
     * @return the Film associated with this show
     */
    public Film getFilm() {
        return filmName;
    }

    /**
     * @return the Venue associated with this show
     */
    public Venue getVenue() {
        return venueID;
    }

    // ─── toString ─────────────────────────────────────────────────────────────────

    /**
     * Returns a readable summary string for this show.
     * Example: "Show: SING | Afternoon (1pm) | Venue: 1N"
     *
     * @return formatted show summary
     */
    @Override
    public String toString() {
        return "Show: " + filmName.getTitle()
                + " | "   + filmName.getSession()
                + " | Venue: " + venueID.getID();
    }
}