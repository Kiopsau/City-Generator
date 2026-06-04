package citygen.city.blocks;

import java.util.List;

import citygen.city.blocks.buildings.Building;
import citygen.city.roads.Node;

public class Block {    
    List<Node> corners; 
    List<Building> buildings; 

    public Block(List<Node> corners) {
        this.corners = corners; 
    } 

    public List<Node> getCorners() {
        return corners; 
    } 
}
