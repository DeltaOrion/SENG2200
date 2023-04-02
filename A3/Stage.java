import java.util.Random;

/**
 * File: Stage.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Abstract class that represents a production line stage. The stage has two main operations, pull and push. Pull takes a {@link Widget} from the previous
 * {@link InterStageQueue}, processes it and after it has finished processing it move it into the next interstage queue. The stage is also required to collect important
 * statistics about itself. A stage can be in 1 of 3 states
 *   - Starved: The stage cannot work as there are no widgets to pull from
 *   - Blocked: The stage cannot work as it cannot push the widget to the next {@link InterStageQueue}
 *   - Working: The stage is actively processing a widget
 */
public abstract class Stage {

    private final double M; //Average Processing Time
    private final double N; //Range of processing time
    private final Random random;
    private InterStageQueue next;
    private InterStageQueue prev;
    private final ProductionLine line; //the line is required so that the stage can add jobs to the discrete event simulation

    private final String name;
    private boolean blocked;

    private Widget currentWidget;
    private Job currentJob;

    private double blockedTime;
    private double blockedTimeStamp;

    private double workTime;
    private double workTimeStamp;

    /**
     * Creates a new stage
     *
     * @param prev The previous stage or null if this is the first stage
     * @param next The next stage or null if this is the final stage
     * @param m The average processing time
     * @param n The range of processing time
     * @param line The production line this stage is apart of
     * @param name The name of the stage. This should be in the form of S[ordinal][parallel] for example s0a
     */
    public Stage(ProductionLine line ,InterStageQueue prev, InterStageQueue next, double m, double n, String name) {
        this.M = m;
        this.N = n;
        this.random = new Random(System.nanoTime()); //seed random to nanoTime to ensure it is properly random
        this.next = null;
        this.prev = null;
        this.name = name;

        this.prev = prev;
        if(prev!=null)
            prev.addNext(this);
        this.next = next;
        if(next!=null)
            next.addPrev(this);
        this.blocked = false;
        this.line = line;
    }

    /**
     * @return Name of the stage. This should be in the form S[ordinal][parallel]
     */
    public String getName() {
        return name;
    }

    /**
     * Moves the currently worked on widget to the next {@link InterStageQueue}. If this is currently working
     * on a widget or is blocked, that is the next queue is full then this will do nothing. This method will then
     * immediately attempt to grab the next widget from the queue before.
     */
    public void push() {
        double currentTime = line.getCurrentTime();
        if(currentWidget==null) //we have no widget to push
            return;

        if(currentJob!=null) {
            //we are working
            if(line.getCurrentTime() < currentJob.getCompletionTime())
                return;
        }

        if(getNext()!=null && getNext().isFull()) {
            //we are blocked
            if(!blocked) {
                blockedTimeStamp = currentTime;
                //if we are blocked then work is over, calculate work time
                workTime += (currentTime-workTimeStamp);
            }
            blocked = true;
            return;
        }

        if(blocked) {
            //we were blocked, do not recalculate work time
            //calculate blocked time
            blockedTime += (currentTime - blockedTimeStamp);
            blocked = false;
        } else {
            //we were never blocked so work ended now
            workTime += (currentTime-workTimeStamp);
        }


        Widget widget = currentWidget;
        currentWidget = null;
        currentJob = null;

        //what the widget does will depend on the type
        pushWidget(widget);

        //attempt to get the next widget
        pull();
    }

    /**
     * Handles a widget which has been pushed. This is a polymorphic method that will vary depending on the stage.
     *
     * @param widget The widget to push
     */
    protected abstract void pushWidget(Widget widget);

    /**
     * Retrieves a widget so that it can be worked on. If this cannot retrieve a widget then the stage is considered starved. If a widget
     * is being worked on or the stage is blocked then this will do nothing. When a widget is pulled the stage will now be "working" and
     * will add a job to the event simulation to symbolise the time being spent.
     */
    public void pull() {
        if(getPrev()!=null && getPrev().isEmpty()) {
            //we are starved
            return;
        }

        if(currentWidget!=null) {
            //we are working or blocked, doesn't matter ignore pull
            return;
        }

        //get the widget being worked on
        Widget widget = pullWidget();
        widget.addStamp(this); //mark that the widget was created here for statistics
        //do math to work out when the next job is
        double time = line.getCurrentTime() + calculateTime();

        workTimeStamp = line.getCurrentTime();
        Job job = new Job(time,this);
        //add the job and mark the stage as worked
        line.addJob(job);
        currentWidget = widget;
        currentJob = job;
    }

    /**
     * Handles the logic for pulling a widget. This provides the actual method as to how to pull it and this will
     * vary depending on the type of stage.
     *
     * @return The retrieved widget.
     */
    protected abstract Widget pullWidget();

    /**
     * Calculates the amount of time it will take to complete a task. As this depends on a random
     * variable it will change each time. The N value will greatly determine the range of values. This
     * is a time delta, now the absolute completion time.
     *
     * @return The amount of time taken to complete a task.
     */
    private double calculateTime() {
        return M + N * (random.nextDouble() - 0.5);
    }

    /**
     * @return The proceeding interstage queue in the production line. This will be null if this is the final stage.
     */
    protected InterStageQueue getNext() {
        return next;
    }

    /**
     * @return The interstage queue before it in the production line. This will be null if this is the first stage
     */
    protected InterStageQueue getPrev() {
        return prev;
    }

    /**
     * Represents the stage as a string. This presents the string as
     *   - Stage[name='name', currentWidget='', blocked='blocked']
     *
     * @return The stage as a string
     */
    public String toString() {
        return "Stage[name="+getName()+", currentWidget="+currentWidget+", blocked="+blocked+"]";
    }

    /**
     * The total time the stage has spent blocked thus far in the simulation. The stage is considered blocked if it can not
     * work because the next interstage queue is full.
     *
     * @return The total time the stage has spent blocked thus far in the simulation.
     */
    public double getBlockedTime() {
        return blockedTime;
    }

    /**
     * The total time the stage has spent working thus far in the simulation. The stage is considered working if it
     * is actually producing items.
     *
     * @return The total time the stage has spent working thus far in the simulation.
     */
    public double getWorkTime() {
        return workTime;
    }

    /**
     * The total time the stage has spent starved. The stage is considered starved if it cannot produce items because there
     * is nothing to grab.
     *
     * @return The total starvation time in the simulation thus far.
     */
    public double getStarvationTime() {
        double realWorkTime = workTime;
        if(currentWidget!=null) //we are working still
            realWorkTime += (line.getCompletionTime() - workTimeStamp);
        return line.getCompletionTime() - realWorkTime - blockedTime;
    }


}
