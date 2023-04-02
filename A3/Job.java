/**
 * File: Job.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Represents a task that needs to be completed by a stage at some time in a discrete
 * event simulation. This will run the push() method for that stage.
 */
public class Job implements Comparable<Job> {

    private final double completionTime;
    private final Stage stage;

    /**
     * Creates a new job
     *
     * @param completionTime When the job is expected to be completed
     * @param stage The stage for which this job is completing a task for. This must not be null.
     */
    public Job(double completionTime, Stage stage) {
        this.completionTime = completionTime;
        this.stage = stage;
    }

    /**
     * Compares this to another job. The job which completes first comes before the one
     * that completes later. Thus this will compare the completion time of the two jobs.
     *
     * @param o The job to compare to
     * @return 1 if this job has a completion time smaller than o, 0 if this job has the same completion time as o, and -1 if this job has a greater completion time than o.
     */
    @Override
    public int compareTo(Job o) {
        return Double.compare(this.completionTime, o.completionTime);
    }

    /**
     * @return When this job completes
     */
    public double getCompletionTime() {
        return completionTime;
    }

    /**
     * Completes the task of the job. This should only be used as a part of the
     * discrete event simulation.
     */
    public void run() {
        stage.push();
    }
}
