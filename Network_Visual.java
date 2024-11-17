package mini_proj_dsa;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;

public class Network_Visual extends JFrame {
    private NetworkTopology networkTopology;

    public Network_Visual() {
    }

    public Network_Visual(NetworkTopology networkTopology) {
        this.networkTopology = networkTopology;
        setTitle("Network Topology Visualizer - " + networkTopology.getTopologyType().toUpperCase());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new TopologyPanel());
    }

    private class TopologyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            try {
                switch (networkTopology.getTopologyType().toLowerCase()) {
                    case "bus":
                        drawBusTopology(g2d);
                        break;
                    case "ring":
                        drawRingTopology(g2d);
                        break;
                    case "tree":
                        drawTreeTopology(g2d, networkTopology.getTreeRoot(), getWidth() / 2, 50, getWidth() / 4);
                        break;
                    case "mesh":
                    case "hybrid":
                        drawMeshOrHybridTopology(g2d);
                        break;
                    case "star":
                        drawStarTopology(g2d);
                        break;
                    default:
                        g2d.drawString("Unsupported topology type", 20, 20);
                }
            } catch (Exception e) {
                g2d.drawString("Error rendering topology: " + e.getMessage(), 20, 40);
            }
        }

        private void drawBusTopology(Graphics2D g2d) {
            int y = getHeight() / 2;
            int spacing = getWidth() / (networkTopology.getBusTopology().size() + 1);
            for (int i = 0; i < networkTopology.getBusTopology().size(); i++) {
                int x = (i + 1) * spacing;
                drawNode(g2d, x, y, networkTopology.getBusTopology().get(i));
                if (i > 0) {
                    g2d.draw(new Line2D.Double(x - spacing, y, x, y));
                }
            }
        }

        private void drawRingTopology(Graphics2D g2d) {
            int radius = Math.min(getWidth(), getHeight()) / 3;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int n = networkTopology.getRingTopology().size();
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n;
                int x = centerX + (int) (radius * Math.cos(angle));
                int y = centerY + (int) (radius * Math.sin(angle));
                drawNode(g2d, x, y, networkTopology.getRingTopology().get(i));
                if (i > 0) {
                    int prevX = centerX + (int) (radius * Math.cos(2 * Math.PI * (i - 1) / n));
                    int prevY = centerY + (int) (radius * Math.sin(2 * Math.PI * (i - 1) / n));
                    g2d.draw(new Line2D.Double(prevX, prevY, x, y));
                }
                if (i == n - 1) {
                    int firstX = centerX + (int) (radius * Math.cos(0));
                    int firstY = centerY + (int) (radius * Math.sin(0));
                    g2d.draw(new Line2D.Double(x, y, firstX, firstY));
                }
            }
        }

        private void drawTreeTopology(Graphics2D g2d, NetworkTopology.TreeNode node, int x, int y, int offset) {
            if (node == null) return;
            drawNode(g2d, x, y, node.name);
            if (node.left != null) {
                g2d.draw(new Line2D.Double(x, y, x - offset, y + 100));
                drawTreeTopology(g2d, node.left, x - offset, y + 100, offset / 2);
            }
            if (node.right != null) {
                g2d.draw(new Line2D.Double(x, y, x + offset, y + 100));
                drawTreeTopology(g2d, node.right, x + offset, y + 100, offset / 2);
            }
        }

        private void drawMeshOrHybridTopology(Graphics2D g2d) {
            int n = networkTopology.getAdjMatrix().size();
            int radius = Math.min(getWidth(), getHeight()) / 3;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            String[] nodes = networkTopology.nodeNames;
            Map<String, Point> positions = new HashMap<>();
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n;
                int x = centerX + (int) (radius * Math.cos(angle));
                int y = centerY + (int) (radius * Math.sin(angle));
                positions.put(nodes[i], new Point(x, y));
                drawNode(g2d, x, y, nodes[i]);
            }
            for (String node1 : nodes) {
                Map<String, Integer> connections = networkTopology.getAdjMatrix().get(node1);
                if (connections != null) {
                    for (String node2 : connections.keySet()) {
                        Point p1 = positions.get(node1);
                        Point p2 = positions.get(node2);
                        g2d.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
                    }
                }
            }
        }

        private void drawStarTopology(Graphics2D g2d) {
            if (networkTopology.getStarTopology().isEmpty()) return;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(getWidth(), getHeight()) / 4;
            drawNode(g2d, centerX, centerY, networkTopology.getStarTopology().get(0));
            int n = networkTopology.getStarTopology().size();
            for (int i = 1; i < n; i++) {
                double angle = 2 * Math.PI * (i - 1) / (n - 1);
                int x = centerX + (int) (radius * Math.cos(angle));
                int y = centerY + (int) (radius * Math.sin(angle));
                drawNode(g2d, x, y, networkTopology.getStarTopology().get(i));
                g2d.draw(new Line2D.Double(centerX, centerY, x, y));
            }
        }

        private void drawNode(Graphics2D g2d, int x, int y, String name) {
            int size = 30;
            g2d.setColor(Color.CYAN);
            g2d.fillOval(x - size / 2, y - size / 2, size, size);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - size / 2, y - size / 2, size, size);
            g2d.drawString(name, x - size / 4, y + size / 4);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter topology type (bus, ring, tree, mesh, hybrid, star) or type 'exit' to quit: ");
                String topologyType = scanner.nextLine().trim();
                if (topologyType.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the program. Goodbye!");
                    break; // Exit the loop
                }
                if (!Arrays.asList("bus", "ring", "tree", "mesh", "hybrid", "star").contains(topologyType.toLowerCase())) {
                    throw new IllegalArgumentException("Invalid topology type.");
                }

                System.out.print("Enter number of nodes: ");
                int numNodes = scanner.nextInt();
                if (numNodes <= 0) throw new IllegalArgumentException("Number of nodes must be positive.");
                scanner.nextLine(); // Consume newline

                System.out.println("Enter node names (space-separated): ");
                String[] nodeNames = scanner.nextLine().trim().split("\\s+");
                if (nodeNames.length != numNodes) {
                    throw new IllegalArgumentException("Number of node names does not match the number of nodes.");
                }

                NetworkTopology network = new NetworkTopology(topologyType, numNodes);
                network.addNodes(nodeNames);

                if (topologyType.equalsIgnoreCase("mesh") || topologyType.equalsIgnoreCase("hybrid")) {
                    System.out.println("Enter connections as 'node1 node2 weight' (or type 'done' to finish):");
                    while (true) {
                        String input = scanner.nextLine().trim();
                        if (input.equalsIgnoreCase("done")) break;
                        String[] parts = input.split("\\s+");
                        if (parts.length != 3) {
                            System.out.println("Invalid connection format. Use 'node1 node2 weight'.");
                            continue;
                        }
                        String node1 = parts[0], node2 = parts[1];
                        int weight;
                        try {
                            weight = Integer.parseInt(parts[2]);
                        } catch (NumberFormatException e) {
                            System.out.println("Weight must be an integer.");
                            continue;
                        }
                        network.addConnection(node1, node2, weight);
                    }
                }

                SwingUtilities.invokeLater(() -> {
                    Network_Visual frame = new Network_Visual(network);
                    frame.setVisible(true);
                });
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close(); // Close the scanner
    }

}
