package citygen.city.districts;

import java.util.ArrayList;

import citygen.city.districts.blocks.Block;

public class District {
    public final DistrictType type; 
    
    public final ArrayList<Block> blocks; 

    public District(DistrictType type) {
        this.type = type; 

        this.blocks = new ArrayList<>(); 
    }

    public enum DistrictType {
        RESIDENTIAL, 
        COMMERCIAL, 
        INDUSTRIAL, 
        PARK, 
        MIXED_USE 
    } 


    public void addBlock(Block block) {
        blocks.add(block); 
    }
}
