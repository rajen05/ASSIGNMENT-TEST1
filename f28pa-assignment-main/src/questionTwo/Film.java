package questionTwo;

/**
 * Film.java
 *
 * Represents a film that can be screened at a venue.
 * A film has a title and a session time (1 = Afternoon, 2 = Evening, 3 = Night).
 *
 * F28PA | Software Development A | Coursework
 */
public class Film {

    // ─── Data Fields ─────────────────────────────────────────────────────────────
    // Protected so that subclasses (if created in future) can access them directly.

    /** The title of the film, e.g. "SING" */
    protected String filmTitle;

    /**
     * The session time indicator:
     *   1 = Afternoon (1pm)
     *   2 = Evening   (5pm)
     *   3 = Night     (9pm)
     */
    protected int sessionTime;

    // ─── Constructor ──────────────────────────────────────────────────────────────

    /**
     * Constructs a Film with the given title and session time.
     *
     * @param title       The title of the film
     * @param sessionTime An integer representing the session: 1, 2, or 3
     */
    public Film(String title, int sessionTime) {
        this.filmTitle   = title;
        this.sessionTime = sessionTime;
    }

    // ─── Methods ─────────────────────────────────────────────────────────────────

    /**
     * Converts the numeric session time to a descriptive string.
     * Uses a switch statement for clarity.
     *
     * @return A string describing the session time, e.g. "Afternoon (1pm)"
     */
    public String getSession() {
        switch (sessionTime) {
            case 1:
                return "Afternoon (1pm)";
            case 2:
                return "Evening (5pm)";
            case 3:
                return "Night (9pm)";
            default:
                return "Unknown Session";
        }
    }

    /**
     * Returns the title of this film.
     *
     * @return filmTitle as a String
     */
    public String getTitle() {
        return filmTitle;
    }

    // ─── toString ─────────────────────────────────────────────────────────────────

    /**
     * Returns a readable summary of this film, including title and session time.
     * Example: "SING | Afternoon (1pm)"
     *
     * @return formatted string for display
     */
    @Override
    public String toString() {
        return filmTitle + " | " + getSession();
    }
}