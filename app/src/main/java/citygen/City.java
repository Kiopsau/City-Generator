package citygen;

import javax.swing.JFrame;

import citygen.graph.Graph;
import citygen.render.Renderer;

public class City {
    private Graph graph; 

    public City(Graph graph) {
        this.graph = graph; 
    }

    public City() {
        this.graph = new Graph(); 
    }


    public void draw() {

        JFrame frame = new JFrame("City Generator");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        Renderer renderer = new Renderer(graph);
        
        frame.setContentPane(renderer);

        frame.setVisible(true);
    }
}
