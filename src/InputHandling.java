import java.awt.event.*;

public class InputHandling implements MouseListener, MouseMotionListener, KeyListener {
    private int[] lastMouse = new int[2];
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMouse = new int[]{e.getX(), e.getY()};
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Main.rotation[0] += (lastMouse[0] - e.getX())/100.0;
        Main.rotation[1] += (lastMouse[1] - e.getY())/100.0;
        Main.rotation[1] = Math.max(-Math.PI/2.0,(Math.min(Main.rotation[1], Math.PI/2.0)));
        lastMouse = new int[]{e.getX(), e.getY()};
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == '-') {
            Camera.moveInOrOut(-1);
            Main.calculateScale();
        } else if (e.getKeyChar() == '=') {
            Camera.moveInOrOut(1);
            Main.calculateScale();
        } else if (e.getKeyChar() == 'g') {
            Main.rotation = new double[]{0,0,0};
            Camera.resetLocation();
            Main.calculateScale();
        } else if (e.getKeyChar() == 'r') {
            Main.rotation[2] += Math.PI/2.0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
