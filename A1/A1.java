import me.jacob.assign.A1Driver;
import me.jacob.assign.InvalidParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * File: A1.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Main Driver Class
 */
public class A1 {

    /**
     * H e l l o     M a r k e r
     *
     * IMPORTANT
     *
     * Documentation is laid out as follows. Some preconditions and post-conditions such as whether the output of a function
     * ----------------------------------
     * Post-Condition/What the method does
     *
     * @param variable what it is and pre-conditions
     * @param variable2 what it is and pre-conditions
     * @throws Exception pre-conditions for this exception to be thrown
     * public void example() {
     *     //do things
     * }
     * ----------------------------------
     */

    private final static A1 main = new A1();
    private static String[] args;

    public static void main(String[] args) {
        //store args for later user in the program
        A1.args = args;
        //move to a non-static environment
        main.run();
    }

    /**
     * Logic for running the main class
     */
    public void run() {
        runUserInput();
    }

    /**
     *  Function does the following
     *    - Take the user inputted file
     *    - Run {@link A1Driver} on the file if it exists to {@link System#out}
     */
    private void runUserInput() {
        if(args.length==0) {
            System.err.println("No file location supplied!");
            return;
        }

        String userSuppliedPath = args[0];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(userSuppliedPath);

            A1Driver driver = A1Driver.parse(inputStream);
            driver.write();

        } catch (FileNotFoundException e) {
            System.err.println("Could not find the file at the specified path '"+userSuppliedPath+"'");
        } catch (IOException | InvalidParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream!=null)
                    inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
