package mini_proj_dsa;

import java.util.*;


class FaultToleranceCalculator {
private int[][] adjacencyMatrix;
private int nodeCount;
public FaultToleranceCalculator() {
	
}
public FaultToleranceCalculator(int nodeCount) {
    this.nodeCount = nodeCount;
    adjacencyMatrix = new int[nodeCount][nodeCount];
}
// Add a link between nodes
public void addLink(int node1, int node2) {
    adjacencyMatrix[node1][node2] = 1;
    adjacencyMatrix[node2][node1] = 1;
}
// Print the adjacency matrix
public void printAdjacencyMatrix() {
    System.out.println("Adjacency Matrix:");
    for (int i = 0; i < nodeCount; i++) {
        for (int j = 0; j < nodeCount; j++) {
            System.out.print(adjacencyMatrix[i][j] + " ");
        }
        System.out.println();
    }
}
// Disable a node by removing all its connections
public void disableNode(int node) {
  for (int i = 0; i < nodeCount; i++) {
      adjacencyMatrix[node][i] = 0;
      adjacencyMatrix[i][node] = 0;
  }
}
// Restore all links of a disabled node
public void restoreNode(int node, List<Integer> connections) {
  for (int i : connections) {
      adjacencyMatrix[node][i] = 1;
      adjacencyMatrix[i][node] = 1;
  }
}
// Check if the network is connected using DFS
public boolean isNetworkConnected() {
  boolean[] visited = new boolean[nodeCount];
  int startNode = findFirstActiveNode();
  if (startNode == -1) return false; // No active nodes in the network
  dfs(startNode, visited);
  // Check if all active nodes were visited
  for (int i = 0; i < nodeCount; i++) {
      if (isNodeActive(i) && !visited[i]) {
          return false; // Found an unvisited active node, network is disconnected
      }
  }
  return true; // Network is connected
}
// Perform DFS from a given node
private void dfs(int node, boolean[] visited) {
  visited[node] = true;
  for (int i = 0; i < nodeCount; i++) {
      if (adjacencyMatrix[node][i] == 1 && !visited[i]) {
          dfs(i, visited);
      }
  }
}
// Find the first active node in the network
private int findFirstActiveNode() {
  for (int i = 0; i < nodeCount; i++) {
      if (isNodeActive(i)) return i;
  }
  return -1; // No active nodes
}
// Check if a node is active (has at least one connection)
private boolean isNodeActive(int node) {
  for (int i = 0; i < nodeCount; i++) {
      if (adjacencyMatrix[node][i] == 1) return true;
  }
  return false;
}
// Calculate fault tolerance based on tolerable failures
public double calculateFaultTolerance() {
  int tolerableFailures = 0;
  for (int i = 0; i < nodeCount; i++) {
      // Temporarily disable the node
      List<Integer> connections = new ArrayList<>();
      for (int j = 0; j < nodeCount; j++) {
          if (adjacencyMatrix[i][j] == 1) {
              connections.add(j);
          }
      }
      disableNode(i);
      // Check if the network is still connected
      if (isNetworkConnected()) {
          tolerableFailures++;
      }
      // Restore the node’s connections
      restoreNode(i, connections);
  }
  return (double) tolerableFailures / nodeCount;
}
public int[][] getAdjacencyMatrix() {
  return adjacencyMatrix;
}
}
	public class Fault_Tolerance {
	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        // Get the number of nodes and links
	        System.out.print("Enter the number of nodes: ");
	        int nodeCount = scanner.nextInt();
	        FaultToleranceCalculator network = new FaultToleranceCalculator(nodeCount);
	        System.out.print("Enter the number of links: ");
	        int linkCount = scanner.nextInt();
	        System.out.println("Enter the links (node1 node2): ");
	        for (int i = 0; i < linkCount; i++) {
	            int node1 = scanner.nextInt();
	            int node2 = scanner.nextInt();
	            if (node1 >= 0 && node1 < nodeCount && node2 >= 0 && node2 < nodeCount) {
	                network.addLink(node1, node2);
	            } else {
	                System.out.println("Error: Node numbers must be between 0 and " + (nodeCount - 1));
	                i--;
	            }
	        }
	        // Print the adjacency matrix
	        network.printAdjacencyMatrix();
	        double faultTolerance = network.calculateFaultTolerance();
	        System.out.println("Fault tolerance: " + faultTolerance);
	        // Visualize the graph
	        GraphVisualizer.visualize(network.getAdjacencyMatrix(), nodeCount);
	        scanner.close();
	    }
	}

