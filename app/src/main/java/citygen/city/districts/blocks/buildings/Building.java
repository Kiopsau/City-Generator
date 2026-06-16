package citygen.city.districts.blocks.buildings;

import java.util.List;

import citygen.city.districts.blocks.Block;

public class Building extends Block{
    private double height; 
    private List<double[]> shape; 

    public Building(List<double[]> shape, double height) {
        super(null); 

        this.shape = shape; 
        this.height = height; 
    } 



    public List<double[]> getShape() {
        return shape; 
    }

    public double getHeight() {
        return height; 
    }
}
