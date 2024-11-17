package mini_proj_dsa;

import java.util.*;

class Node {
    String name;
    Set<Node> neighbors;

    // Constructor to initialize the node with a given name
    public Node(String name) {
        this.name = name;
        this.neighbors = new HashSet<>();
    }

    // Method to add a neighbor to the current node
    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    // Method to get the set of neighbors of the current node
    public Set<Node> getNeighbors() {
        return neighbors;
    }
}

class Network {
    List<Node> nodes;

    // Constructor to initialize the network with a list of nodes
    public Network(List<Node> nodes) {
        this.nodes = nodes;
    }

    // BFS for calculating the average transmission time (in hops) between source and destination
    public int bfsTransmissionTime(Node source, Node destination) {
        // If source or destination is null, return -1 (invalid input)
        if (source == null || destination == null) {
            return -1;
        }

        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        Map<Node, Integer> distance = new HashMap<>();
        
        queue.add(source);
        visited.add(source);
        distance.put(source, 0);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if (currentNode == destination) {
                return distance.get(currentNode); // Return the number of hops to reach the destination
            }

            for (Node neighbor : currentNode.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    distance.put(neighbor, distance.get(currentNode) + 1);
                }
            }
        }
        return -1; // Return -1 if no path exists between the source and destination
    }

    // DFS for checking if all nodes are reachable (network connectivity)
    public boolean isFullyConnected() {
        Set<Node> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        
        stack.push(nodes.get(0));  // Start DFS from the first node
        visited.add(nodes.get(0));

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            for (Node neighbor : currentNode.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                }
            }
        }
        return visited.size() == nodes.size();  // If all nodes are visited, network is fully connected
    }

    // Check if the network is robust: Removing any node should not disconnect the network
    public boolean isNetworkRobust() {
        for (Node node : nodes) {
            List<Node> remainingNodes = new ArrayList<>(nodes);
            remainingNodes.remove(node);  // Remove node from the network
            Network subNetwork = new Network(remainingNodes);

            // If removing the node disconnects the network, return false
            if (!subNetwork.isFullyConnected()) {
                return false; 
            }
        }
        return true;  // Network is robust if no node removal disconnects it
    }
}

public class Performance_Analysis {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Node> nodes = new ArrayList<>();
        
        // Input for node count
        System.out.print("Enter the number of nodes: ");
        int nodeCount = scanner.nextInt();
        scanner.nextLine();  // Consume newline left over

        // Create nodes with custom names
        System.out.println("Enter the names of the nodes:");
        for (int i = 0; i < nodeCount; i++) {
            String nodeName = scanner.nextLine();
            // Check for duplicate node names
            if (getNodeByName(nodes, nodeName) != null) {
                System.out.println("Node name " + nodeName + " already exists. Enter a unique name.");
                i--;  // Decrement the counter to retry input for this node
            } else {
                nodes.add(new Node(nodeName));
            }
        }

        // Input for network topology (edges)
        System.out.println("Enter the number of connections:");
        int edgeCount = scanner.nextInt();
        scanner.nextLine();  // Consume newline left over
        
        System.out.println("Enter the connections (node1 node2):");
        for (int i = 0; i < edgeCount; i++) {
            String node1 = scanner.next();
            String node2 = scanner.next();
            scanner.nextLine();  // Consume newline left over
            Node n1 = getNodeByName(nodes, node1);
            Node n2 = getNodeByName(nodes, node2);

            // Error handling: Ensure both nodes exist before adding a connection
            if (n1 == null || n2 == null) {
                System.out.println("One or both nodes not found: " + node1 + ", " + node2);
            } else {
                n1.addNeighbor(n2);
                n2.addNeighbor(n1);
            }
        }
        
        Network network = new Network(nodes);

        // Ask for source and destination for packet transmission
        System.out.print("Enter source node for transmission: ");
        String sourceName = scanner.next();
        System.out.print("Enter destination node for transmission: ");
        String destName = scanner.next();

        Node source = getNodeByName(nodes, sourceName);
        Node destination = getNodeByName(nodes, destName);

        // Error handling: Check if source or destination is null
        if (source == null || destination == null) {
            System.out.println("Invalid source or destination node.");
            return;  // Exit the program if invalid input is provided
        }

        // Calculate Average Transmission Time (using BFS)
        int transmissionTime = network.bfsTransmissionTime(source, destination);
        if (transmissionTime == -1) {
            System.out.println("No path found between source and destination.");
        } else {
            System.out.println("Average Transmission Time (hops): " + transmissionTime);
        }

        // Check Node Reachability (if all nodes are reachable)
        boolean isReachable = network.isFullyConnected();
        System.out.println("Network Reachable: " + (isReachable ? "Yes" : "No"));

        // Check Network Robustness (resilience to node failure)
        boolean isRobust = network.isNetworkRobust();
        System.out.println("Network Robust: " + (isRobust ? "Yes" : "No"));
    }

    // Helper method to get node by name
    private static Node getNodeByName(List<Node> nodes, String nodeName) {
        for (Node node : nodes) {
            if (node.name.equals(nodeName)) {
                return node;
            }
        }
        return null;  // Return null if node not found
    }
}
