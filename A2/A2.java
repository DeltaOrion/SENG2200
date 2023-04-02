import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

/**
 * File: A2.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Main Driver Class
 */
public class A2 {

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

    private final static A2 main = new A2();
    private static String[] args;
    private LinkedList<PlanarShape> unsorted;
    private LinkedList<PlanarShape> sorted;

    public static void main(String[] args) {
        //store args for later user in the program
        A2.args = args;
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
     *    - Read and parse the contents of the file using {@link #parse(InputStream)}
     *    - Write the contents in a formatted way using {@link #write()}
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

            parse(inputStream);
            write();
        } catch (FileNotFoundException e) {
            System.err.println("Could not find the file at the specified path '"+userSuppliedPath+"'");
        } catch (IllegalArgumentException e) {
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

    /**
     * Writes the polygons in the format
     * unsorted list
     * ${unsorted list}
     *
     * sorted list
     * ${sorted list}
     */
    public void write()  {
        System.out.print("Unsorted list");
        System.out.print(System.lineSeparator());
        writeList(unsorted);
        System.out.print("Sorted list");
        System.out.print(System.lineSeparator());
        writeList(sorted);
    }

    /**
     * Writes a list by displaying each line sequentially.
     *
     * @param shapes The list of shapes to write.
     */
    private void writeList(LinkedList<PlanarShape> shapes) {
        Iterator<PlanarShape> iterator = shapes.iterator();
        while(iterator.hasNext()) {
            PlanarShape shape = iterator.next();
            System.out.print(shape.toString());
            System.out.print(System.lineSeparator());
        }
    }
    /**
     * Main Driver Logic
     *    - parses the input stream into a {@link LinkedList}
     *    - creates a second sorted {@link LinkedList}
     *
     * The Input Stream will not be closed
     *
     * @param input the input stream containing.
     * @throws IllegalArgumentException if the contents of the input stream cannot be parsed to a {@link LinkedList}
     */
    public void parse(InputStream input) {

        LinkedList<PlanarShape> unsorted = new LinkedList<>();
        SortedLinkedList<PlanarShape> sorted = new SortedLinkedList<>();

        Scanner scanner = new Scanner(input);
        boolean foundToken = false;
        char token = ' ';
        double[] nums = null;
        int count = 0;
        //loop through finding each non whitespace token
        while(scanner.hasNext()) {
            if(!foundToken) {
                //if we have yet to find a token get the next token
                token = scanner.next().charAt(0);
                foundToken = true;
                continue;
            }

            if(nums==null) {
                //if the nums array has not be established then make it depending on the token
                switch (String.valueOf(token).toUpperCase(Locale.ROOT)) {
                    case "P":
                        nums = new double[scanner.nextInt()*2];
                        break;
                    case "C":
                        nums = new double[3];
                        break;
                    case "S":
                        nums = new double[4];
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown Token "+token);
                }
            }
            //now that the tokens have been found look for the next double and append it ot the array
            if(scanner.hasNextDouble()) {
                nums[count] = scanner.nextDouble();
                count++;
            } else {
                //if it is not a double then we have found the next token so we can finish this shape off and make the next
                unsorted.append(shapeFactory(token,nums));
                nums = null;
                foundToken = false;
                count = 0;
            }
        }

        if(nums!=null) {
            unsorted.append(shapeFactory(token,nums));
        }

        for(PlanarShape shape : unsorted) {
            sorted.insertInOrder(shape);
        }

        scanner.close();

        this.unsorted = unsorted;
        this.sorted = sorted;
    }

    /**
     * Factory method to retrieve a planar shape given an array of nums and the specified token.
     *
     * Token
     *   C: circle         nums must have a length of 3
     *   S: semi-circle    nums must have a length of 4
     *   P: polygon        nums must have a length of 2n
     *
     * By the way, the thing with me not treating it as sunny day data is more for my benefit while im coding
     * so I can clearly identify errors while testing.
     *
     * @param token The token to identify which planar shape should be parsed.
     * @param nums the doubles that should be parsed
     * @return The planar shape from the values.
     * @throws IllegalArgumentException If there is an unknown token or the nums does not conform to the token size definition
     */
    private static PlanarShape shapeFactory(char token, double[] nums) {
        //search through token
        switch (String.valueOf(token).toUpperCase(Locale.ROOT)) {
            case "P":
                return getPolygon(nums);
            case "C":
                return getCircle(nums);
            case "S":
                return getSemiCircle(nums);
        }
        throw new IllegalArgumentException("Token not found!");
    }

    /**
     * Creates a semi-circle from the input definition
     *
     * x0, y0 x1, y1 where P0 is the circle base and P1 is the perpendicular
     *
     * @param nums an array of 4 numbers, x0, y0, x1, y1
     * @return A circle from the definition
     * @throws IllegalArgumentException If the array is not 4 numbers
     */
    private static PlanarShape getSemiCircle(double[] nums) {
        if(nums.length!=4)
            throw new IllegalArgumentException("More than 4 nums for a semi-circle definition");

        Point center = new Point(nums[0],nums[1]);
        Point perpendicular = new Point(nums[2],nums[3]);
        return new SemiCircle(center,perpendicular);
    }

    /**
     * Creates a circle from the input definition
     *
     * C x0 y0
     *
     * @param nums an array of 3 numbers, C, x0 and y0
     * @return A circle from the definition
     * @throws IllegalArgumentException If the array is not 3 numbers
     */
    private static PlanarShape getCircle(double[] nums) {
        if(nums.length!=3)
            throw new IllegalArgumentException("More than 3 nums for a circle definition");

        Point center = new Point(nums[0],nums[1]);
        return new Circle(center,nums[2]);
    }

    /**
     * Creates a polygon from the input definition
     *
     * P x0, y0, x1, y1, ....
     *
     * @param nums an array of n numbers, x0, y0, x1, y1
     * @return A circle from the definition
     * @throws IllegalArgumentException If the array is not 2n numbers
     */
    private static PlanarShape getPolygon(double[] nums) throws IllegalArgumentException {
        if(nums.length%2!=0)
            throw new IllegalArgumentException("Not an even amount of numbers for the polygon! '"+nums.length+"'");
        Point[] points = new Point[nums.length/2];
        //for every 2 numbers add the respective point.
        for(int i=0;i<nums.length;i+=2) {
            points[i/2] = new Point(nums[i],nums[i+1]);
        }
        return new Polygon(points);
    }
}
