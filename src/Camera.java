import java.security.Key;
import java.util.Arrays;

public class Camera {

    //holds only focal length and camera coordinates
    private static final int focalLength = 1700;
    private static final double[] coordinates = new double[] {0, 0, -30};
    private static final double[] rotation = new double[]{0, 0, Math.PI};

    public Camera() {
    }

    public static int getFocalLength() {
        return focalLength;
    }

    public static double[] getCoordinates() {
        return coordinates;
    }

    public static void moveInOrOut(int inOrOut) {
        if (inOrOut > 0) {
            coordinates[2] += 2;
        } else {
            coordinates[2] -= 2;
        }
    }
}
