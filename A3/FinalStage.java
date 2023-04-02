/**
 * File: FinalStage.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Represents an end-stage in the production line. This stage does not have a next stage. This will instead move the completed to the {@link Warehouse}. which
 * handles fully developed widgets.
 */
public class FinalStage extends Stage {

    private final Warehouse warehouse;

    /**
     * Creates a new FinalStage
     *
     * @param line The production line which this stage is apart of
     * @param warehouse The warehouse to move completed widgets to.
     * @param m The average processing time
     * @param n The range of processing time
     * @param name The name of the stage. This should be in the form of S[ordinal]<parallel> for example s0a, s1, s3b
     */
    public FinalStage(ProductionLine line, Warehouse warehouse, InterStageQueue prev, double m, double n, String name) {
        super(line,prev, null, m, n, name);
        this.warehouse = warehouse;
    }

    /**
     * Moves the completed widget ot the warehouse.
     *
     * @param widget The widget to push
     */
    @Override
    protected void pushWidget(Widget widget) {
        warehouse.offer(widget);
    }

    /**
     * Takes a widget from the previous interstage queue. Adds a stamp to show it was made here.
     *
     * @return The pulled widget.
     */
    @Override
    protected Widget pullWidget() {
        return getPrev().poll();
    }
}
