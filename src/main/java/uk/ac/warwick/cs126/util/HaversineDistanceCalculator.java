package uk.ac.warwick.cs126.util;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    /**
     * Converts degrees to radians
     *
     * @param x angle in degrees
     * @return angle in radians
     */
    private static double toRad(float x) {
        return x * Math.PI / 180;
    }

    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        double lat1r = toRad(lat1);
        double lat2r = toRad(lat2);
        double lon1r = toRad(lon1);
        double lon2r = toRad(lon1);

        double a = Math.sin((lat2r - lat1r) / 2) * Math.sin((lat2r - lat1r) / 2)
                + Math.cos(lat1r) * Math.cos(lat2r) * Math.sin((lon2r - lon1r) / 2) * Math.sin((lon2r - lon1r) / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double d = R * c;
        return (float) Math.round(d * 10) / 10;
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        return (float) Math.round(inKilometres(lat1, lon1, lat2, lon2) / kilometresInAMile * 10) / 10;
    }

}