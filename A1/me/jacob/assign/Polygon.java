package me.jacob.assign;

import me.jacob.assign.util.MathUtil;
import me.jacob.assign.util.MoreObjects;

/**
 * File: Polygon.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 * The following class represents an Immutable 2 Dimensional Mathematical Irregular Cartesian Polygon. A Polygon is a shape consisting of n sides which is defined
 * by n-1 cartesian coordinates. The order of the points is important for a polygon as it is drawn from one point to the next. For any irregular and
 * filled polygon, that being a polygon that does not have diagonals in it the points should be listed in clockwise or counter clockwise order fixed
 * about the origin.
 */
public class Polygon implements ComparePoly {

    //may not be null
    private final Point[] points;
    private final static double EQUALS_DELTA = 0.001;
    private final static String TO_STRING_DOUBLE_FORMAT = "%6.2f";

    /**
     * Creates a new Polygon using the builder. The created Polygon must meet the following PostConditions
     *   - All Points are ordered correctly
     *   - No points are null
     *
     * @param points the points in the polygon. No point can be null and all points should be ordered properly. This parameter may not be null
     */
    public Polygon(Point[] points) {
        //Using the private variables of an inner class is NOT a violation of encapsulation as the inner class is
        //apart of the capsule. All aspects of the implementation of the Polygon remain hidden from the outside world
        //including its creation
        for(Point point : points) {
            MoreObjects.requiresNonNull(point);
        }
        this.points = points;
    }

    /**
     * Calculates the area of this Polygon using the Shoelace Formula.
     *   -  https://www.wikiwand.com/en/Shoelace_formula
     *
     * The resultant area will be positive. The order of the Points in the polygon
     * will affect the result.
     *
     * @return The area of the polygon.
     */
    public double getArea() {
        return 0.5f * MathUtil.abs(getAreaSum());
    }

    /**
     * Helper function which returns a positive or negative Shoelace Sum.
     *
     * @return The ShoeLace sum of the Polygon.
     */
    private double getAreaSum() {
        double sum = 0;
        for(int i=0;i<=points.length-1;i++) {
            final Point point = points[i];
            final Point pointNext = i+1 == points.length ? points[0] : points[i+1];
            sum += (point.getX() * pointNext.getY()) - (point.getY()*pointNext.getX());
        }
        return sum;
    }

    /**
     * Returns the lowest minimum vertex distance to the origin. This takes the vertex which has the closest distance
     * to the origin, and then returns that distance.
     *
     * Distances must be positive
     *
     * @return The lowest minimum vertex distance to the origin.
     */
    public double distanceOfClosestPointToOrigin() {
        double closestDistance = Double.MAX_VALUE;
        for (Point point : points) {
            double distance = point.distanceFromOrigin();
            if (distance < closestDistance) {
                closestDistance = distance;
            }
        }
        return closestDistance;
    }

    /**
     * Documented in {@link ComparePoly} interface
     */
    @Override
    public boolean ComesBefore(Object o) {
        if(!(o instanceof Polygon))
            return false;

        Polygon polygon = (Polygon) o;
        double thisArea = getArea();
        double otherArea = polygon.getArea();

        //check if the diff of the two areas is less than 0.1% of the area of the minimum polygon
        if ((MathUtil.abs(thisArea-otherArea) <= EQUALS_DELTA*Math.min(thisArea,otherArea)))
            return Double.compare(this.distanceOfClosestPointToOrigin(), polygon.distanceOfClosestPointToOrigin()) < 0;

        return Double.compare(thisArea,otherArea) < 0; //if this area is smaller than the other area then return true;
    }

    /**
     * Returns a string in the format [point1point2point3]: area
     * Returned output cannot be null
     *
     * Area will be in {@link #TO_STRING_DOUBLE_FORMAT}
     */
    @Override
    public String toString() {
        String builder = "[";
        for(Point point : points) {
            builder = builder + point;
        }
        builder = builder + "]: ";
        builder = builder + formatArea(getArea());
        return builder;
    }

    /**
     * Helper function which takes the area, and formats it according to the specifications. The area may be
     * any double value. Returned output cannot be null
     *
     * @param area The area of the polygon
     * @return A formatted string of the area.
     */
    private String formatArea(double area) {
        return String.format(TO_STRING_DOUBLE_FORMAT,area);
    }

    /**
     * Checks if two polygons are equal. Polygons are only equal if they have the same points in the same order.
     *
     * @param o any nullable object
     * @return whether the polygons are equal.
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Polygon))
            return false;

        Polygon polygon = (Polygon) o;
        if(polygon.points.length != this.points.length)
            return false;

        for(int i=0;i<points.length;i++) {
            if(!points[i].equals(polygon.points[i]))
                return false;
        }

        return true;
    }
}
