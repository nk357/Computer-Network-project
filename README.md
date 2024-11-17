# Virtual Network Simulator

A comprehensive simulation tool that allows users to create and analyze virtual networks using various topology structures. This simulator provides insights into network performance, fault tolerance, and redundancy. Users can define nodes, customize topology types, and visualize data paths in real-time using **Java Swing** or **ASCII-based visualizations**. Additionally, it offers metrics for comparing the efficiency and resilience of different network topologies.

---

## Key Features

### 1. Network Creation and Topology Selection
- **Define Nodes**: Users can specify the number of nodes (devices) and their identifiers (e.g., names, IPs).
- **Topology Support**: Choose from multiple topology types:
  - **Bus**: Represents nodes on a single line.
  - **Star**: Central node connects to all others.
  - **Ring**: Nodes connected in a circular fashion.
  - **Mesh**: Each node connects to multiple nodes.
  - **Tree**: Models hierarchical structures.
  - **Hybrid**: Combination of different topologies.
- **Connections and Weights**:
  - Define the connections between nodes and select cable types (e.g., twisted pair, coaxial) to simulate transfer times.
- **Data Structures Used**:
  - **Array** for Bus topology.
  - **Tree** for Tree topology.
  - **Circular Linked List** for Ring topology.
  - **Graph** for Mesh and Hybrid topologies.
- **Outputs**:
  - Visualization of the created network topology using Java Swing and ASCII.
  - Adjacency matrix representing connections, connection types, and transfer times.

---

### 2. Fault Tolerance and Redundancy Testing
- **Simulate Failures**: Test network resilience by simulating node or link failures.
  - If a link or node fails, the simulator recalculates paths (if alternatives exist).
- **Insights**:
  - Observe varied resilience levels across topology types (e.g., Mesh with multiple paths vs. Ring with single failure points).
- **Inputs**:
  - Specify nodes or links to disable during the fault tolerance test.
- **Outputs**:
  - Updated visualization of data transmission paths with disabled nodes/links.
  - Summary of network behavior, including failed connections and rerouted paths.

---

### 3. Performance Metrics and Analysis
- **Key Metrics**:
  - **Average Transmission Time**: Average time or hops for data transmission across the network.
  - **Node Reachability**: Percentage of nodes reachable when certain nodes or links are disabled.
  - **Network Robustness**: Resilience of the network to failures.
- **Compare Topologies**: Evaluate performance differences in terms of speed, reachability, and robustness.
- **Outputs**:
  - A detailed dashboard displaying metrics for each topology.
  - Graphs or tables comparing average transmission time, reachability, and robustness.

---

### 4. Broadcast Simulation
- **Simulate Broadcasts**: Send data from one source node to all nodes in the network.
- **Optimized Paths**: Use Prim’s Algorithm to calculate minimum-cost broadcast paths.
- **Inputs**:
  - Source node for the broadcast.
- **Outputs**:
  - Broadcast path visualization through Java Swing and ASCII.
  - Transmission cost for reaching all nodes.

---

## Technology and Tools
- **Programming Language**: Java
- **Visualization**: Java Swing for GUI-based visualization and ASCII for text-based visualization.
- **Data Structures**:
  - Array/List: For Bus topology.
  - Tree Structure: For Tree topology.
  - Circular Linked List: For Ring topology.
  - Graph: For Mesh and Hybrid topologies.

---

This project aims to provide a robust and interactive way to learn and analyze network topologies, helping users understand network performance, fault tolerance, and design trade-offs.
