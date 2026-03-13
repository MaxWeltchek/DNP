public class Bond {
    private Points start;
    private Points end;
    private Points secondStart;
    private Points secondEnd;
    private boolean doubleBond;
    private final double doubleBondOffset = 0.2;

    public Bond(Atom startAtom, Atom endAtom, boolean doubleBond_) {
        doubleBond = doubleBond_;
        double dx = endAtom.getCenter().getCoordinates()[0] - startAtom.getCenter().getCoordinates()[0];
        double dy = endAtom.getCenter().getCoordinates()[1] - startAtom.getCenter().getCoordinates()[1];
        if (!doubleBond) {
            double theta = Math.atan2(dy, dx);
            start = new Points(startAtom.getCenter().getCoordinates()[0] + startAtom.getRadius() * Math.cos(theta), startAtom.getCenter().getCoordinates()[1] + startAtom.getRadius() * Math.sin(theta), startAtom.getCenter().getCoordinates()[2]);
            double thetaTheOtherWay = theta + Math.PI;
            end = new Points(endAtom.getCenter().getCoordinates()[0] + endAtom.getRadius() * Math.cos(thetaTheOtherWay), endAtom.getCenter().getCoordinates()[1] + endAtom.getRadius() * Math.sin(thetaTheOtherWay), endAtom.getCenter().getCoordinates()[2]);
        } else {
            double theta = Math.atan2(dy, dx) + doubleBondOffset;
            start = new Points(startAtom.getCenter().getCoordinates()[0] + startAtom.getRadius() * Math.cos(theta), startAtom.getCenter().getCoordinates()[1] + startAtom.getRadius() * Math.sin(theta), startAtom.getCenter().getCoordinates()[2]);
            double thetaTheOtherWay = theta + Math.PI - (doubleBondOffset * 2);
            end = new Points(endAtom.getCenter().getCoordinates()[0] + endAtom.getRadius() * Math.cos(thetaTheOtherWay), endAtom.getCenter().getCoordinates()[1] + endAtom.getRadius() * Math.sin(thetaTheOtherWay), endAtom.getCenter().getCoordinates()[2]);

            theta = Math.atan2(dy, dx) - doubleBondOffset;
            secondStart = new Points(startAtom.getCenter().getCoordinates()[0] + startAtom.getRadius() * Math.cos(theta), startAtom.getCenter().getCoordinates()[1] + startAtom.getRadius() * Math.sin(theta), startAtom.getCenter().getCoordinates()[2]);
            thetaTheOtherWay = theta + Math.PI + (doubleBondOffset * 2);
            secondEnd = new Points(endAtom.getCenter().getCoordinates()[0] + endAtom.getRadius() * Math.cos(thetaTheOtherWay), endAtom.getCenter().getCoordinates()[1] + endAtom.getRadius() * Math.sin(thetaTheOtherWay), endAtom.getCenter().getCoordinates()[2]);
        }
    }

    public Points getStart() {
        return start;
    }
    public Points getEnd() {
        return end;
    }
    public Points getSecondStart() {
        return secondStart;
    }
    public Points getSecondEnd() {
        return secondEnd;
    }
    public boolean isDoubleBond() {
        return doubleBond;
    }
}
