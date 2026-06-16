package citygen.utils;

import java.util.Map;

import citygen.city.roads.Node;
import citygen.graph.Graph;

public class Utils {
    public static Node findNodeAt(double targetX, double targetY, Graph city) {

        for (Node node : city.getNodes()) {

            double dx = node.getX() - targetX;
            double dy = node.getY() - targetY;

            double distanceSq = dx * dx + dy * dy;

            if (distanceSq <= 36) {
                return node;
            }
        }

        return null;
    }


    public static double pointToSegmentDistance(double px, double py, double ssx, double ssy, double sex, double sey) {

        double abx = sex - ssx;
        double aby = sey - ssy;

        double apx = px - ssx;
        double apy = py - ssy;

        double abLenSq = abx * abx + aby * aby;

        double t = (apx * abx + apy * aby) / abLenSq;

        t = Math.max(0, Math.min(1, t));

        double cx = ssx + t * abx;
        double cy = ssy + t * aby;

        double dx = px - cx;
        double dy = py - cy;

        return Math.sqrt(dx * dx + dy * dy);
    } 




    public static Node getIntersection(double l1sX, double l1sY, double l1eX, double l1eY, double l2sX, double l2sY, double l2eX, double l2eY) {
        double den = (l1sX - l1eX) * (l2sY - l2eY) - (l1sY - l1eY) * (l2sX - l2eX); 

        // Parallel or coincident lines
        double EPSILON = 1e-9;
        if (Math.abs(den) < EPSILON) {
            return null;
        }

        double t = ((l1sX - l2sX) * (l2sY - l2eY) - (l1sY - l2sY) * (l2sX - l2eX)) / den; 

        double u = ((l1sX - l2sX) * (l1sY - l1eY) - (l1sY - l2sY) * (l1sX - l1eX)) / den; 

        // Intersection must lie on both segments
        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {

            double ix = l1sX + t * (l1eX - l1sX); 
            double iy = l1sY + t * (l1eY - l1sY); 

            return new Node(-1, ix, iy); 
        }

        return null;
    } 

    public static Node resolve(Node n, Map<Node, Node> parent) {
        while (parent.containsKey(n)) {
            n = parent.get(n);
        }
        return n;
    } 

    public static double angle(Node a, Node b) {
        return Math.atan2(b.getY() - a.getY(), b.getX() - a.getX()); 
    } 

    public static double angleDifference(double a, double b) {
        double diff = b - a;
        while (diff <= 0) diff += Math.PI * 2; 
        return diff;
    } 

    public static double normalizeAngle(double a) {
        while (a < 0) a += Math.PI * 2;
        while (a >= Math.PI * 2) a -= Math.PI * 2;
        return a;
    }
}
