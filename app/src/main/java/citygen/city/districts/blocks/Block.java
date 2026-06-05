package citygen.city.districts.blocks;

import java.util.ArrayList;
import java.util.List;

import citygen.city.districts.District;
import citygen.city.districts.blocks.buildings.Building;
import citygen.city.roads.Node;

public class Block {
    private final District district; 

    private final List<Node> corners;
    private final List<Building> buildings;

    public Block(District district, List<Node> corners) {
        this.district = district; 
        this.corners = corners;
        this.buildings = new ArrayList<>();
    }
}