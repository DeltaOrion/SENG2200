/**
 * File: A3.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Represents a widget stored in an {@link InterStageQueue}. This class exists so that the average time each widget
 * spent in a queue can be calculated. This class tracks when the widget first entered the queue
 */
public class StoredWidget {

    private final Widget widget; //the stored widget
    private final double timeStamp; //when the widget entered the queue.

    /**
     * Creates a new stored widget
     *
     * @param widget
     * @param timeStamp
     */
    public StoredWidget(Widget widget, double timeStamp) {
        this.widget = widget;
        this.timeStamp = timeStamp;
    }

    public Widget getWidget() {
        return widget;
    }

    public double getTimeStamp() {
        return timeStamp;
    }
}
