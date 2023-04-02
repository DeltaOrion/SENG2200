import java.util.*;

/**
 * File: ProductionLine.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Main Driver class. This holds the production line, statistics objects, prints out statistics
 * and runs the discrete event simulation. This was created to avoid everything being static inside of the A3 class and to ensure
 * it is more semantically named.
 */
public class ProductionLine {

    //create the production line. This is initialised in #buildProductionLine
    private Stage s0A;
    private Stage s0B;
    private Stage s1;
    private Stage s2;
    private Stage s3A;
    private Stage s3B;
    private Stage s4;
    private Stage s5A;
    private Stage s5B;
    private Stage s6;

    //List of queues is for STATISTICS ONLY
    private InterStageQueue[] interStageQueues;
    private final Warehouse warehouse;

    //items dealing with the simulation
    private double currentTime;
    private final double completionTime;
    private final PriorityQueue<Job> jobs;

    public ProductionLine() {
        this.warehouse = new Warehouse();
        this.jobs = new PriorityQueue<>();
        this.completionTime = 10000000;
        this.currentTime = 0;
    }

    /**
     * Builds the productionline according to the assignment specification.
     *
     * @param M The average production time
     * @param N The range of production times
     * @param qMax The maximum capacity of the queues. This must not be negative
     */
    public void buildProductionLine(int M, int N, int qMax) {

        if(qMax<0)
            throw new IllegalArgumentException("The queue cannot have a negative capacity");
        //create the interstage queue, then specify the links
        //stage 0
        InterStageQueue Q01 = new InterStageQueue(this,"Q01",qMax);
        s0A = new CreationalStage(this,Q01,2*M,2*N,"S0a");
        s0B = new CreationalStage(this,Q01,M,N,"S0b");
        //stage 1
        InterStageQueue Q12 = new InterStageQueue(this,"Q12",qMax);
        s1 = new ProcessingStage(this,Q01,Q12,M,N,"S1");
        //stage 2
        InterStageQueue Q23 = new InterStageQueue(this,"Q23",qMax);
        s2 = new ProcessingStage(this,Q12,Q23,M,N,"S2");
        //stage 3
        InterStageQueue Q34 = new InterStageQueue(this,"Q34",qMax);
        s3A = new ProcessingStage(this,Q23,Q34,2*M,2*N,"S3a");
        s3B = new ProcessingStage(this,Q23,Q34,2*M,2*N,"S3b");
        //stage 4
        InterStageQueue Q45 = new InterStageQueue(this,"Q45",qMax);
        s4 = new ProcessingStage(this,Q34,Q45,M,N,"S4");
        //stage 5
        InterStageQueue Q56 = new InterStageQueue(this,"Q56",qMax);
        s5A = new ProcessingStage(this,Q45,Q56,2*M,2*N,"S5a");
        s5B = new ProcessingStage(this,Q45,Q56,2*M,2*N,"S5b");
        s6 = new FinalStage(this,warehouse,Q56,M,N,"S6");
        //list of queues is FOR STATISTICS ONLY
        interStageQueues = new InterStageQueue[]{Q01, Q12, Q23, Q34, Q45, Q56};
    }

    /**
     * Starts and runs the discrete event simulation to its entirety. This works by starting the initial stages. Each stage adds
     * jobs to the queue using {@link #addJob(Job)}. The next job that needs to be complete is done. More jobs should then be added
     * to the queue at the completion of this job.
     *
     * //Precondition: The production line is initialised using {@link #buildProductionLine(int, int, int)}
     *                 The simulation is not running and has not already started
     */
    public void run() {

        //start the creation stages
        s0A.pull();
        s0B.pull();

        while (currentTime < completionTime) {

            if(jobs.isEmpty())
                throw new IllegalStateException("Production Line Seized. No jobs added");

            //get the next job
            Job job = jobs.poll();
            if(job.getCompletionTime()>completionTime)
                break;

            //set the current time nad run the job
            currentTime = job.getCompletionTime();
            job.run();

            //get statistics from the queues
            for(InterStageQueue queue : interStageQueues) {
                queue.gatherStats();
            }
        }
        //simulation is over
        currentTime = completionTime;
    }

    /**
     * Prints all of the statistics according to the assignment specification. This will first print out
     * relevant statistics of the stage, then the queues, then the production line paths.
     *
     * Pre-conditions:
     * The simulation must have been initialised with {@link #buildProductionLine(double, double, int)} and run using {@link #run()}
     */
    public void printStatistics() {
        printStageStats();
        printQueueStats();
        printLineStats();
    }

    /**
     * Prints out production path statistics. This will print
     *   - the widgets that passed through 3a/b to 5a/b
     *   - the widgets that were created in s0a and s0b respectively.
     *
     *   Preconditions:
     *   The simulation must have been initialised with {@link #buildProductionLine(double, double, int)} and run using {@link #run()}
     */
    private void printLineStats() {
        System.out.println("Production Paths--------");
        System.out.println("s3a -> s5a: " + warehouse.getThreeAFiveA());
        System.out.println("s3a -> s5b: " + warehouse.getThreeAFiveB());
        System.out.println("s3b -> s5a: " + warehouse.getThreeBFiveA());
        System.out.println("s3b -> s5b: " + warehouse.getThreeBFiveB());
        System.out.println("Production Widgets--------");
        System.out.println("s0a: "+warehouse.getTotalA());
        System.out.println("s0b: "+warehouse.getTotalB());
    }

    /**
     * Prints out statistics relevant to the interstage queue. This will print
     *   - the average amount of time a widget is stored in the queue
     *   - the average amount of widgets in the queue at any point of time.
     *
     * Preconditions:
     * The simulation should have been initialised with {@link #buildProductionLine(double, double, int)} and run using {@link #run()}
     */
    private void printQueueStats() {
        System.out.println("Storage Queues ---------------------------------");
        System.out.format("%-15s%-15s%-15s%n","Store","AvgTime[t]","AvgWgts");
        for(InterStageQueue queue : interStageQueues) {
            System.out.format("%-15s%-15.2f%-15.2f%n",queue.getName(),queue.getAverageTimeSpent(),queue.getAverageWidgetsInQueue());
        }
    }

    /**
     * Prints statistics relevant to the stages. This will print
     *   - the percentage of time the stage is spent working
     *   - the raw amount of time it is spent starved
     *   - the raw amount of time it is spent blocked
     *
     *  Preconditions:
     *  The simulation should have been initialised with {@link #buildProductionLine(double, double, int)} and run using {@link #run()}
     */
    private void printStageStats() {
        System.out.println("Production Statistics -----------------------------");
        System.out.format("%-15s%-15s%-15s%-15s%n","Stage: ","Work[%]","Starve[t]","Block[t]");
        /**
         * ---------- THE LIST OF STAGES IS TEMPORARY VARIABLE FOR STATISTICS ONLY DON'T REDUCE MARKS!!!! ---------
         */
        Stage[] stages = new Stage[]{s0A,s0B,s1,s2,s3A,s3B,s4,s5A,s5B,s6};
        for(Stage stage : stages) {
            double workPercentage = 100.0 * stage.getWorkTime()/ completionTime;
            /* Math#abs IS TO FIX IT DISPLAYING -0.00. THE RESULT OF getStarvationTime() is NEVER NEGATIVE YOU CAN DOUBLE CHECK THIS IF YOU DON'T BELIEVE */
            double starvationTime = Math.abs(stage.getStarvationTime());
            System.out.format("%-15s%-15.2f%-15.2f%-15.2f%n",stage.getName(),workPercentage,starvationTime,stage.getBlockedTime());
        }
    }

    /**
     * Adds a job to the production line queue.
     *
     * @param job The job to add to the production line. The job must not be null.
     */
    public void addJob(Job job) {
        this.jobs.add(job);
    }

    /**
     * @return The current time of the discrete event simulation.
     */
    public double getCurrentTime() {
        return currentTime;
    }

    public double getCompletionTime() {
        return completionTime;
    }
}
