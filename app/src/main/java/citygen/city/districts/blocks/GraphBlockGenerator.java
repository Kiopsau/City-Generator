package citygen.city.districts.blocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import citygen.city.roads.Edge;
import citygen.city.roads.Node;
import citygen.graph.Graph;

public class GraphBlockGenerator {
    private Graph graph; 

    public GraphBlockGenerator(Graph graph) {
        this.graph = graph; 
    }

    /*This road network is an organically grown, tree-dominated street layout that expands outward from a 
    central starting point. Roads branch incrementally from existing intersections, with a bias toward 
    extending newer branches, creating a natural sense of urban sprawl rather than dense clustering around 
    the center. Minimum spacing rules keep intersections evenly distributed, while proximity checks prevent 
    roads from crowding one another or creating unrealistically narrow blocks. Occasional loop connections
     introduce alternative routes and improve connectivity, avoiding the rigidity of a pure tree structure. 
     After generation, any road crossings are converted into proper intersections and nearby junctions are 
     merged, resulting in a clean, coherent network that resembles the street patterns commonly found in 
     suburban developments, small towns, or organically evolved cities rather than a strict grid-based plan. */


    public Graph generate() {

        List<List<Node>> faces = new ArrayList<>();

        Set<DirectedEdge> visited = new HashSet<>();

        for (Edge edge : graph.getEdges()) {

            walkFace(edge.getA(), edge.getB(), visited, faces);
            walkFace(edge.getB(), edge.getA(), visited, faces);
        }

        removeOuterFace(faces); 

        for (List<Node> face : faces) {
            graph.addBlock(new Block(face));
        }

        return graph;
    }

    private void walkFace(
            Node startA,
            Node startB,
            Set<DirectedEdge> visited,
            List<List<Node>> faces) {

        DirectedEdge start = new DirectedEdge(startA, startB);

        if (visited.contains(start)) {
            return;
        }

        List<Node> face = new ArrayList<>();

        Node prev = startA;
        Node current = startB;

        while (true) {

            visited.add(new DirectedEdge(prev, current));

            face.add(prev);

            Node next = getNextClockwise(prev, current);

            Node oldCurrent = current;

            current = next;
            prev = oldCurrent;

            if (prev == startA && current == startB) {
                break;
            }
        }

        if (face.size() >= 3) {
            faces.add(face);
        }
    }

    private Node getNextClockwise(Node previous, Node center) {

        List<Node> neighbors = getSortedNeighbors(center);

        int index = neighbors.indexOf(previous);

        return neighbors.get(
            (index - 1 + neighbors.size()) % neighbors.size()
        );
    }

    private List<Node> getSortedNeighbors(Node node) {

        List<Node> neighbors = new ArrayList<>();

        for (Edge edge : node.getEdges()) {

            Node other =
                edge.getA() == node
                    ? edge.getB()
                    : edge.getA();

            neighbors.add(other);
        }

        neighbors.sort(
            Comparator.comparingDouble(
                n -> Math.atan2(
                    n.getY() - node.getY(),
                    n.getX() - node.getX()
                )
            )
        );

        return neighbors;
    }

    private void removeOuterFace(List<List<Node>> faces) {

        if (faces.isEmpty()) {
            return;
        }

        List<Node> largest = null;
        double largestArea = -1;

        for (List<Node> face : faces) {

            double area = Math.abs(area(face));

            if (area > largestArea) {
                largestArea = area;
                largest = face;
            }
        }

        faces.remove(largest);
    }

    private double area(List<Node> polygon) {

        double sum = 0;

        for (int i = 0; i < polygon.size(); i++) {

            Node a = polygon.get(i);
            Node b = polygon.get((i + 1) % polygon.size());

            sum += a.getX() * b.getY()
                 - b.getX() * a.getY();
        }

        return sum * 0.5;
    }

    private static class DirectedEdge {

        private final Node a;
        private final Node b;

        DirectedEdge(Node a, Node b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {

            if (!(o instanceof DirectedEdge d)) {
                return false;
            }

            return a == d.a && b == d.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }
}
