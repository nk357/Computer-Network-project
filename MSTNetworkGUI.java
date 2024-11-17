package mini_proj_dsa;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

class MSTNetworkGUI extends JFrame {
    private static final int NODE_RADIUS = 20;  // Radius of each node
    private static final int WIDTH = 800;       // Width of the window
    private static final int HEIGHT = 600;      // Height of the window

    // Edge class representing a connection between two nodes with a weight
    static class Edge implements Comparable<Edge> {
        String src, dest;
        int weight;

        Edge(String src, String dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    private int numNodes;
    private String[] nodeNames;
    private List<Edge> edges = new ArrayList<>();           // All edges in the graph
    private Map<String, Point> nodePositions = new HashMap<>(); // Positions of nodes
    private List<Edge> mstEdges = new ArrayList<>(); // MST edges

    public MSTNetworkGUI() {
    	
    }
    public MSTNetworkGUI(int numNodes) {
        this.numNodes = numNodes;
        setTitle("Network Visualization");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Add nodes and set their random positions for visualization
    public void addNodes(String[] names) {
        this.nodeNames = names;

        // Assign random positions within the frame for each node
        Random rand = new Random();
        for (int i = 0; i < numNodes; i++) {
            nodePositions.put(nodeNames[i], new Point(rand.nextInt(WIDTH - 100) + 50, rand.nextInt(HEIGHT - 100) + 50));
        }
    }

    // Add an edge between two nodes with a specified weight
    public void addConnection(String node1, String node2, int weight) {
        edges.add(new Edge(node1, node2, weight));
    }

    // Find method with path compression for union-find structure
    private int find(int[] parent, int i) {
        if (parent[i] != i) {
            parent[i] = find(parent, parent[i]);
        }
        return parent[i];
    }

    // Union method with rank to combine sets in union-find
    private void union(int[] parent, int[] rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    // Method to find MST using Kruskal’s algorithm
    public void findMinimumSpanningTree() {
        Collections.sort(edges);  // Sort the edges by weight

        Map<String, Integer> nodeIndexMap = new HashMap<>();
        for (int i = 0; i < numNodes; i++) {
            nodeIndexMap.put(nodeNames[i], i);
        }

        int[] parent = new int[numNodes];
        int[] rank = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            parent[i] = i;
            rank[i] = 0;
        }

        // Go through sorted edges to build the MST
        for (Edge edge : edges) {
            int srcRoot = find(parent, nodeIndexMap.get(edge.src));
            int destRoot = find(parent, nodeIndexMap.get(edge.dest));

            // If src and dest are not connected, add edge to MST and union them
            if (srcRoot != destRoot) {
                mstEdges.add(edge);
                union(parent, rank, srcRoot, destRoot);
            }

            // Stop once we have enough edges for the MST
            if (mstEdges.size() == numNodes - 1) break;
        }

        repaint();  // Redraw the visualization
    }

    // Override paint to draw the network and MST with edge weights and node labels
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw all edges with their weights
        g.setColor(Color.BLACK);
        for (Edge edge : edges) {
            Point p1 = nodePositions.get(edge.src);
            Point p2 = nodePositions.get(edge.dest);

            // Draw edge line
            g.drawLine(p1.x, p1.y, p2.x, p2.y);

            // Display the weight of the edge at the midpoint
            int midX = (p1.x + p2.x) / 2;
            int midY = (p1.y + p2.y) / 2;
            g.drawString(String.valueOf(edge.weight), midX, midY);
        }

        // Highlight MST edges in red
        g.setColor(Color.RED);
        for (Edge edge : mstEdges) {
            Point p1 = nodePositions.get(edge.src);
            Point p2 = nodePositions.get(edge.dest);

            // Draw MST edge in red
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Draw nodes with names
        g.setColor(Color.MAGENTA);
        for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
            Point p = entry.getValue();
            String nodeName = entry.getKey();

            // Draw node as a filled oval
            g.fillOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            // Display node name inside or near the node
            g.setColor(Color.BLACK);
            g.drawString(nodeName, p.x - NODE_RADIUS / 2, p.y + NODE_RADIUS / 2);
            g.setColor(Color.MAGENTA);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numNodes = 0;
        while (true) {
            try {
                System.out.print("Enter the number of network nodes: ");
                numNodes = Integer.parseInt(scanner.nextLine());
                if (numNodes <= 0) throw new IllegalArgumentException("Number of nodes must be positive.");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for the number of nodes.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        MSTNetworkGUI network = new MSTNetworkGUI(numNodes);
        network.setVisible(true);

        String[] nodeNames = null;
        while (true) {
            System.out.println("Enter the names of the network nodes (space-separated): ");
            nodeNames = scanner.nextLine().split(" ");
            if (nodeNames.length == numNodes) {
                network.addNodes(nodeNames);
                break;
            } else {
                System.out.println("Number of names provided does not match the number of nodes. Please try again.");
            }
        }

        System.out.println("Enter connections in the format 'node1 node2 weight' (type 'done' to finish): ");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) break;

            String[] parts = input.split(" ");
            if (parts.length != 3) {
                System.out.println("Invalid format. Please enter in the format 'node1 node2 weight'.");
                continue;
            }

            String node1 = parts[0];
            String node2 = parts[1];
            int weight;
            try {
                weight = Integer.parseInt(parts[2]);
                if (!network.nodePositions.containsKey(node1) || !network.nodePositions.containsKey(node2)) {
                    System.out.println("Invalid nodes specified. Ensure nodes are among: " + Arrays.toString(nodeNames));
                    continue;
                }
                network.addConnection(node1, node2, weight);
            } catch (NumberFormatException e) {
                System.out.println("Invalid weight. Please enter a valid integer for the weight.");
            }
        }

        network.findMinimumSpanningTree();
        scanner.close();
    }
}


