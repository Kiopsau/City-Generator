package citygen.city.districts.blocks;

import java.util.ArrayList;
import java.util.List;

import citygen.city.districts.blocks.buildings.Building;
import citygen.city.roads.Node;

public class Block {
    //private final District district; 

    private final List<Node> corners;
    private final List<Building> buildings; 
    private double area; 

    public Block(List<Node> corners) {
        this.corners = corners;
        this.buildings = new ArrayList<>(); 

        this.area = getArea(); 
    } 

    public List<Node> getCorners() {
        return corners; 
    } 









    public double getArea() {
        double A = 0.0; 
        for (int i = 0; i < corners.size() - 1; i++) {
            double x1 = corners.get(i).getX(); 
            double y1 = corners.get(i).getY(); 
            double x2 = corners.get(i + 1).getX(); 
            double y2 = corners.get(i + 1).getY(); 
            A += (x1 * y2) - (y1 * x2); 
        }
        return Math.abs(A) / 2.0; 
    } 

    @Override 
    public String toString() {
        return "Block{" +
                "corners=" + corners +
                ", area=" + area +
                '}';
    }
}