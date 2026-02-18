package questionTwo;

/*
 * BookingExceptions.java
 *
 * A single file that groups all custom exceptions used by the BookingSoft system.
 * Each exception handles a specific booking error scenario, making error handling
 * clear, organised, and easy to maintain in one place.
 *
 * Exceptions defined here:
 *   1. InvalidSeatException - row or seat number is out of range for the venue
 *   2. SeatAlreadyBookedException - the chosen seat has already been booked
 *   3. VenueFullException - the entire venue/show is sold out
 *
 
 * F28PA | Software Development A | Coursework
 */

/*
 * Thrown when a user enters a seat location that is outside the valid range.
 */
class InvalidSeatException extends Exception {

    public InvalidSeatException(String message) {
        super(message);
    }
}

/*
 * Thrown when a user attempts to book a seat that is already booked.
 */
class SeatAlreadyBookedException extends Exception {

    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}

/*
 * Thrown when the venue is sold out.
 */
class VenueFullException extends Exception {

    public VenueFullException(String message) {
        super(message);
    }
}
