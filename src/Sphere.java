import java.util.*;

public class Sphere {
    private final Points center;
    private final List<Points> vertices;
    private final double radius;

    public Sphere(Points center_, double radius_, int resolution) {
        center = center_;
        vertices = new ArrayList<>();
        radius = radius_;

        //Fibonacci sphere projection
        double phi = (1 + Math.sqrt(5))/2.0;
        double goldenAngle = 2.0 * (Math.PI / phi);

        for (int i = 0; i <  resolution; i++) {
            double y = 1.0 - (2.0 * (i + 0.5))/((double) resolution);
            double r = Math.sqrt((1 - y * y));
            double theta = i * goldenAngle;

            double x = radius * r * Math.cos(theta) + center.getCoordinates()[0];
            double z = radius * r * Math.sin(theta) + center.getCoordinates()[2];
            y= (y * radius) + center.getCoordinates()[1];

            vertices.add(new Points(x, y, z));
        }
    }

    public List<Points> getVertices() {
        return vertices;
    }

    public Points getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

}
