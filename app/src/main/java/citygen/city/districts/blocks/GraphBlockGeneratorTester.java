/*This road network is an organically grown, tree-dominated street layout that expands outward from a 
    central starting point. Roads branch incrementally from existing intersections, with a bias toward 
    extending newer branches, creating a natural sense of urban sprawl rather than dense clustering around 
    the center. Minimum spacing rules keep intersections evenly distributed, while proximity checks prevent 
    roads from crowding one another or creating unrealistically narrow blocks. Occasional loop connections
     introduce alternative routes and improve connectivity, avoiding the rigidity of a pure tree structure. 
     After generation, any road crossings are converted into proper intersections and nearby junctions are 
     merged, resulting in a clean, coherent network that resembles the street patterns commonly found in 
     suburban developments, small towns, or organically evolved cities rather than a strict grid-based plan. */


    // public Graph generate() {

    //     List<List<Node>> faces = new ArrayList<>();

    //     Set<DirectedEdge> visited = new HashSet<>();

    //     for (Edge edge : graph.getEdges()) {

    //         walkFace(edge.getA(), edge.getB(), visited, faces);
    //         walkFace(edge.getB(), edge.getA(), visited, faces);
    //     }

    //     removeOuterFace(faces); 

    //     for (List<Node> face : faces) {
    //         graph.addBlock(new Block(face));
    //     }

    //     return graph;
    // }

    // private void walkFace(
    //         Node startA,
    //         Node startB,
    //         Set<DirectedEdge> visited,
    //         List<List<Node>> faces) {

    //     DirectedEdge start = new DirectedEdge(startA, startB);

    //     if (visited.contains(start)) {
    //         return;
    //     }

    //     List<Node> face = new ArrayList<>();

    //     Node prev = startA;
    //     Node current = startB;

    //     while (true) {

    //         visited.add(new DirectedEdge(prev, current));

    //         face.add(prev);

    //         Node next = getNextClockwise(prev, current);

    //         Node oldCurrent = current;

    //         current = next;
    //         prev = oldCurrent;

    //         if (prev == startA && current == startB) {
    //             break;
    //         }
    //     }

    //     if (face.size() >= 3) {
    //         faces.add(face);
    //     }
    // }

    // private Node getNextClockwise(Node previous, Node center) {

    //     List<Node> neighbors = getSortedNeighbors(center);

    //     int index = neighbors.indexOf(previous);

    //     return neighbors.get(
    //         (index - 1 + neighbors.size()) % neighbors.size()
    //     );
    // }

    // private List<Node> getSortedNeighbors(Node node) {

    //     List<Node> neighbors = new ArrayList<>();

    //     for (Edge edge : node.getEdges()) {

    //         Node other =
    //             edge.getA() == node
    //                 ? edge.getB()
    //                 : edge.getA();

    //         neighbors.add(other);
    //     }

    //     neighbors.sort(
    //         Comparator.comparingDouble(
    //             n -> Math.atan2(
    //                 n.getY() - node.getY(),
    //                 n.getX() - node.getX()
    //             )
    //         )
    //     );

    //     return neighbors;
    // }

    // private void removeOuterFace(List<List<Node>> faces) {

    //     if (faces.isEmpty()) {
    //         return;
    //     }

    //     List<Node> largest = null;
    //     double largestArea = -1;

    //     for (List<Node> face : faces) {

    //         double area = Math.abs(area(face));

    //         if (area > largestArea) {
    //             largestArea = area;
    //             largest = face;
    //         }
    //     }

    //     faces.remove(largest);
    // }

    // public Graph generate() {
    //     Set<DirectedEdge> visitedEdges = new HashSet<>(); 

    //     for (Edge edge : graph.getEdges()) {

    //         DirectedEdge ab = new DirectedEdge(edge.getA(), edge.getB()); 
    //         DirectedEdge ba = new DirectedEdge(edge.getB(), edge.getA()); 

    //         if (!visitedEdges.contains(ab) && !visitedEdges.contains(ba)) {

    //             List<Node> face = traceFace(ab, visitedEdges); 

    //             if (face != null && face.size() >= 3) {
    //                 graph.addBlock(new Block(face)); 
    //             }
    //         }
    //     }

    //     return graph; 
    // }

    // private List<Node> traceFace(DirectedEdge start, Set<DirectedEdge> visited) {
    //     List<Node> face = new ArrayList<>();

    //     Node previous = start.getA();
    //     Node current = start.getB();

    //     int safety = 0; 

    //     while (true) {

    //         visited.add(new DirectedEdge(previous, current));

    //         face.add(previous);

    //         Node next = getNextFaceNode(previous, current);

    //         if (next == null) {
    //             return null;
    //         }

    //         previous = current;
    //         current = next; 

    //         if (++safety > graph.getEdges().size() * 2) {
    //             return null; 
    //         }

    //         if (previous == start.getA() && current == start.getB()) {
    //             face.add(previous); 
    //             break;
    //         }
    //     }

    //     return face;
    // } 


    // private Node getNextFaceNode(Node previous, Node current) {
    //     List<Node> neighbors = new ArrayList<>(current.getNeighbors());

    //     if (neighbors.size() < 2) return null;

    //     // angle of incoming edge
    //     double incomingAngle = Math.atan2(
    //         previous.getY() - current.getY(),
    //         previous.getX() - current.getX()
    //     );

    //     // we want to find the FIRST edge clockwise AFTER incoming edge
    //     Node bestNode = null;
    //     double bestAngle = Double.POSITIVE_INFINITY;

    //     for (Node neighbor : neighbors) {

    //         if (neighbor == previous) continue;

    //         double angle = Math.atan2(
    //             neighbor.getY() - current.getY(),
    //             neighbor.getX() - current.getX()
    //         );

    //         // compute clockwise delta from incoming direction
    //         double diff = incomingAngle - angle;

    //         if (diff < 0) diff += 2 * Math.PI;

    //         // IMPORTANT: we want smallest clockwise turn
    //         if (diff < bestAngle) {
    //             bestAngle = diff;
    //             bestNode = neighbor;
    //         }
    //     }

    //     return bestNode;
    // }