import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * File: Widget.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Represents a widget. A widget can be anything created by a production line, whether it be a car, boat or some toy. That is not important. The more
 * important thing is its role in the production line. Each widget has a uniqueId and a set of stamps marking the areas it has been in the production line.
 */
public class Widget {

    private final String uniqueId; //the name of the widget
    private final Set<Stage> stamps; //all of the stages which this widget has been to

    /**
     * Creates a new widget
     *
     * @param uniqueId A unique name of the widget. The name should be in the form ID + Creation Stage Code, for example 0A, or 5B. Codes should
     *                 be generated by the {@link IdGenerator} singleton.
     */
    public Widget(String uniqueId) {
        this.uniqueId = uniqueId;
        this.stamps = new HashSet<>();
    }

    /**
     * This marks that the stage has visited the given stage
     *
     * @param stage The stage which the widget has visited.
     */
    public void addStamp(Stage stage) {
        if(stamps.contains(stage))
            throw new IllegalStateException("Widget cannot go through same stage twice!");

        stamps.add(stage);
    }

    /**
     * A unique name of the widget. The name should be in the form ID + Creation Stage Code, for example 0A, or 5B. Codes should
     * be generated by the {@link IdGenerator} singleton.
     *
     * @return The name of the widget
     */
    public String getId() {
        return uniqueId;
    }

    /**
     * Represents the widget as a string. If the widget has no name it will be called a "Raw Widget" otherwise it will
     * be called "Widget {@link #getId()}"
     *
     * @return
     */
    public String toString() {
        if(getId()==null)
            return "Raw Widget";

        return "Widget "+ getId();
    }

    /**
     * @return  an immutable collection of all of the stages this widget has visited thus far.
     */
    public Collection<Stage> getStamps() {
        return Collections.unmodifiableSet(stamps);
    }
}
