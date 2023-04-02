/**
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 *
 * This class represents a 2 dimensional circle on the Cartesian plane. A circle is a shape in
 * which all points are equidistant from a central point. This distance is called the radius and the central
 * point is called the centre.
 * The circle is defined by the centre or x0, y0 and a radius.
 */
public class Circle extends PlanarShape {

    private final Point center;
    private final double r;

    private final static String TO_STRING_DOUBLE_FORMAT_AREA = "%5.2f";
    private final static String TO_STRING_DOUBLE_FORMAT_RADIUS = "%3.2f";

    /**
     * Constructs a new circle with a center c and a radius r.
     *
     * @param center the center of the circle
     * @param r the radius of the circle
     */
    public Circle(Point center, double r) {
        this.center = center;
        this.r = r;
    }

    /**
     * Constructs a unit circle with a radius of 1 and center at 0,0
     */
    public Circle() {
        this(new Point(),1);
    }

    //use PI*r^2 to calculate area
    @Override
    public double getArea() {
        return Math.PI*r*r;
    }

    /**
     * String in format CIRC=[point0 r]: area_value.
     * The radius is in format 3.2f and the area is in format 5.2f
     *
     * @return a formatted string according to the above specification
     */
    @Override
    public String toString() {
        return "CIRC=[" +
                center +
                " " +
                formatRadius(r) + "]: "
                + formatArea(getArea());
    }

    /**
     * Helper function which takes the area, and formats it according to the specifications. The area may be
     * any double value. Returned output cannot be null
     *
     * @param area The area of the polygon
     * @return A formatted string of the area.
     */
    private String formatArea(double area) {
        return String.format(TO_STRING_DOUBLE_FORMAT_AREA,area);
    }
    /**
     * Helper function which takes the radius, and formats it according to the specifications. The area may be
     * any double value. Returned output cannot be null
     *
     * @param radius The radius
     * @return A formatted string of the area.
     */
    private String formatRadius(double radius) {
        return String.format(TO_STRING_DOUBLE_FORMAT_RADIUS,radius);
    }

    @Override
    public double originDistance() {
        return center.distanceFromOrigin()-r;
    }
}
