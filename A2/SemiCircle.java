/**
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 * This class represents a 2 dimensional semi circle on the cartesian plane. A semi-circle represents one half of a circle that is bisected
 * by a line L which passes through the center.
 *
 * The line which is perpendicular to L and intersects the center of the circle is called M. Given a circle C, a semi-circle is defined by two points, one being the centre
 * of the original circle and the other being the point in where M touches the side of the circle.
 */
public class SemiCircle extends PlanarShape {

    private final Point centerBase;
    private final Point perpendicular;

    private final static String TO_STRING_DOUBLE_FORMAT = "%5.2f";

    public SemiCircle(Point centerBase, Point perpendicularBase) {
        this.centerBase = centerBase;
        this.perpendicular = perpendicularBase;
    }

    /**
     * Creates an upright semicircle that would be represented by y=sqrt(1-x^2)
     */
    public SemiCircle() {
        this(new Point(),new Point(0,1));
    }

    /**
     * A semi-circle is half a circle and thus the area is half that of the area of a circle
     * which is pi*r^2 /2
     *
     * @return The semi-circle area
     */
    @Override
    public double getArea() {
        return Math.PI*getRadius()*getRadius()/2.0f;
    }

    /**
     * Calculates and returns the radius of a semi-circle. This is taken by getting the distance from the
     * perpendicular to the base.
     *
     * @return The radius of the semi-circle
     */
    private double getRadius() {
        return perpendicular.distance(centerBase);
    }

    /**
     * Converts the semi-circle to a string in the
     *
     * "SEMI=[CenterPerpendicular]: area"
     */
    @Override
    public String toString() {
        return "SEMI=[" +
                centerBase +
                perpendicular +
                "]: "
                +formatArea(getArea());
    }

    /**
     * Calculates the distance to the origin by taking all the extreme points of the semicircle and
     * finding out which is closest to the origin.
     *
     * @return The closest extreme point to the origin
     */
    @Override
    public double originDistance() {
        //calculate x and y of unknown extreme points, that being the points on the two extreme ends of the base
        double x2 = centerBase.getX() - Math.abs(centerBase.getY() - perpendicular.getY());
        double y2 = centerBase.getY() + Math.abs(centerBase.getX() - perpendicular.getX());
        double x3 = centerBase.getX() + Math.abs(centerBase.getY()-perpendicular.getY());
        double y3 = centerBase.getY() - Math.abs(centerBase.getX()-perpendicular.getX());
        //convert x and ys to a point
        Point p2 = new Point(x2,y2);
        Point p3 = new Point(x3,y3);

        //put all the extreme points include the base and the perpendicular into an array
        Point[] extremePoints = {centerBase,perpendicular,p2,p3};
        double lowestDistance = Double.MAX_VALUE;

        for(Point point : extremePoints) {
            //iterate through all points to find the minima
            double originDist = point.distanceFromOrigin();
            if(originDist<=lowestDistance) {
                lowestDistance = originDist;
            }
        }
        return lowestDistance;
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
}
