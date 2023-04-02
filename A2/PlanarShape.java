/**
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 * A planar shape represents any shape which can be defined on a 2 dimensional cartesian plane. A planar shape must be
 * able to in some way define
 *    - its area
 *    - a distance from an extreme point to the origin.
 *
 * Two planar shapes will be considered equal if the distance between them is less than 0.005% of the area of the smaller polygon.
 */
public abstract class PlanarShape implements Comparable<PlanarShape> {

    private final static double EQUALS_DELTA = 0.0005;

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
    @Override
    public int compareTo(PlanarShape o) {
        double thisArea = getArea();
        double otherArea = o.getArea();

        //check if the diff of the two areas is less than 0.1% of the area of the minimum polygon
        if ((Math.abs(thisArea-otherArea) <= EQUALS_DELTA*Math.min(thisArea,otherArea)))
            return Double.compare(this.originDistance(), o.originDistance());

        return Double.compare(thisArea,otherArea); //if this area is smaller than the other area then return true;
    }
    /**
     * Calculates and returns the area of the shape.
     *
     * The resultant area will be positive.
     *
     * @return The area of the polygon.
     */
    public abstract double getArea();

    /**
     * Converts the shape to a string as per the assignment specifications.
     *
     * @return a string representation of the shape
     */
    public abstract String toString();

    /**
     * Calculates the distance the shape is from the origin. This is not always an exact calculation and often
     * looks at which important or significant vertex is closest to the origin.
     *
     * @return The important distance to the origin.
     */
    public abstract double originDistance();
}
