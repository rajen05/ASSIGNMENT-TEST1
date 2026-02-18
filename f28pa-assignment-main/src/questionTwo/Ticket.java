package questionTwo;

/**
 * Ticket.java
 *
 * Represents a single seat (ticket) within a venue.
 * Each ticket stores its row letter, seat number, and whether it has been booked.
 *
 * F28PA | Software Development A | Coursework
 */
public class Ticket {

    // ─── Data Fields ─────────────────────────────────────────────────────────────

    /** The row letter for this seat, e.g. 'A', 'B', 'C' */
    private char rowLetter;

    /** The seat number (1-based) for this seat, e.g. 1, 2, 3 */
    private int seatNo;

    /** Booking status: true = occupied/booked, false = available */
    private boolean isOccupied;

    // ─── Constructor ──────────────────────────────────────────────────────────────

    /**
     * Constructs a Ticket using 0-based row and column indices.
     * Row index is converted to a letter (0 -> 'A', 1 -> 'B', etc.).
     * Column index is converted to a 1-based seat number (0 -> 1, 1 -> 2, etc.).
     *
     * @param rowIdx     0-based row index
     * @param colIdx     0-based column index
     * @param isOccupied initial occupied status
     */
    public Ticket(int rowIdx, int colIdx, boolean isOccupied) {
        this.rowLetter = (char) (rowIdx + 'A');   // 0 -> 'A', 1 -> 'B', ...
        this.seatNo    = colIdx + 1;               // 0 -> 1, 1 -> 2, ...
        this.isOccupied = isOccupied;
    }

    // ─── Getters and Setters ─────────────────────────────────────────────────────

    /**
     * @return the row letter for this seat (e.g. 'A')
     */
    public char getRowLetter() {
        return rowLetter;
    }

    /**
     * @return the seat number (1-based) for this seat (e.g. 3)
     */
    public int getSeatNo() {
        return seatNo;
    }

    /**
     * @return true if this seat has been booked, false if still available
     */
    public boolean getIsOccupied() {
        return isOccupied;
    }

    /**
     * Updates the booking status of this seat.
     * @param isOccupied true to mark as booked, false to mark as available
     */
    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    // ─── toString ─────────────────────────────────────────────────────────────────

    /**
     * Returns a human-readable description of this ticket.
     * Example output: "Seat A3 [OCCUPIED]" or "Seat B5 [AVAILABLE]"
     *
     * @return formatted string describing this ticket
     */
    @Override
    public String toString() {
        String status = isOccupied ? "OCCUPIED" : "AVAILABLE";
        return "Seat " + rowLetter + seatNo + " [" + status + "]";
    }
}