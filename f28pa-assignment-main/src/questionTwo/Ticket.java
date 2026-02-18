package questionTwo;

/**
 * Ticket.java
 *
 * Represents a single seat (ticket) within a venue.
 * Each ticket stores its row letter, seat number, and whether it has been
 * booked.
 *
 * F28PA | Software Development A | Coursework
 */
public class Ticket {

    /** The row letter for this seat, e.g. 'A', 'B', 'C' */
    private char rowLetter;

    /** The seat number (1-based) for this seat, e.g. 1, 2, 3 */
    private int seatNo;

    /** Booking status: true = occupied/booked, false = available */
    private boolean isOccupied;

    /*
     * Constructor for a Ticket.
     * Row index is converted to a letter (0 -> 'A').
     * Column index is converted to a seat number (0 -> 1).
     */
    public Ticket(int rowIdx, int colIdx, boolean isOccupied) {
        this.rowLetter = (char) (rowIdx + 'A'); // 0 -> 'A', 1 -> 'B', ...
        this.seatNo = colIdx + 1; // 0 -> 1, 1 -> 2, ...
        this.isOccupied = isOccupied;
    }

    public char getRowLetter() {
        return rowLetter;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    /*
     * Returns a string like: "Seat A3 [OCCUPIED]"
     */
    @Override
    public String toString() {
        String status = isOccupied ? "OCCUPIED" : "AVAILABLE";
        return "Seat " + rowLetter + seatNo + " [" + status + "]";
    }
}