import java.util.*;

/**
 * File: InterStageQueue.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 *
 * The interstage queue represents a queue that sits between two stages. The queue has n stages that sit before
 * it and n stages that sit after it. The queue will store widgets inside of it. Widgets are taken by the next stage
 * and are added by the previous stage. Each interstage queue has a total capacity of qMax.
 */
public class InterStageQueue {

    private final ProductionLine line;
    private final List<Stage> next;
    private final List<Stage> prev;
    //the queue stores widgets like this to take statistics on how long they spent in the queue
    private final Queue<StoredWidget> widgets;
    private final int qMax; //total capacity of the queue.

    private int totalWidgets;
    private int count;
    private double totalTimeSpent;
    private int widgetsEntered;
    private final String name;

    /**
     * Creates a new interstage queue.
     *
     * @param line The production line which this queue sits on
     * @param name The name of the queue. This should be in the form Q[before][after]. For example Q12 would sit between stages 1 and 2.
     * @param qMax The maximum capacity of the queue.
     */
    public InterStageQueue(ProductionLine line, String name ,int qMax) {
        this.name = name;
        widgets = new ArrayDeque<>(qMax);
        this.next = new ArrayList<>();
        this.prev = new ArrayList<>();
        this.qMax = qMax;
        totalWidgets = 0;
        count = 0;
        this.line = line;
    }

    /**
     * The queue is full if the amount of widgets exceeds qMax.
     *
     * @return Returns true if the queue is full and false if it is not.
     */
    public boolean isFull() {
        return widgets.size()>=qMax;
    }

    /**
     * The queue is empty if it has 0 widgets.
     *
     * @return Returns true if the queue is empty and false if it is not.
     */
    public boolean isEmpty() {
        return widgets.isEmpty();
    }

    /**
     * Offers a new widget into the queue. A widget cannot be added if the queue is full. The widget must not
     * be null. When a widget is added to the queue it will run the {@link Stage#pull()} method on the next stages
     * which will cause any starved stages to start working on the newly added widget.
     *
     * Only the stages before this queue should offer a widget.
     *
     * @param widget
     * @throws IllegalStateException if the queue is full
     */
    public void offer(Widget widget) {
        //check if queue is full
        if(isFull())
            throw new IllegalStateException("Queue is full");

        //collect statistics
        widgetsEntered++;
        widgets.offer(new StoredWidget(widget,line.getCurrentTime()));
        for(Stage stage : next) {
            stage.pull();
        }
    }

    /**
     * Takes a widget from the queue. A widget cannot be taken if the queue is empty. When a widget is taken from
     * the queue it will run {@link Stage#push()} on all of the previous stages causing any blocked stages to move
     * their widget forward.
     *
     * @return The widget from the queue.
     * @throws IllegalStateException if the queue is empty
     */
    public Widget poll() {
        //check if empty
        if(isEmpty())
            throw new IllegalStateException("Queue is empty");

        //run statistics on the queue
        StoredWidget widget = widgets.poll();
        double delta = line.getCurrentTime() - widget.getTimeStamp();
        totalTimeSpent += delta;
        for(Stage stage : prev) {
            stage.push();
        }
        return widget.getWidget();
    }

    /**
     * Gathers statistics on the average amount of widgets in the queue at any time. This will record the amount of widgets in the queue now
     * and will add count for each time this is called.
     */
    public void gatherStats() {
        count++;
        totalWidgets += widgets.size();
    }

    public double getAverageTimeSpent() {
        //don't forget to include all the widgets still in the queue
        double extraTimeSpent = 0;
        for(StoredWidget widget : widgets) {
            double delta = line.getCompletionTime() - widget.getTimeStamp();
            extraTimeSpent += delta;
        }

        double total = totalTimeSpent + extraTimeSpent;
        return total/widgetsEntered;
    }

    /**
     * This stat is recorded by checking the amount of widgets in the queue on each event tick using {@link #gatherStats()}
     *
     * @return The average amount of widgets in the queue at any time.
     */
    public double getAverageWidgetsInQueue() {
        return ((double) totalWidgets)/count;
    }

    /**
     * @return The amount of widgets in storage at any time.
     */
    public int getAmountInStorage() {
        return widgets.size();
    }

    /**
     * @return The name of the queue. This will be in the form Q[prev][next], for example Q12 would be the queue between stage 1 and 2.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a stage so that it sits after this queue.
     *
     * @param stage The stage that is after this queue
     */
    public void addNext(Stage stage) {
        this.next.add(stage);
    }

    /**
     * Adds a stage so that it sits before this queue.
     *
     * @param stage The stage that sits before this queue.
     */
    public void addPrev(Stage stage) {
        this.prev.add(stage);
    }

    /**
     * Converts the queue to a string representation. This will be in the form
     * InterStageQueue[name='', max=qMax, inStorage='amountInStorage']
     *
     * @return The string representation of the queue.
     */
    @Override
    public String toString() {
        return "InterStageQueue[name="+getName()+", max="+qMax+", inStorage="+getAmountInStorage()+"]";
    }
}
