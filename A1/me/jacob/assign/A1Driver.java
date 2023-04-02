package me.jacob.assign;

import java.io.IOException;
import java.io.InputStream;

/**
 * File: A1Driver.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Runs the logic for A1
 */
public class A1Driver implements Parseable {

    private MyPolygons polygons;
    private MyPolygons sorted;

    private A1Driver(MyPolygons polygons, MyPolygons sorted) {
        this.polygons = polygons;
        this.sorted = sorted;
    }

    /**
     * Writes the polygons in the format
     * unsorted list
     * ${unsorted list}
     *
     * sorted list
     * ${sorted list}
     *
     * The writer will not be closed
     *
     * @throws IOException if an error occurs while writing
     */
    public void write() throws IOException {
        //output to the writer
        System.out.print("Unsorted list");
        System.out.print(System.lineSeparator());
        polygons.write();
        System.out.print(System.lineSeparator());
        System.out.print("Sorted list");
        System.out.print(System.lineSeparator());
        sorted.write();
    }
    /**
     * Main Driver Logic
     *    - parses the input stream into a {@link MyPolygons}
     *    - creates a second sorted {@link MyPolygons}
     *
     * The Input Stream will not be closed
     *
     * @param input the input stream containing.
     * @throws InvalidParseException if the contents of the input stream cannot be parsed to a {@link MyPolygons}
     */
    public static A1Driver parse(InputStream input) throws InvalidParseException {
        MyPolygons polygons = MyPolygons.parse(input);
        MyPolygons sorted = new MyPolygons();
        //insert all of the polygons into the sorted list
        polygons.resetCurrent();
        while(polygons.canStep()) {
            sorted.appendInOrder(polygons.step());
        }
        return new A1Driver(polygons,sorted);
    }
}
