package citygen.city.districts.blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import citygen.city.roads.Node;
import citygen.city.roads.edges.HalfEdge;
import citygen.graph.Graph;
import citygen.utils.Utils;

public class GraphBlockGenerator {
    private Graph graph; 

    public GraphBlockGenerator(Graph graph) {
        this.graph = graph; 
    }





    public Graph generate() {
        Set<HalfEdge> visited = new HashSet<>();

        for (Node start : graph.getNodes()) {

            for (Node neighbor : start.getNeighbors()) {

                HalfEdge startHe = new HalfEdge(start, neighbor);

                if (visited.contains(startHe)) continue;

                List<Node> face = new ArrayList<>();

                Node prev = start;
                Node current = neighbor;

                Node startNode = start;
                Node startNext = neighbor;

                while (true) {

                    visited.add(new HalfEdge(prev, current));
                    visited.add(new HalfEdge(current, prev));

                    face.add(prev);

                    Node next = getNextClockwiseNeighbor(current, prev);

                    prev = current;
                    current = next;

                    if (prev == startNode && current == startNext) {
                        break;
                    }

                    if (current == null) break;
                }

                if (face.size() > 2) {
                    graph.addBlock(new Block(face));
                }
            }
        }

        return graph;
    }








    private Node getNextClockwiseNeighbor(Node current, Node from) {
        List<Node> neighbors = current.getNeighbors();

        double baseAngle = Math.atan2(
            from.getY() - current.getY(),
            from.getX() - current.getX()
        );

        Node best = null;
        double bestDiff = Double.POSITIVE_INFINITY;

        for (Node n : neighbors) {

            if (n == from) continue;

            double angle = Math.atan2(
                n.getY() - current.getY(),
                n.getX() - current.getX()
            );

            double diff = Utils.normalizeAngle(angle - baseAngle);

            if (diff > 0 && diff < bestDiff) {
                bestDiff = diff;
                best = n;
            }
        }

        return best;
    }
}
