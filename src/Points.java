public class Points {
    //contains xyz coordinates for a given point
    private double[] coordinates;

    public Points(double x_, double y_, double z_) {
        coordinates = new double[]{x_, y_, z_};
    }

    public Points(Points original) {
        this.coordinates = original.coordinates.clone();
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    //rasterizes the coordinates of a point to be projected onto a 2d plane, based on focal length and distance from camera
    private double[] transposeToXY() {
        double[] XYCoordinates = new double[2];
        double[] cameraCoords = Camera.getCoordinates();
        double relativeX = coordinates[0] - cameraCoords[0];
        double relativeY = coordinates[1] - cameraCoords[1];
        double relativeZ = coordinates[2] - cameraCoords[2];

        XYCoordinates[0] = relativeX * (Camera.getFocalLength() / relativeZ);
        XYCoordinates[1] = relativeY * (Camera.getFocalLength() / relativeZ);
        return XYCoordinates;
    }

    //full casting method, both rasterizes and translates from 0,0 origin to 500, 500 java canvas origin
    public int[] castToXY() {
        double[] tempDouble = transposeToXY();
        int[] tempInt = {(int) tempDouble[0], (int) tempDouble[1]};
        tempInt[0] += 500;
        tempInt[1] = (tempInt[1] *-1) + 500;
        return tempInt;
    }

    //rotation methods in each axis, uses standard 3d rotation matrices and multiples them
    public void rotateXAxis(double theta, double[] center) {
        for (int i = 0; i < 3; i++) {
            coordinates[i] -= center[i];
        }
        double[][] rotationMatrix = new double[3][3];
        rotationMatrix[0] = new double[]{1.0, 0.0, 0.0};
        rotationMatrix[1] = new double[]{0.0, Math.cos(theta), -1.0 * Math.sin(theta)};
        rotationMatrix[2] = new double[]{0.0, Math.sin(theta), Math.cos(theta)};
        coordinates = multiplyMatrix(rotationMatrix, coordinates);
        for (int i = 0; i < 3; i++) {
            coordinates[i] += center[i];
        }
    }

    public void rotateYAxis(double theta, double[] center) {
        for (int i = 0; i < 3; i++) {
            coordinates[i] -= center[i];
        }
        double[][] rotationMatrix = new double[3][3];
        rotationMatrix[0] = new double[]{Math.cos(theta), 0.0, Math.sin(theta)};
        rotationMatrix[1] = new double[]{0.0, 1.0, 0.0};
        rotationMatrix[2] = new double[]{-1.0 * Math.sin(theta), 0.0, Math.cos(theta)};
        coordinates = multiplyMatrix(rotationMatrix, coordinates);
        for (int i = 0; i < 3; i++) {
            coordinates[i] += center[i];
        }
    }

    public void rotateZAxis(double theta, double[] center) {
        for (int i = 0; i < 3; i++) {
            coordinates[i] -= center[i];
        }
        double[][] rotationMatrix = new double[3][3];
        rotationMatrix[0] = new double[]{Math.cos(theta), -1.0 * Math.sin(theta), 0.0};
        rotationMatrix[1] = new double[]{Math.sin(theta), Math.cos(theta), 0.0};
        rotationMatrix[2] = new double[]{0.0, 0.0, 1.0};
        coordinates = multiplyMatrix(rotationMatrix, coordinates);
        for (int i = 0; i < 3; i++) {
            coordinates[i] += center[i];
        }
    }

    //helper matrix multiplication method
    //where transformationMatrix is a 3x3 transformation matrix
    private double[] multiplyMatrix(double[][] transformationMatrix, double[] coordinates) {
        double[] tempCords = new double[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tempCords[i] += transformationMatrix[i][j] * coordinates[j];
            }
        }
        return tempCords;
    }
}
