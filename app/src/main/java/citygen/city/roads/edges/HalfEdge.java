package citygen.city.roads.edges;

import citygen.city.roads.Node;

public class HalfEdge {

    private final Node from;
    private final Node to;

    public HalfEdge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HalfEdge other)) return false;

        return from.getID() == other.from.getID()
            && to.getID() == other.to.getID();
    }

    @Override
    public int hashCode() {
        return from.getID() * 31 + to.getID();
    }

    @Override
    public String toString() {
        return from.getID() + " -> " + to.getID();
    }
}