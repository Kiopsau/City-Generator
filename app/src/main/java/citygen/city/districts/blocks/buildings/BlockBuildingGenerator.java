package citygen.city.districts.blocks.buildings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import citygen.city.districts.blocks.Block;
import citygen.city.roads.Node;

public class BlockBuildingGenerator {

    public static List<Building> generate(Block block) {
        List<Building> buildings = new ArrayList<>();
        List<Node> corners = block.getCorners();

        int numBuildings = Math.max(1, (int)(block.getArea() / 1500));

        int attempts = 0;
        int placed = 0;

        while (placed < numBuildings && attempts < numBuildings * 20) {
            attempts++;

            double x = 0;
            double y = 0;

            // sample random point in bounding box
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
            double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

            for (Node n : corners) {
                minX = Math.min(minX, n.getX());
                minY = Math.min(minY, n.getY());
                maxX = Math.max(maxX, n.getX());
                maxY = Math.max(maxY, n.getY());
            }
            Random random = new Random();
            x = minX + random.nextDouble() * (maxX - minX);
            y = minY + random.nextDouble() * (maxY - minY);

            // IMPORTANT: reject if outside polygon
            if (!pointInPolygon(corners, x, y)) continue;

            double width = 8 + random.nextDouble() * 12;
            double depth = 8 + random.nextDouble() * 12;
            double height = 30 + random.nextDouble() * 120;

            List<double[]> shape = List.of(
                new double[]{x, y},
                new double[]{x + width, y},
                new double[]{x + width, y + depth},
                new double[]{x, y + depth}
            );

            buildings.add(new Building(shape, height));
            placed++;
        }

        return buildings;
    }

    private static boolean pointInPolygon(List<Node> poly, double x, double y) {
        boolean inside = false;

        for (int i = 0, j = poly.size() - 1; i < poly.size(); j = i++) {

            double xi = poly.get(i).getX();
            double yi = poly.get(i).getY();
            double xj = poly.get(j).getX();
            double yj = poly.get(j).getY();

            boolean intersect =
                ((yi > y) != (yj > y)) &&
                (x < (xj - xi) * (y - yi) / (yj - yi + 1e-9) + xi);

            if (intersect) inside = !inside;
        }

        return inside;
    }
}