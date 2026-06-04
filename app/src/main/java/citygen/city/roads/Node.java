package citygen.city.roads;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id; 

    private double x; 
    private double y; 

    private List<Node> neighbors; 
    private List<Edge> edges; 

    public Node(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y; 

        this.neighbors = new ArrayList<>();
        this.edges = new ArrayList<>();
    }




    public void connect(Node other) {
        if (!neighbors.contains(other)) {
            neighbors.add(other);
        }

        if (!other.neighbors.contains(this)) {
            other.neighbors.add(this);
        }
    } 

    public void addEdge(Edge edge) {
        if (!edges.contains(edge)){
            edges.add(edge); 
        }
    }

    public void removeEdge(Edge edge) {
        if (edges.contains(edge)){
            edges.remove(edge); 
        }
    }




    public int getID() {
        return id; 
    }

    public double getX() {
        return x; 
    }

    public double getY() {
        return y; 
    }

    public List<Node> getNeighbors() {
        return neighbors; 
    }

    public List<Edge> getEdges() {
        return edges; 
    }




    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    } 

    
}
