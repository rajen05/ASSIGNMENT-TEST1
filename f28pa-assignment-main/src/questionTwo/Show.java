package questionTwo;

/**
 * Show.java
 *
 * Represents a cinema show: a specific film being screened in a specific venue.
 * Show aggregates a Venue (composition relationship in the UML) and holds a
 * Film.
 *
 * Key responsibilities:
 * - Buying (booking) a ticket for a seat in this show's venue
 * - Checking seat availability
 * - Displaying the hall layout and availability information
 *
 * F28PA | Software Development A | Coursework
 */
public class Show {

    /** The film being screened in this show */
    private Film filmName;

    /** The venue where this show is being screened */
    private Venue venueID;

    /*
     * Creates a Show with a Venue and a Film.
     */
    public Show(String venueID, Film filmName) {
        // Create the Venue object using the provided ID
        this.venueID = new Venue(venueID);
        this.filmName = filmName;
    }

    /*
     * Books a ticket for the given row and seat number.
     * Calls the Venue's bookASeat method.
     */
    public void buyTicket(char row, int seatNo) {
        int rowIdx = Venue.rowLetter2Idx(row); // Convert letter to 0-based index
        venueID.bookASeat(rowIdx, seatNo);
    }

    /*
     * Checks whether a seat is available in the venue.
     */
    public boolean seatCheckAvailability(char row, int seatNo) {
        int rowIdx = Venue.rowLetter2Idx(row);
        return !venueID.checkOccupied(rowIdx, seatNo);
    }

    /*
     * Prints the visual seat map for the venue.
     */
    public void printHall() {
        System.out.println("  Venue: " + venueID.getID()
                + "  |  Film: " + filmName.getTitle()
                + "  |  Session: " + filmName.getSession());
        System.out.println();
        System.out.println(venueID.seatVenueDisplay());
    }

    /*
     * Prints a text summary of seat availability.
     */
    public void printAvailability() {
        int total = venueID.getNumOfSeats();
        int noRows = venueID.getNoRows();
        int noCols = venueID.getNoCols();

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

        System.out.println("  Film: " + filmName.getTitle()
                + "  |  Session: " + filmName.getSession()
                + "  |  Venue: " + venueID.getID());
        System.out.println("  Total Seats: " + total
                + "  |  Booked: " + booked
                + "  |  Available: " + available);

        // Alert user if the venue is completely sold out
        if (venueID.checkIfVenueIsFull()) {
            System.out.println("  *** SOLD OUT ***");
        }
    }

    // Returns location info string.

    public String locationInfo() {
        return "Venue " + venueID.getID()
                + " | " + venueID.getNoRows() + " rows x " + venueID.getNoCols() + " cols"
                + " | Total Seats: " + venueID.getNumOfSeats();
    }

    public Film getFilm() {
        return filmName;
    }

    public Venue getVenue() {
        return venueID;
    }

    @Override
    public String toString() {
        return "Show: " + filmName.getTitle()
                + " | " + filmName.getSession()
                + " | Venue: " + venueID.getID();
    }
}