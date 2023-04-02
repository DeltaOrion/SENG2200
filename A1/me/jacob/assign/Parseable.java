package me.jacob.assign;

import java.io.IOException;

/**
 * File: Parseable.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 *
 * Represents any class that can be parsed by input and written to output.
 *   - The output does not need to be identical to the parsed input
 *   - The output does not need to be readable by the input method.
 *
 * Any class implementing this interface must include a static method which allows for the creation of it through
 * an input stream called parse. This method must throw a {@link InvalidParseException} if parsing through input fails at any point
 */
public interface Parseable {

    /**
     * Writes the object to output. This should represent the object as a string. The written object does not need to be readable
     * by the parse method.
     *
     * @param writer The writer to write the object to.
     * @throws IOException If an error occurs while writing the object at any point.
     */
    void write() throws IOException;
}
