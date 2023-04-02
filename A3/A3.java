/**
 * File: A3.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Main Driver Class
 */
public class A3 {

    public static void main(String[] args) {
        int M,N,qMax;
        try {
            //check user provided command args if not send them a quick message
            M = getArgAsInt(args,0,"Did not provide a value of M");
            N = getArgAsInt(args,1,"Did not provide a value of N");
            qMax = getArgAsInt(args,2,"Did not provide a value of qMax");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        //create production line and run simulation
        ProductionLine line = new ProductionLine();
        line.buildProductionLine(M,N,qMax);
        line.run();
        line.printStatistics();
    }

    /**
     * Returns a string argument from the args list as an integer
     *
     * @param args the list of strings
     * @param index the index to retrieve from
     * @param err the error message to display
     * @return The argument as an integer
     * @throws IllegalArgumentException if the argument does not exist or cannot be converted to a number.
     */
    private static int getArgAsInt(String[] args, int index, String err) {
        if(args.length<=index)
            throw new IllegalArgumentException(err);

        try {
            return Integer.parseInt(args[index]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
