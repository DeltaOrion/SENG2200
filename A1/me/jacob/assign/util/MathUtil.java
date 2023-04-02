package me.jacob.assign.util;

/**
 * File: MathUtil.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 * Provide Math functions
 * */
public class MathUtil {

    /**
     * Returns the absolute value of a double x. If x < 0 then this will return the positive equivalent
     * of x.
     *
     * @param x The number to get the absolute value of
     * @return The absolute value of x.
     */
    public static double abs(double x) {
        return (x <= 0.0D) ? 0.0D - x : x;
    }
}
