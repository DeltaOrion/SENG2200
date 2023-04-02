import java.util.Locale;

/**
 * File: CreationalStage.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Represents a stage that creates widgets. This stage does not have a previous interstage queue
 * as it is the first in the production line. This stage will create a widget, give it a new unique ID using the Id Generator
 * and pass the id to the next queue after the stage.
 */
public class CreationalStage extends Stage {

    /**
     * Creates a new CreationalStage
     *
     * @param line The production line which this stage is apart of
     * @param next The next queue in the production line.
     * @param m The average processing time
     * @param n The range of processing time
     * @param name The name of the stage. This should be in the form of S[ordinal]<parallel> for example s0a, s1, s3b
     */
    public CreationalStage(ProductionLine line, InterStageQueue next, double m, double n, String name) {
        super(line,null,next, m, n, name);
    }

    /**
     * @return Returns the name of this stage. For example if the stage was called s0a this would return A.
     */
    private String getCode() {
        return String.valueOf(getName().charAt(getName().length()-1)).toUpperCase(Locale.ROOT);
    }

    /**
     * Moves the widget to the next queue.
     *
     * @param widget The widget to push
     */
    @Override
    protected void pushWidget(Widget widget) {
        getNext().offer(widget);
    }

    /**
     * Creates a widget, gives it a unique id using the {@link IdGenerator} and marks that the widget was created here.
     *
     * @return A newly created widget
     */
    @Override
    protected Widget pullWidget() {
        String name = IdGenerator.instance().getId() + getCode(); //id + A/B
        return new Widget(name);
    }

}
