/**
 * File: Warehouse.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: The final resting place for widgets. This class takes widgets that have completed their journey in the production line
 * and calculates statistics on them. The widgets are not actually stored in memory here but rather pass through methods that do statistical calculations
 * to get meaningful information
 */
public class Warehouse {

    private int totalInStorage;
    private int totalA;
    private int totalB;
    private int threeAFiveA;
    private int threeAFiveB;
    private int threeBFiveA;
    private int threeBFiveB;

    public void offer(Widget widget) {
        //just delete the widget to save memory
        totalInStorage++;
        getProductionPaths(widget);
    }

    /**
     * Calculates the following statistics from a widget
     *   - the amount of widgets created from s0a and s0b
     *   - the amount of widgets that passed the following production lines
     *       - s3a -> s5a
     *       - s3b -> s5a
     *       - s3b -> s5a
     *       - s3b -> s5b
     *
     *  Preconditions: The widget has passed through the {@link ProductionLine} in a running
     *  simulation.
     *
     * @param widget The widget that has gone through the production line
     */
    private void getProductionPaths(Widget widget) {
        boolean fiveA = false;
        boolean fiveB = false;
        boolean threeA = false;
        boolean threeB = false;

        for (Stage stage : widget.getStamps()) {

            //Get the creational Stages
            if (stage.getName().equals("S0a"))
                totalA++;

            if (stage.getName().equals("S0b"))
                totalB++;

            //work out which stages the widget went through in the 3a,3b,5a,5b chain
            if(stage.getName().equals("S3a"))
                threeA = true;

            if(stage.getName().equals("S3b"))
                threeB = true;

            if (stage.getName().equals("S5a"))
                fiveA = true;

            if(stage.getName().equals("S5b"))
                fiveB = true;
        }

        //add totals for the widgets that went down the paths.
        //get widgets that went down 3a and 5a
        if (threeA && fiveA)
            threeAFiveA++;

        //3a and 5b
        if (threeA && fiveB)
            threeAFiveB++;

        if (threeB && fiveA)
            threeBFiveA++;

        if (threeB && fiveB)
            threeBFiveB++;

    }


    /**
     * Precondition: The simulation has completed
     *
     * @return The amount of widgets that are in storage
     */
    public int totalInStorage() {
        return totalInStorage;
    }

    /**
     * Precondition: The simulation has completed
     *
     * @return The amount of widgets that are in the storage that were created at s0a
     */
    public int getTotalA() {
        return totalA;
    }

    /**
     * Precondition: The simulation has completed
     *
     * @return The amount of widgets that are in the storage that were created at s0b
     */
    public int getTotalB() {
        return totalB;
    }

    /**
     * Precondition: The simulation has completed
     *
     * @return The amount of widgets that passed through s3a and s5a
     */
    public int getThreeAFiveA() {
        return threeAFiveA;
    }

    /**
     * Precondition: The simulation has completed
     *
     * @return The amount of widgets that passed through s3a and s5b
     */
    public int getThreeAFiveB() {
        return threeAFiveB;
    }

    /**
     * Precondition: The simulation has completed
     *
     * @return The amount of widgets that passed through s3b and s5a
     */
    public int getThreeBFiveA() {
        return threeBFiveA;
    }

    /**
     * Precondition: The simulation has completed
     *
     * @return The amount of widgets that passed through s3b and s5b
     */
    public int getThreeBFiveB() {
        return threeBFiveB;
    }

    /**
     * Represents the warehouse in its current state as a string
     *
     * @return Represents the warehouse as a string
     */
    public String toString() {
        return "Warehouse[storage=" + totalInStorage() + "]";
    }
}
