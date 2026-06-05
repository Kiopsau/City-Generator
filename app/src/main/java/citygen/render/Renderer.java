package citygen.render; 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D; 
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import citygen.city.roads.Edge;
import citygen.city.roads.Node;
import citygen.graph.Graph;
import citygen.utils.Utils;
;

public class Renderer extends JPanel {

    private Graph city; 

    private Node hoveredNode; 

    private double zoom = 1.0;
    private double offsetX = 0;
    private double offsetY = 0; 

    private int lastX;
    private int lastY;

    public Renderer(Graph city) {
        this.city = city;

        setBackground(Color.BLACK); 

        setFocusable(true); 
        requestFocusInWindow(); 

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override   
            public void mouseMoved(MouseEvent e) {
                double worldX = (e.getX() - offsetX) / zoom;
                double worldY = (e.getY() - offsetY) / zoom;

                hoveredNode = Utils.findNodeAt(worldX, worldY, city);
                repaint(); 
            }
        }); 

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        }); 

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                offsetX += e.getX() - lastX;
                offsetY += e.getY() - lastY;

                lastX = e.getX();
                lastY = e.getY();

                repaint();
            }
        });

        addMouseWheelListener(e -> {
            double mouseX = e.getX();
            double mouseY = e.getY();

            double oldZoom = zoom;

            if (e.getWheelRotation() < 0) {
                zoom *= 1.1;
            } else {
                zoom /= 1.1;
            }

            zoom = Math.max(0.1, Math.min(zoom, 10.0));

            offsetX = mouseX - ((mouseX - offsetX) * zoom / oldZoom);
            offsetY = mouseY - ((mouseY - offsetY) * zoom / oldZoom);

            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create(); 

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.translate(offsetX, offsetY);
        g2.scale(zoom, zoom);



        drawEdges(g2);
        // drawBlocks(g2);
        drawNodes(g2);



        g2.dispose(); 
    }

    private void drawEdges(Graphics2D g2) {
        for (Edge edge : city.getEdges()) {

            Node a = edge.getA();
            Node b = edge.getB(); 

            boolean highlighted = hoveredNode != null && (a == hoveredNode || b == hoveredNode);

            if (highlighted) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            } 

            g2.drawLine(
                    (int) a.getX(),
                    (int) a.getY(),
                    (int) b.getX(),
                    (int) b.getY()
            );
        }
    }

    private void drawNodes(Graphics2D g2) {

        for (Node node : city.getNodes()) {
            boolean highlighted = node == hoveredNode; 
            
            if (highlighted) {
                g2.setColor(Color.GREEN);
            } else {
                g2.setColor(Color.RED);
            } 

            int size = 6;

            g2.fillOval(
                    (int) node.getX() - size / 2,
                    (int) node.getY() - size / 2,
                    size,
                    size
            );
        }
    } 

    // private final List<Color> colors = List.of(
    //     Color.BLACK, Color.WHITE, Color.RED, Color.GREEN,
    //     Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA,
    //     Color.GRAY, Color.DARK_GRAY, Color.LIGHT_GRAY,
    //     Color.ORANGE, Color.PINK
    // );


    // private void drawBlocks(Graphics2D g2) {
    //     int i = 0;

    //     for (Block block : city.getBlocks()) {
    //         List<Node> corners = block.getCorners();
    //         if (corners.size() < 3) continue;

    //         int[] xPoints = new int[corners.size()];
    //         int[] yPoints = new int[corners.size()];

    //         for (int j = 0; j < corners.size(); j++) {
    //             xPoints[j] = (int) corners.get(j).getX();
    //             yPoints[j] = (int) corners.get(j).getY();
    //         }

    //         int colorIndex = Math.abs(block.getCorners().hashCode()) % colors.size(); 
    //         g2.setColor(colors.get(colorIndex)); 

    //         g2.fillPolygon(xPoints, yPoints, corners.size());
    //         i++;
    //     }
    // }
}