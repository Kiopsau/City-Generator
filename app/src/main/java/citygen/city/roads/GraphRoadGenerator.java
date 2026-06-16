package citygen.city.roads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import citygen.city.roads.edges.Edge;
import citygen.graph.Graph;
import citygen.utils.Utils;

public class GraphRoadGenerator {

    private final Random random = new Random();

    private final int width;
    private final int height;
    private final int maxNodes;
    private final double stepSize;

    // NEW: tuning parameters
    private final double minDistance;
    private final double loopChance;

    public GraphRoadGenerator(int width, int height, int maxNodes, double stepSize) {
        this(width, height, maxNodes, stepSize, stepSize * 0.75, 0.08);
    } 

    public GraphRoadGenerator(int width, int height, int maxNodes, double stepSize, double minDistance, double loopChance) {
        this.width = width;
        this.height = height;
        this.maxNodes = maxNodes;
        this.stepSize = stepSize;
        this.minDistance = minDistance;
        this.loopChance = loopChance;
    }

    public Graph generate() {

        Graph graph = new Graph();
        List<Node> nodes = new ArrayList<>();

        // 1. Seed in center
        Node start = new Node(0, width / 2.0, height / 2.0);
        graph.addNode(start);
        nodes.add(start);

        int id = 1;

        // 2. Grow graph
        while (nodes.size() < maxNodes) {

            // bias toward newer nodes (reduces center clustering)
            Node base = nodes.get((int) (Math.pow(random.nextDouble(), 2) * nodes.size()));

            double angle = random.nextDouble() * Math.PI * 2;

            double nx = base.getX() + Math.cos(angle) * stepSize;
            double ny = base.getY() + Math.sin(angle) * stepSize;

            // reject out-of-bounds instead of clamping
            if (nx < 0 || nx > width || ny < 0 || ny > height) {
                continue;
            }

            // enforce spacing
            if (!isFarEnough(nx, ny, nodes)) {
                continue;
            } 

            if (tooCloseToEdges(nx, ny, base, graph)) {
                continue; 
            }

            Node newNode = new Node(id, nx, ny);

            if (edgeTooCloseToNodes(base, newNode, nodes)) {
                continue;
            }

            id++;

            graph.addNode(newNode);
            graph.connect(base, newNode);

            // optional loop creation (turns tree into graph)
            if (random.nextDouble() < loopChance) {
                Node other = nodes.get(random.nextInt(nodes.size()));
                if (other != base) {
                    graph.connect(newNode, other);
                }
            }

            nodes.add(newNode); 
        } 

        // List<Intersection> intersections = new ArrayList<>();

        // List<Edge> edges = new ArrayList<>(graph.getEdges());

        // // 1. detect intersections (store edge pairs correctly)
        // for (int i = 0; i < edges.size(); i++) {
        //     for (int j = i + 1; j < edges.size(); j++) {
        //         Edge e1 = edges.get(i); 
        //         Edge e2 = edges.get(j); 

        //         if (e1.getA() == e2.getA() || e1.getA() == e2.getB() || e1.getB() == e2.getA() || e1.getB() == e2.getB()) {
        //             continue;
        //         }

        //         Node point = Utils.getIntersection(
        //                 e1.getA().getX(), e1.getA().getY(),
        //                 e1.getB().getX(), e1.getB().getY(),
        //                 e2.getA().getX(), e2.getA().getY(),
        //                 e2.getB().getX(), e2.getB().getY()
        //         );

        //         if (point == null) {
        //             continue;
        //         }

        //         intersections.add(new Intersection(point, e1, e2));
        //     } 
        // }

        // List<Node> intersectionNodes = new ArrayList<>(); 
        // List<Edge> edgesToRemove = new ArrayList<>();

        // // collect edges
        // for (Intersection i : intersections) {

        //     if (!edgesToRemove.contains(i.a)) {
        //         edgesToRemove.add(i.a);
        //     }

        //     if (!edgesToRemove.contains(i.b)) {
        //         edgesToRemove.add(i.b);
        //     }
        // }

        // // remove originals FIRST
        // for (Edge e : edgesToRemove) {
        //     graph.removeEdge(e); 
        //     e.getA().removeEdge(e); 
        //     e.getB().removeEdge(e); 
        // }

        // // now rebuild split edges
        // for (Intersection i : intersections) {

        //     Node p = i.point;

        //     Edge a = i.a;
        //     Edge b = i.b;

        //     Node existing = findExistingIntersection(p, intersectionNodes); 

        //     if (existing != null) {
        //         p = existing;
        //     } else {
        //         graph.addNode(p); 
        //         intersectionNodes.add(p); 
        //     }

        //     graph.connect(a.getA(), p);
        //     graph.connect(p, a.getB());

        //     graph.connect(b.getA(), p);
        //     graph.connect(p, b.getB());
        // }

        Map<Edge, List<Node>> splitPoints = new HashMap<>();

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        List<Node> intersectionNodes = new ArrayList<>();

        for (int i = 0; i < edges.size(); i++) {

            for (int j = i + 1; j < edges.size(); j++) {

                Edge e1 = edges.get(i);
                Edge e2 = edges.get(j);

                // Skip edges sharing endpoints
                if (e1.getA() == e2.getA() ||
                    e1.getA() == e2.getB() ||
                    e1.getB() == e2.getA() ||
                    e1.getB() == e2.getB()) {
                    continue;
                }

                Node point = Utils.getIntersection(
                    e1.getA().getX(), e1.getA().getY(),
                    e1.getB().getX(), e1.getB().getY(),
                    e2.getA().getX(), e2.getA().getY(),
                    e2.getB().getX(), e2.getB().getY()
                );

                if (point == null) {
                    continue;
                }

                Node existing = findExistingIntersection(point, intersectionNodes);

                if (existing != null) {
                    point = existing;
                } else {
                    graph.addNode(point);
                    intersectionNodes.add(point);
                }

                splitPoints.computeIfAbsent(e1, k -> new ArrayList<>()).add(point);
                splitPoints.computeIfAbsent(e2, k -> new ArrayList<>()).add(point);
            }
        }

        // Remove intersected edges
        for (Edge edge : splitPoints.keySet()) {
 
            graph.removeEdge(edge);

            edge.getA().removeEdge(edge);
            edge.getB().removeEdge(edge);
        }

        // Rebuild each edge
        for (Edge edge : splitPoints.keySet()) {

            List<Node> points = splitPoints.get(edge);

            points.sort((p1, p2) -> {

                double d1 =
                    Math.pow(p1.getX() - edge.getA().getX(), 2) +
                    Math.pow(p1.getY() - edge.getA().getY(), 2);

                double d2 =
                    Math.pow(p2.getX() - edge.getA().getX(), 2) +
                    Math.pow(p2.getY() - edge.getA().getY(), 2);

                return Double.compare(d1, d2);
            });

            Node previous = edge.getA();

            for (Node point : points) {

                graph.connect(previous, point);

                previous = point;
            }

            graph.connect(previous, edge.getB());
        }

        mergeCloseNodes(graph); 

        normalizeGraph(graph); 

        for (Node n : graph.getNodes()) {
            n.sortEdgesAndNeighbors(); 
        }

        for (Node n : new ArrayList<>(graph.getNodes())) {
            if (n.getEdges().isEmpty()) {
                graph.removeNode(n);
            }
        }

        return graph;
    }



    private void normalizeGraph(Graph graph) {
        // 1. Remove duplicate edges 
        Set<Edge> unique = new HashSet<>();
        List<Edge> toRemove = new ArrayList<>();

        for (Edge e : graph.getEdges()) {
            if (!unique.add(e)) {
                toRemove.add(e);
            }
        }

        for (Edge e : toRemove) {
            graph.removeEdge(e);
            e.getA().removeEdge(e);
            e.getB().removeEdge(e);
        }

        // 2. FULL RESET 
        for (Node n : graph.getNodes()) {
            n.getNeighbors().clear();
            n.getEdges().clear();
        }

        // 3. Rebuild strictly from graph edges
        for (Edge e : graph.getEdges()) {

            e.getA().addEdge(e);
            e.getB().addEdge(e);

            e.getA().connect(e.getB());
        }

        // 4. Sort geometry last
        for (Node n : graph.getNodes()) {
            n.sortEdgesAndNeighbors();
        }
    }



    private boolean isFarEnough(double x, double y, List<Node> nodes) {
        for (Node n : nodes) {
            double dx = n.getX() - x;
            double dy = n.getY() - y;
            if (dx * dx + dy * dy < minDistance * minDistance) {
                return false;
            }
        }
        return true;
    } 

    
    private boolean tooCloseToEdges(double x, double y, Node base, Graph city) {

        double minEdgeDistance = stepSize * 0.5;

        for (Edge e : city.getEdges()) {

            Node a = e.getA();
            Node b = e.getB();

            double dist = Utils.pointToSegmentDistance(
                    x, y,
                    a.getX(), a.getY(),
                    b.getX(), b.getY()
            );

            if (dist < minEdgeDistance) {
                return true;
            }
        }

        return false;
    } 



    private boolean edgeTooCloseToNodes(Node a, Node b, List<Node> nodes) {

        double minDist = stepSize * 0.6;

        for (Node n : nodes) {

            // ignore endpoints
            if (n == a || n == b) continue;

            double dist = Utils.pointToSegmentDistance(
                    n.getX(), n.getY(),
                    a.getX(), a.getY(),
                    b.getX(), b.getY()
            );

            if (dist < minDist) {
                return true;
            }
        }

        return false;
    } 



    private Node findExistingIntersection(Node point, List<Node> nodes) {

        double epsilon = 0.01;

        for (Node n : nodes) {

            double dx = n.getX() - point.getX();
            double dy = n.getY() - point.getY();

            if (dx * dx + dy * dy < epsilon * epsilon) {
                return n;
            }
        }

        return null;
    }


    // private List<Node> getIntersections(Edge edge, Graph city) {
    //     List<Node> intersections = new ArrayList<>(); 
    //     for (Edge other : city.getEdges()) {
    //         if (edge == other) continue; 

    //         if (edge.getA() == other.getA() || edge.getA() == other.getB() || edge.getB() == other.getA() || edge.getB() == other.getB()) {
    //             continue;
    //         }

    //         Node intersection = Utils.getIntersection(
    //             edge.getA().getX(), edge.getA().getY(), 
    //             edge.getB().getX(), edge.getB().getY(), 
    //             other.getA().getX(), other.getA().getY(), 
    //             other.getB().getX(), other.getB().getY()
    //         ); 

    //         if (intersection == null) {
    //             continue; 
    //         } 
    //         intersections.add(intersection); 
    //     } 
    //     return intersections; 
    // } 


    private void mergeCloseNodes(Graph graph) {
        double mergeDistance = stepSize * 0.4; 

        List<Node> nodes = new ArrayList<>(graph.getNodes());
        Map<Node, Node> parent = new HashMap<>();

        // Union-find style merging
        for (int i = 0; i < nodes.size(); i++) {
            Node a = nodes.get(i);

            for (int j = i + 1; j < nodes.size(); j++) {
                Node b = nodes.get(j);

                if (parent.containsKey(b)) continue;

                double dx = a.getX() - b.getX();
                double dy = a.getY() - b.getY();

                if (dx * dx + dy * dy < mergeDistance * mergeDistance) {

                    // merge b into a
                    parent.put(b, a);
                }
            }
        }

        // redirect edges
        for (Edge e : new ArrayList<>(graph.getEdges())) {

            Node a = Utils.resolve(e.getA(), parent);
            Node b = Utils.resolve(e.getB(), parent);

            if (a == b) {
                graph.removeEdge(e);
                continue;
            }

            // if endpoints changed, rebuild edge
            if (a != e.getA() || b != e.getB()) {
                graph.removeEdge(e);
                graph.connect(a, b);
            }
        }

        // remove merged nodes
        for (Node n : nodes) {
            if (parent.containsKey(n)) {
                graph.removeNode(n);
            }
        }
    } 
}