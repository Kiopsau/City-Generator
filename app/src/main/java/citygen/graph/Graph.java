package citygen.graph;

import java.util.ArrayList;
import java.util.List;

import citygen.city.districts.District;
import citygen.city.roads.Edge;
import citygen.city.roads.Node;


public class Graph {
    private List<Node> nodes; 
    private List<Edge> edges; 
    private List<District> districts; 

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        districts = new ArrayList<>(); 
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void addDistrict(District district) {
        districts.add(district); 
    }


    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    } 

    public List<District> getDistricts() {
        return districts; 
    }


    public void removeNode(Node node) {
        nodes.remove(node); 

        List<Edge> edgesToRemove = new ArrayList<>(); 

        for (Edge edge : edges) {
            if(edge.getA() == node || edge.getB() == node) {
                edgesToRemove.add(edge); 
            }
        }
        edges.removeAll(edgesToRemove); 
    } 

    public void removeEdge(Edge edge) {
        edges.remove(edge); 
    }



    public void connect(Node a, Node b) {

        if (a == b || areConnected(a, b)) {
            return;
        }

        Edge edge = new Edge(a, b);

        edges.add(edge);

        a.addEdge(edge);
        b.addEdge(edge);

        a.connect(b);
        b.connect(a);
    }

    public void clearEdges() {
        edges.clear();
    }

    public boolean areConnected(Node a, Node b) {

        for (Edge e : edges) {

            if (
                (e.getA() == a && e.getB() == b) ||
                (e.getA() == b && e.getB() == a)
            ) {
                return true;
            }
        }

        return false;
    }
}
