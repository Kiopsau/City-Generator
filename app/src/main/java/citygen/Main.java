package citygen;

import citygen.city.districts.blocks.GraphBlockGenerator;
import citygen.city.roads.GraphRoadGenerator;
import citygen.graph.Graph;

public class Main {
    public static void main(String[] args) {
        Graph roads = new GraphRoadGenerator(800, 600, 200, 40).generate(); 
        Graph blocks = new GraphBlockGenerator(roads).generate(); 

        City city = new City(blocks); 
        
        city.draw(); 

        // GraphRoadGenerator generator =
        // new GraphRoadGenerator(800, 600, 120, 40);

        // Graph graph = generator.generate(); 
    } 
}
