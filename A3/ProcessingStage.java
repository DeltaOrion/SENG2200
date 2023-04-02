/**
 * File: ProcessingStage.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Represents a stage that processes widgets. This stage will take the widget from the previous {@link InterStageQueue}, process it
 * and then move it over to the next queue.
 */
public class ProcessingStage extends Stage {

    /**
     * Creates a new processing stage.
     *
     * @param line The production line which this stage is apart of
     * @param prev The previous queue in the production line.
     * @param next The next queue in the production line.
     * @param m The average processing time
     * @param n The range of processing time
     * @param name The name of the stage. This should be in the form of S[ordinal]<parallel> for example s0a, s1, s3b
     */
    public ProcessingStage(ProductionLine line, InterStageQueue prev, InterStageQueue next, double m, double n, String name) {
        super(line,prev, next, m, n, name);
    }

    /**
     * Moves the current widget to the next queue.
     *
     * @param widget The widget to push
     */
    @Override
    protected void pushWidget(Widget widget) {
        getNext().offer(widget);
    }

    /**
     * Takes a widget from the previous queue.
     *
     * @return The widget from the previous queue.
     */
    @Override
    protected Widget pullWidget() {
        return getPrev().poll();
    }
}
