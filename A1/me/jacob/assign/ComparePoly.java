package me.jacob.assign;

/**
 * File: ComparePoly.java
 *
 * Author: SENG2200 Staff
 * Course: SENG2200
 * Program Description:
 * Represents a polygon that can be compared. 
 */
public interface ComparePoly {

    /**
     * Implemented by a polygon. If the entered object is not a polygon this will return false.
     * If the entered object is a polygon then
     *    Returns true if o has a smaller area than this polygon
     *    Returns false if o has a larger area than this polygon
     *    If both polygons have an equal area, as defined by if the difference
     *    in area's is less than 0.1% that of the smaller area then it will
     *    return false if this polygon has a lower minimum vertex distance and true if otherwise
     *
     * @param o The other polygon
     * @return whether o should sit before this one when comparing polygons
     */
    boolean ComesBefore(Object o); // true if this < param
}