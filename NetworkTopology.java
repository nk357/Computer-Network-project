package mini_proj_dsa;
import java.util.*;

public class NetworkTopology {
	   private String topologyType;
	   private List<String> busTopology;
	   private List<String> ringTopology;
	   private TreeNode treeRoot;
	   private Map<String, Map<String, Integer>> adjMatrix;
	   private List<String> starTopology;
	   public String[] nodeNames;
	   public NetworkTopology(String topologyType, int numNodes) {
	       this.topologyType = topologyType.toLowerCase();
	       this.busTopology = new ArrayList<>();
	       this.ringTopology = new ArrayList<>();
	       this.adjMatrix = new HashMap<>();
	       this.starTopology = new ArrayList<>();
	       this.nodeNames = new String[numNodes];
	   }
	   // Method to add nodes
	   public void addNodes(String[] nodes) {
	       this.nodeNames = nodes;
	       switch (topologyType) {
	           case "bus":
	               busTopology.addAll(Arrays.asList(nodes));
	               break;
	           case "ring":
	               ringTopology.addAll(Arrays.asList(nodes));
	               break;
	           case "tree":
	               treeRoot = buildTree(nodes, 0);
	               break;
	           case "mesh":
	           case "hybrid":
	               for (String node : nodes) {
	                   adjMatrix.put(node, new HashMap<>());
	               }
	               break;
	           case "star":
	               starTopology.addAll(Arrays.asList(nodes));
	               break;
	       }
	   }
	   // Method to add connections for mesh and hybrid topologies
	   public void addConnection(String node1, String node2, int weight) {
	       if (!topologyType.equals("mesh") && !topologyType.equals("hybrid")) return;
	       adjMatrix.get(node1).put(node2, weight);
	       adjMatrix.get(node2).put(node1, weight); // Assuming undirected connections
	   }
	   // Recursive method to build a tree structure
	   private TreeNode buildTree(String[] nodes, int index) {
	       if (index >= nodes.length) return null;
	       TreeNode root = new TreeNode(nodes[index]);
	       root.left = buildTree(nodes, 2 * index + 1);
	       root.right = buildTree(nodes, 2 * index + 2);
	       return root;
	   }
	   // Getter for topology type
	   public String getTopologyType() {
	       return topologyType;
	   }
	   // Getters for specific topologies
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
	   public List<String> getStarTopology() {
	       return starTopology;
	   }
	   // TreeNode class for tree topology
	   public static class TreeNode {
	       String name;
	       TreeNode left, right;
	       public TreeNode(String name) {
	           this.name = name;
	       }
	   }
	}

