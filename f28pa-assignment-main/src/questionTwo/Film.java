package questionTwo;

/**
 * Film.java
 *
 * Represents a film that can be screened at a venue.
 * A film has a title and a session time (1 = Afternoon, 2 = Evening, 3 =
 * Night).
 *
 * F28PA | Software Development A | Coursework
 */
public class Film {

    /** The title of the film, e.g. "SING" */
    protected String filmTitle;

    /**
     * The session time indicator:
     * 1 = Afternoon (1pm)
     * 2 = Evening (5pm)
     * 3 = Night (9pm)
     */
    protected int sessionTime;

    /*
     * Constructs a Film.
     */
    public Film(String title, int sessionTime) {
        this.filmTitle = title;
        this.sessionTime = sessionTime;
    }

    /*
     * Returns the session time description (e.g. "Afternoon (1pm)").
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

    public String getTitle() {
        return filmTitle;
    }

    @Override
    public String toString() {
        return filmTitle + " | " + getSession();
    }
}