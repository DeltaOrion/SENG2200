/**
 * File: MoreObjects.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 * Functions to assist object manipulation.
 */
public class MoreObjects {

    /**
     * Compares two nullable objects to check for equality
     *
     * @param a the first object
     * @param b the second object
     * @return whether they are equal.
     */
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Throws a null pointer exception if the object is null with a default message.
     *
     * @param object any nullable object
     */
    public static void requiresNonNull(Object object) {
        requiresNonNull(object,"Non null objects are not permitted");
    }

    /**
     * Throws a null pointer if the object is null with the specified message.
     *
     * @param object Any nullable object to check
     * @param message The message of the exception.
     */
    public static void  requiresNonNull(Object object, String message) {
        if(object==null)
            throw new NullPointerException(message);
    }
}
