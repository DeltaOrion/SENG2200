package me.jacob.assign;

/**
 * File: Point.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 * The following class represents any immutable 2 dimensional point on a cartesian plane. A point has a double precision x coordinate
 * and double precision y coordinate. The coordinate of a given access represents its position on its respective axis.
 *
 *
 * The origin is a special point which represents the center of the cartesian plane or 0,0
 */
public class Point {

    private final double x;
    private final double y;

    private final static Point ORIGIN = new Point(0,0);
    private final static String TO_STRING_DOUBLE_FORMAT = "%4.2f";

    /**
     * Constructs a new point instance.
     *
     * @param x The x coordinate, may be any double precision number
     * @param y The y coordinate, may be any double precision number
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the x coordinate of the Point. That is, its position on the x axis.
     *
     * @return The x coordinate of the point.
     */
    public double getX() {
        return x;
    }

    /**
     * Retrieves the y coordinate of the Point. That is, its position on the y axis.
     *
     * @return The y coordinate of the point.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the squared distance of this point from another point on the cartesian plane.
     *
     * @param point The other non null point. This may not be null
     * @return The squared distance of this point from another point on the cartesian plane.
     */
    private double distanceSquared(Point point) {
        return Math.pow((point.x - this.x),2) + Math.pow((point.y - this.y),2);
    }

    /**
     * Returns the distance of this point from another point on the cartesian plane.
     *
     * @param point The other point. This may not be null
     * @return The distance of this point from another point on the cartesian plane.
     */
    public double distance(Point point) {
        return Math.sqrt(distanceSquared(point));
    }

    /**
     * Returns the distance of this point lies from the {@link #ORIGIN} point, that is the center of the
     * cartesian plane.
     *
     * @return The distance of this point from the origin.
     */
    public double distanceFromOrigin() {
        return distance(ORIGIN);
    }

    /**
     * Converts this point into a string representation in the format. This may not return null
     *
     * (x , y). Each double precision coordinate should be in the form {@link #TO_STRING_DOUBLE_FORMAT}
     *
     * @return A string representation of this point.
     */
    @Override
    public String toString() {
        return "(" +
                formatCoordinate(x) +
                " " +
                "," +
                " " +
                formatCoordinate(y) +
                ")";
    }

    /**
     * Helper function which takes a coordinate, and formats it according to the specifications. The coordinate may be
     * any double value. This may not return null
     *
     * @param coordinate The coordinate
     * @return A formatted string of the coordinate
     */
    private String formatCoordinate(double coordinate) {
        return String.format(TO_STRING_DOUBLE_FORMAT,coordinate);
    }

    /**
     * Checks whether two point are equal. This will return false if the object is not a point.
     * A point is equal if and only if both if its x coordinates and y coordinates are exactly equal.
     *
     * @param o The other object to compare with.
     * @return Whether this point is equal to the other object.
     */
    @Override
    public boolean equals(final Object o) {
        if(!(o instanceof Point))
            return false;

        final Point point = (Point) o;
        return Double.compare(point.x,this.x) == 0 && Double.compare(point.y,this.y) == 0;
    }


}
