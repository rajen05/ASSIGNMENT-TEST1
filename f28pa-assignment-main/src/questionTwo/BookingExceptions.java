package questionTwo;

/**
 * BookingExceptions.java
 *
 * A single file that groups all custom exceptions used by the BookingSoft system.
 * Each exception handles a specific booking error scenario, making error handling
 * clear, organised, and easy to maintain in one place.
 *
 * Exceptions defined here:
 *   1. InvalidSeatException       - row or seat number is out of range for the venue
 *   2. SeatAlreadyBookedException - the chosen seat has already been booked
 *   3. VenueFullException         - the entire venue/show is sold out
 *
 * Note: Only one class in a Java file can be declared public.
 * The other classes here use package-private access (no modifier),
 * which means they are still fully accessible within the same package (questionTwo).
 *
 * F28PA | Software Development A | Coursework
 */


// ─── Exception 1 ─────────────────────────────────────────────────────────────

/**
 * Thrown when a user enters a seat location that is outside the valid
 * range of the venue — for example, row 'Z' in a 7-row venue,
 * or seat number 10 in a 5-column venue.
 */
class InvalidSeatException extends Exception {

    /**
     * Constructs an InvalidSeatException with a descriptive message.
     *
     * @param message A description of why the seat location is invalid
     */
    public InvalidSeatException(String message) {
        super(message);
    }
}


// ─── Exception 2 ─────────────────────────────────────────────────────────────

/**
 * Thrown when a user attempts to book a seat that has already been
 * reserved by a previous customer.
 * Prompts the user to choose a different available seat instead.
 */
class SeatAlreadyBookedException extends Exception {

    /**
     * Constructs a SeatAlreadyBookedException with a descriptive message.
     *
     * @param message A description of the double-booking attempt
     */
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}


// ─── Exception 3 ─────────────────────────────────────────────────────────────

/**
 * Thrown when a customer attempts to book a ticket for a show whose
 * venue has already sold every available seat.
 * Redirects the user back to the show list to pick a different show.
 */
class VenueFullException extends Exception {

    /**
     * Constructs a VenueFullException with a descriptive message.
     *
     * @param message A description explaining that the venue is completely full
     */
    public VenueFullException(String message) {
        super(message);
    }
}
