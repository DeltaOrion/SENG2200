/**
 * File: IdGenerator.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description: Singleton class that provides unique ids for {@link Widget}. Each id that is generated is a number. The first generated
 * will be 0, then second 1, etc etc.
 */
public class IdGenerator {

    private int counter;
    private static IdGenerator instance = null;

    public IdGenerator() {
        this.counter = 0;
    }

    /**
     * @return The singleton instance. If the singleton instance does exist it will be created.
     */
    public static IdGenerator instance() {
        //make the instance if it does not exist
        if(instance==null)
            instance = new IdGenerator();

        return instance;
    }

    /**
     * Returns a new Id for a widget. Id's are generated as ordinal numbers from a counter. The first number generated
     * will be 0, the next 1, the next 2 etc etc.
     *
     * @return The A new ID for a widget.
     */
    public String getId() {
        return String.valueOf(counter++);
    }
}
