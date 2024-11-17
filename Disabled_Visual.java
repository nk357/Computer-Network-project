package mini_proj_dsa;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;
class CS extends JPanel {
   private final Map<String, List<NetTopo_F.Node>> adjacencyList;
   private final Set<String> disabledNodes;
   private final Set<String> disabledLinks;
   private final Map<String, Point> nodePositions = new HashMap<>();
   private final int panelWidth = 800;
   private final int panelHeight = 600;
   public CS(Map<String, List<NetTopo_F.Node>> adjacencyList,
                            Set<String> disabledNodes, Set<String> disabledLinks) {
       this.adjacencyList = adjacencyList;
       this.disabledNodes = disabledNodes;
       this.disabledLinks = disabledLinks;
       generateNodePositions();
       setPreferredSize(new Dimension(panelWidth, panelHeight));
   }
   // Calculate positions for nodes in a circular layout
   private void generateNodePositions() {
       int nodeCount = adjacencyList.size();
       int centerX = panelWidth / 2;
       int centerY = panelHeight / 2;
       int radius = Math.min(centerX, centerY) - 50;
       int angleIncrement = 360 / Math.max(1, nodeCount);
       int angle = 0;
       for (String node : adjacencyList.keySet()) {
           int x = (int) (centerX + radius * Math.cos(Math.toRadians(angle)));
           int y = (int) (centerY + radius * Math.sin(Math.toRadians(angle)));
           nodePositions.put(node, new Point(x, y));
           angle += angleIncrement;
       }
   }
   @Override
   protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       Graphics2D g2d = (Graphics2D) g;
       // Draw edges
       g2d.setColor(Color.BLUE);
       for (String node : adjacencyList.keySet()) {
           if (disabledNodes.contains(node)) continue;
           Point p1 = nodePositions.get(node);
           for (NetTopo_F.Node neighbor : adjacencyList.get(node)) {
               if (disabledNodes.contains(neighbor.name)) continue;
               String link = node + "-" + neighbor.name;
               if (disabledLinks.contains(link)) continue;
               Point p2 = nodePositions.get(neighbor.name);
               if (p2 != null) {
                   g2d.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
               }
           }
       }
       // Draw nodes
       for (String node : nodePositions.keySet()) {
           Point position = nodePositions.get(node);
           if (disabledNodes.contains(node)) {
               g2d.setColor(Color.GRAY); // Disabled nodes in gray
           } else {
               g2d.setColor(Color.RED);  // Active nodes in red
           }
           g2d.fillOval(position.x - 10, position.y - 10, 20, 20);
           g2d.setColor(Color.BLACK);
           g2d.drawString(node, position.x - 5, position.y - 15);
       }
   }
   public static void visualizeNetwork(Map<String, List<NetTopo_F.Node>> adjacencyList,
                                       Set<String> disabledNodes, Set<String> disabledLinks) {
       JFrame frame = new JFrame("Network Topology Visualization");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.add(new CS(adjacencyList, disabledNodes, disabledLinks));
       frame.pack();
       frame.setLocationRelativeTo(null);
       frame.setVisible(true);
   }
}
class NetTopo_F {
   private final Map<String, List<Node>> adjacencyList = new HashMap<>();
   private final Set<String> disabledNodes = new HashSet<>();
   private final Set<String> disabledLinks = new HashSet<>();
   class Node {
       String name;
       int weight;
       Node(String name, int weight) {
           this.name = name;
           this.weight = weight;
       }
   }
   public void addConnection(String node1, String node2, int weight) {
       adjacencyList.putIfAbsent(node1, new ArrayList<>());
       adjacencyList.putIfAbsent(node2, new ArrayList<>());
       adjacencyList.get(node1).add(new Node(node2, weight));
       adjacencyList.get(node2).add(new Node(node1, weight));
   }
   public Map<String, List<Node>> getAdjacencyList() {
       return adjacencyList;
   }
   public void disableNode(String node) {
       if (adjacencyList.containsKey(node)) {
           disabledNodes.add(node);
       } else {
           System.out.println("Node '" + node + "' does not exist in the network.");
       }
   }
   public void disableLink(String node1, String node2) {
       if (adjacencyList.containsKey(node1) && adjacencyList.containsKey(node2)) {
           disabledLinks.add(node1 + "-" + node2);
           disabledLinks.add(node2 + "-" + node1);
       } else {
           System.out.println("One or both nodes do not exist: " + node1 + ", " + node2);
       }
   }
   // Method to visualize the current network topology
   public void visualize() {
       CS.visualizeNetwork(adjacencyList, disabledNodes, disabledLinks);
   }
   // Calculate reachability percentage after disabling certain nodes and links
   public double calculateReachability(String startNode) {
       if (!adjacencyList.containsKey(startNode) || disabledNodes.contains(startNode)) {
           System.out.println("Start node is invalid or disabled.");
           return 0.0;
       }
       Set<String> visited = new HashSet<>();
       Queue<String> queue = new LinkedList<>();
       queue.add(startNode);
       visited.add(startNode);
       while (!queue.isEmpty()) {
           String current = queue.poll();
           for (Node neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
               if (!visited.contains(neighbor.name) && !disabledNodes.contains(neighbor.name) &&
                   !disabledLinks.contains(current + "-" + neighbor.name)) {
                   visited.add(neighbor.name);
                   queue.add(neighbor.name);
               }
           }
       }
       int reachableNodes = visited.size();
       int totalNodes = (int) adjacencyList.keySet().stream().filter(node -> !disabledNodes.contains(node)).count();
       return (totalNodes > 0) ? (reachableNodes / (double) totalNodes) * 100 : 0.0;
   }
   // Calculate redundancy by counting alternative paths using DFS
   public int calculatePathRedundancy(String startNode, String endNode) {
       if (!adjacencyList.containsKey(startNode) || !adjacencyList.containsKey(endNode)) {
           System.out.println("One or both nodes do not exist.");
           return 0;
       }
       return findAllPaths(startNode, endNode).size();
   }
   private List<List<String>> findAllPaths(String startNode, String endNode) {
       List<List<String>> paths = new ArrayList<>();
       Set<String> visited = new HashSet<>();
       findPathsDFS(startNode, endNode, visited, new ArrayList<>(), paths);
       return paths;
   }
   private void findPathsDFS(String current, String destination, Set<String> visited, List<String> path, List<List<String>> paths) {
       visited.add(current);
       path.add(current);
       if (current.equals(destination)) {
           paths.add(new ArrayList<>(path));
       } else {
           for (Node neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
               if (!visited.contains(neighbor.name) && !disabledNodes.contains(neighbor.name) &&
                   !disabledLinks.contains(current + "-" + neighbor.name)) {
                   findPathsDFS(neighbor.name, destination, visited, path, paths);
               }
           }
       }
       path.remove(path.size() - 1);
       visited.remove(current);
   }
   public double calculateFaultTolerance(String startNode) {
       double reachability = calculateReachability(startNode);
       // Redundant path analysis could be added here for further fault tolerance metrics.
       return reachability;
   }
}
public class Disabled_Visual {
   public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
       NetTopo_F network = new NetTopo_F();
       // Adding connections
       System.out.println("Enter the number of connections:");
       int connections = scanner.nextInt();
       scanner.nextLine();
       if (connections == 0) {
           System.out.println("Error: The network must have at least one connection.");
           return;
       }
       for (int i = 0; i < connections; i++) {
           System.out.println("Enter connection (format: node1 node2 weight):");
           String node1 = scanner.next();
           String node2 = scanner.next();
           int weight = scanner.nextInt();
           scanner.nextLine();
           network.addConnection(node1, node2, weight);
       }
       // Visualize the network before fault tolerance
       network.visualize();
       // Disabling nodes and links
       System.out.println("Enter the number of nodes to disable:");
       int disableNodesCount = scanner.nextInt();
       scanner.nextLine();
       for (int i = 0; i < disableNodesCount; i++) {
           System.out.println("Enter node to disable:");
           String node = scanner.nextLine();
           network.disableNode(node);
       }
       System.out.println("Enter the number of links to disable:");
       int disableLinksCount = scanner.nextInt();
       scanner.nextLine();
       for (int i = 0; i < disableLinksCount; i++) {
           System.out.println("Enter link to disable (format: node1 node2):");
           String node1 = scanner.next();
           String node2 = scanner.next();
           scanner.nextLine();
           network.disableLink(node1, node2);
       }
       // Visualize the network after disabling nodes and links
       network.visualize();
       // Calculate fault tolerance
       //System.out.println("Fault tolerance: " + network.calculateFaultTolerance("A") + "%");
   }
}






