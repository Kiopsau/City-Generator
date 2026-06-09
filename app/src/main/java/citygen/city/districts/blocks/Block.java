package citygen.city.districts.blocks;

import java.util.ArrayList;
import java.util.List;

import citygen.city.districts.blocks.buildings.Building;
import citygen.city.roads.Node;

public class Block {
    //private final District district; 

    private final List<Node> corners;
    private final List<Building> buildings;

    public Block(List<Node> corners) {
        this.corners = corners;
        this.buildings = new ArrayList<>();
    } 

    public List<Node> getCorners() {
        return corners; 
    }
}