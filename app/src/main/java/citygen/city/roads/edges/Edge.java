package citygen.city.roads.edges;

import citygen.city.roads.Node;

public class Edge {

    private Node a;
    private Node b;

    public Edge(Node a, Node b) {
        if (a.getID() < b.getID()) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }

    public Node getA() {
        return a;
    }

    public Node getB() {
        return b;
    }

    public double length() {

        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();

        return Math.sqrt(dx * dx + dy * dy);
    } 


    public Node other(Node n) {
        return (a == n) ? b : a;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Edge e)) return false;

        return (a.getID() == e.a.getID() && b.getID() == e.b.getID());
    }

    @Override
    public int hashCode() {
        int min = Math.min(a.getID(), b.getID());
        int max = Math.max(a.getID(), b.getID());
        return min * 31 + max;
    }
} 