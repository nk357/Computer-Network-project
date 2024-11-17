package mini_proj_dsa;

import java.util.*;

class Network_ASCII {
    private String topologyType;
    private int numNodes;
    private String[] nodeNames;
    private Map<String, Map<String, Integer>> adjMatrix; // For Mesh and Hybrid topologies
    private List<String> busTopology;  // For Bus topology
    private List<String> ringTopology; // For Ring topology
    private List<String> starTopology; // For Star topology
    private TreeNode treeRoot;         // For Tree topology

    // Constructor to initialize the topology type and number of nodes
    public Network_ASCII(String topologyType, int numNodes) {
        this.topologyType = topologyType;
        this.numNodes = numNodes;
        this.nodeNames = new String[numNodes];
        adjMatrix = new HashMap<>();
        busTopology = new ArrayList<>();
        ringTopology = new ArrayList<>();
        starTopology = new ArrayList<>();
    }
    public Network_ASCII() {
    	
    }

    // Add nodes to the appropriate topology based on the type
    public void addNodes(String[] names) {
        this.nodeNames = names;

        // Validate the number of nodes
        if (names.length != numNodes) {
            throw new IllegalArgumentException("Number of nodes does not match the specified count.");
        }

        switch (topologyType.toLowerCase()) {
            case "bus":
                busTopology.addAll(Arrays.asList(names));
                break;
            case "ring":
                ringTopology.addAll(Arrays.asList(names));
                break;
            case "tree":
                buildTreeTopology(names);
                break;
            case "mesh":
            case "hybrid":
                for (String node : names) {
                    adjMatrix.put(node, new HashMap<>());
                }
                break;
            case "star":
                starTopology.addAll(Arrays.asList(names));
                break;
            default:
                throw new IllegalArgumentException("Unsupported topology type: " + topologyType);
        }
    }

    // Add connection between nodes for Mesh, Hybrid, and custom topologies
    public void addConnection(String node1, String node2, int weight) {
        // Validate node existence
        if (!adjMatrix.containsKey(node1) || !adjMatrix.containsKey(node2)) {
            throw new IllegalArgumentException("One or both nodes do not exist in the network.");
        }

        if (topologyType.equals("mesh") || topologyType.equals("hybrid")) {
            adjMatrix.get(node1).put(node2, weight);
            adjMatrix.get(node2).put(node1, weight);
        }
    }

    // Display topology with ASCII art
    public void displayTopology() {
    	
        System.out.println("\nNetwork Topology (" + topologyType.toUpperCase() + "):");
        switch (topologyType.toLowerCase()) {
            case "bus":
                displayBusTopology();
                break;
            case "ring":
                displayRingTopology();
                break;
            case "tree":
                System.out.println("Tree Topology:");
                displayTreeWithRelationships(treeRoot);
                break;
            case "mesh":
            case "hybrid":
                System.out.println("Mesh/Hybrid Topology:");
                displayMeshOrHybridTopology();
                break;
            case "star":
                displayStarTopology();
                break;
            
            default:
                System.out.println("Unsupported topology type.");
        }
    	
    }

    // Display Bus Topology in ASCII format
    private void displayBusTopology() {
        System.out.print("BUS: ");
        for (int i = 0; i < busTopology.size(); i++) {
            System.out.print(busTopology.get(i));
            if (i < busTopology.size() - 1) System.out.print(" -- ");
        }
        System.out.println();
    }

    // Display Ring Topology in ASCII format
    private void displayRingTopology() {
        System.out.print("RING: ");
        for (int i = 0; i < ringTopology.size(); i++) {
            System.out.print(ringTopology.get(i));
            if (i < ringTopology.size() - 1) System.out.print(" <--> ");
        }
        System.out.println(" <--> " + ringTopology.get(0)); // Close the ring
    }

    // Display Star Topology in ASCII format
    private void displayStarTopology() {
        if (starTopology.isEmpty()) return;
        String centralNode = starTopology.get(0); // First node is the central node
        System.out.println("STAR: ");
        for (int i = 1; i < starTopology.size(); i++) {
            System.out.println(centralNode + " <--> " + starTopology.get(i));
        }
    }

    // Display Mesh/Hybrid Topology as ASCII adjacency matrix
    private void displayMeshOrHybridTopology() {
        System.out.println("Adjacency Matrix (showing connections and weights):");
        System.out.println("   " + String.join(" ", nodeNames));
        for (String node1 : nodeNames) {
            System.out.print(node1 + ": ");
            for (String node2 : nodeNames) {
                Integer weight = adjMatrix.get(node1).get(node2);
                System.out.print((weight != null ? weight : "0") + " ");
            }
            System.out.println();
        }
    }

    // Helper methods for Tree topology
    private void buildTreeTopology(String[] nodes) {
        treeRoot = new TreeNode(nodes[0]); // First node as root
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(treeRoot);
        int i = 1;
        while (i < nodes.length) {
            TreeNode parent = queue.poll();
            if (parent != null) {
                TreeNode leftChild = new TreeNode(nodes[i++]);
                parent.left = leftChild;
                queue.add(leftChild);
                if (i < nodes.length) {
                    TreeNode rightChild = new TreeNode(nodes[i++]);
                    parent.right = rightChild;
                    queue.add(rightChild);
                }
            }
        }
    }

    private void displayTreeWithRelationships(TreeNode node) {
        if (node != null) {
            displayParentChildRelation(node);
            displayTreeWithRelationships(node.left);
            displayTreeWithRelationships(node.right);
        }
    }

    // Display parent-child relationships for Tree nodes
    private void displayParentChildRelation(TreeNode node) {
        if (node != null) {
            if (node.left != null) {
                System.out.println(node.name + " --> " + node.left.name); // Parent to left child
            }
            if (node.right != null) {
                System.out.println(node.name + " --> " + node.right.name); // Parent to right child
            }
        }
    }

    // Tree node class for Tree topology
    static class TreeNode {
        String name;
        TreeNode left, right;
        TreeNode(String name) {
            this.name = name;
            this.left = this.right = null;
        }
    }

    // Getters
    public String getTopologyType() {
        return topologyType;
    }

    public List<String> getBusTopology() {
        return busTopology;
    }

    public List<String> getRingTopology() {
        return ringTopology;
    }

    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    public Map<String, Map<String, Integer>> getAdjMatrix() {
        return adjMatrix;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                // Prompt for topology type or exit
                System.out.print("\nEnter topology type (bus, ring, tree, mesh, hybrid, star) or type 'exit' to quit: ");
                String topologyType = scanner.nextLine().toLowerCase();

                // Exit condition
                if (topologyType.equals("exit")) {
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                }

                // Validate topology type
                if (!topologyType.matches("bus|ring|tree|mesh|hybrid|star")) {
                    System.out.println("Invalid topology type. Please try again.");
                    continue;
                }

                // Input for number of nodes
                System.out.print("Enter number of nodes: ");
                int numNodes;
                try {
                    numNodes = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number of nodes. Please enter a valid integer.");
                    continue;
                }

                // Input for node names
                System.out.println("Enter node names (space-separated): ");
                String[] nodeNames = scanner.nextLine().split(" ");
                if (nodeNames.length != numNodes) {
                    System.out.println("Node names count does not match the specified number of nodes. Please try again.");
                    continue;
                }

                // Create and initialize the network
                Network_ASCII network = new Network_ASCII(topologyType, numNodes);
                network.addNodes(nodeNames);

                // For mesh/hybrid, add connections
                if (topologyType.equalsIgnoreCase("mesh") || topologyType.equalsIgnoreCase("hybrid")) {
                    System.out.println("Enter connections as 'node1 node2 weight' (or type 'done' to finish):");
                    while (true) {
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("done")) break;
                        String[] parts = input.split(" ");
                        if (parts.length != 3) {
                            System.out.println("Invalid input format. Example: node1 node2 weight");
                            continue;
                        }
                        String node1 = parts[0];
                        String node2 = parts[1];
                        int weight;
                        try {
                            weight = Integer.parseInt(parts[2]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid weight. Please enter an integer value.");
                            continue;
                        }
                        try {
                            network.addConnection(node1, node2, weight);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                // Display the topology
                network.displayTopology();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}