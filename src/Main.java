import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.concurrent.*;

public class Main {
    private static final Canvas canvas = new Canvas();
    public static BufferStrategy bs;
    public static BufferedImage baseState;
    public static double[] rotation = new double[3];
    public static final int resolution = 300;
    public static Atom[] molecule;
    public static Bond[] bonds;
    public static final int PPI = 254;
    public static int scale;

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("2,4 - Dinitrophenol");
        canvas.setSize(1000, 1000);
        canvas.setBackground(new Color(10, 10, 10));
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        baseState = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics baseStatePen = baseState.getGraphics();
        baseStatePen.setColor(new Color(80, 80, 80));
        baseStatePen.fillRect(0,0,1000,1000);
        baseStatePen.dispose();

        InputHandling inputListener = new InputHandling();
        canvas.addMouseListener(inputListener);
        canvas.addMouseMotionListener(inputListener);
        canvas.addKeyListener(inputListener);


        Thread.sleep(500);

        molecule = new Atom[] {
                new Atom(new Points( 0.000,  2.800, 0.0), 0.4,  resolution, Color.black),  //carbon 1
                new Atom(new Points( 2.425,  1.400, 0.0), 0.4,  resolution, Color.black),  //carbon 2
                new Atom(new Points( 2.425, -1.400, 0.0), 0.4,  resolution, Color.black),  //carbon 3
                new Atom(new Points( 0.000, -2.800, 0.0), 0.4,  resolution, Color.black),  //carbon 4
                new Atom(new Points(-2.425, -1.400, 0.0), 0.4,  resolution, Color.black),  //carbon 5
                new Atom(new Points(-2.425,  1.400, 0.0), 0.4,  resolution, Color.black),  //carbon 6

                new Atom(new Points( 0.000,  5.520, 0.0), 0.35, resolution, Color.RED),  //oxygen in hydroxyl group
                new Atom(new Points( 1.920,  7.440, 0.0), 0.2,  resolution/2, Color.WHITE),  //hydrogen in hydroxyl group

                new Atom(new Points( 4.971,  2.870, 0.0), 0.35, resolution, Color.blue),  //nitrogen in pos 2 nitro group
                new Atom(new Points( 4.971,  5.310, 0.0), 0.35, resolution, Color.RED),   //oxygen in pos 2 nitro group
                new Atom(new Points( 7.084,  1.650, 0.0), 0.35, resolution, Color.RED),   //oxygen in pos 2 nitro group

                new Atom(new Points( 0.000, -5.740, 0.0), 0.35, resolution, Color.blue),  //nitrogen in pos 4 nitro group
                new Atom(new Points( 2.113, -6.960, 0.0), 0.35, resolution, Color.RED),   //oxygen in pos 4 nitro group
                new Atom(new Points(-2.113, -6.960, 0.0), 0.35, resolution, Color.RED),   //oxygen in pos 4 nitro group

                new Atom(new Points( 4.295, -2.480, 0.0), 0.2,  resolution/2, Color.white),  //hydrogen bonded to carbon 3
                new Atom(new Points(-4.295, -2.480, 0.0), 0.2,  resolution/2, Color.white),  //hydrogen bonded to carbon 5
                new Atom(new Points(-4.295,  2.480, 0.0), 0.2,  resolution/2, Color.white),  //hydrogen bonded to carbon 6
        };

        bonds = new Bond[] {
                //carbon - carbon bonds
                new Bond(molecule[0], molecule[1], true),
                new Bond(molecule[1], molecule[2], false),
                new Bond(molecule[2], molecule[3], true),
                new Bond(molecule[3], molecule[4], false),
                new Bond(molecule[4], molecule[5], true),
                new Bond(molecule[5], molecule[0], false),

                //top carbon to hydroxyl group
                new Bond(molecule[0], molecule[6], false),
                new Bond(molecule[6], molecule[7], false),

                //carbon to nitro group at pos 2
                new Bond(molecule[1], molecule[8], false),
                new Bond(molecule[8], molecule[9], true),
                new Bond(molecule[8], molecule[10], false),

                //carbon to nitro group at pos 4
                new Bond(molecule[3], molecule[11], false),
                new Bond(molecule[11], molecule[12], true),
                new Bond(molecule[11], molecule[13], false),

                //carbons to hydrogen's
                new Bond(molecule[2], molecule[14], false),
                new Bond(molecule[4], molecule[15], false),
                new Bond(molecule[5], molecule[16], false)
        };

        System.out.print("\033[H\033[2J");
        System.out.println("--------------------------------------");
        System.out.println("Controls:");
        System.out.println("\"+\" & \"-\" to zoom in and out");
        System.out.println("\"g\" to reset camera");
        System.out.println("--------------------------------------");

        calculateScale();
        ScheduledExecutorService renderer = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> renderTask = renderer.scheduleAtFixedRate(Main::renderFrame, 0, 17, TimeUnit.MILLISECONDS);
        Thread.sleep(Integer.MAX_VALUE);
        renderTask.cancel(false);
        renderer.shutdown();
    }

    public static void renderFrame() {
        Graphics pen = bs.getDrawGraphics();
            clearScreen(pen);

            pen.setColor(Color.WHITE);
            for (Atom atom : molecule) {
                for (Points vertex : atom.getVertices()) {
                    Points temp = new Points(vertex);
                    temp.rotateZAxis(rotation[2], new double[]{0,0,0});
                    temp.rotateYAxis(rotation[0], new double[]{0,0,0});
                    temp.rotateXAxis(rotation[1], new double[]{0,0,0});
                    int[] coords = temp.castToXY();
                    pen.setColor(atom.getColor());
                    pen.drawOval(coords[0], coords[1], 1, 1);
                }
            }

            for (Bond bond : bonds) {
                Points startTemp = new Points(bond.getStart());
                startTemp.rotateZAxis(rotation[2], new double[]{0,0,0});
                startTemp.rotateYAxis(rotation[0], new double[]{0,0,0});
                startTemp.rotateXAxis(rotation[1], new double[]{0,0,0});
                int[] startCoords = startTemp.castToXY();
                Points endTemp = new Points(bond.getEnd());
                endTemp.rotateZAxis(rotation[2], new double[]{0,0,0});

                endTemp.rotateYAxis(rotation[0], new double[]{0,0,0});
                endTemp.rotateXAxis(rotation[1], new double[]{0,0,0});
                int[] endCoords = endTemp.castToXY();
                pen.setColor(Color.BLACK);
                pen.drawLine(startCoords[0], startCoords[1], endCoords[0], endCoords[1]);
                if (bond.isDoubleBond()) {
                    startTemp = new Points(bond.getSecondStart());
                    startTemp.rotateZAxis(rotation[2], new double[]{0,0,0});

                    startTemp.rotateYAxis(rotation[0], new double[]{0,0,0});
                    startTemp.rotateXAxis(rotation[1], new double[]{0,0,0});
                    startCoords = startTemp.castToXY();
                    endTemp = new Points(bond.getSecondEnd());
                    endTemp.rotateZAxis(rotation[2], new double[]{0,0,0});
                    endTemp.rotateYAxis(rotation[0], new double[]{0,0,0});
                    endTemp.rotateXAxis(rotation[1], new double[]{0,0,0});
                    endCoords = endTemp.castToXY();
                    pen.drawLine(startCoords[0], startCoords[1], endCoords[0], endCoords[1]);

                }
            }

            Font myFont = new Font("Serif", Font.PLAIN, 20);
            pen.setFont(myFont);
            String formattedScale = String.format("%,d", scale);
            pen.drawString("1 : " + formattedScale, 10, 20);

            bs.show();
    }

    public static void clearScreen(Graphics pen) {
        pen.drawImage(baseState, 0, 0, null);
    }

    public static void calculateScale() {
        double realDist = 1.10236 * Math.pow(10, -8);
        int[] carbon1 = molecule[0].getCenter().castToXY();
        int[] carbon4 = molecule[3].getCenter().castToXY();
        double pixelDistance = Math.abs(carbon1[1] - carbon4[1]);
        double screenDistance = pixelDistance/(double)PPI;
        scale = (int) (screenDistance/realDist);
    }

}
