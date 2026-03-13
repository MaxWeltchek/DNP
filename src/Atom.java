import java.awt.*;

public class Atom extends Sphere {
    private Color color;
    public Atom(Points center_, double radius_, int resolution, Color color) {
        super(center_, radius_, resolution);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
