package mini_proj_dsa;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
class GraphVisualizer extends JPanel {
   private int[][] adjacencyMatrix;
   private int nodeCount;
   private int radius = 20; // Radius of each node circle
   private List<Point> nodePositions;
   public GraphVisualizer(int[][] adjacencyMatrix, int nodeCount) {
       this.adjacencyMatrix = adjacencyMatrix;
       this.nodeCount = nodeCount;
       this.nodePositions = new ArrayList<>();
       // Calculate node positions in a circular layout
       for (int i = 0; i < nodeCount; i++) {
           int x = (int) (200 + 150 * Math.cos(2 * Math.PI * i / nodeCount));
           int y = (int) (200 + 150 * Math.sin(2 * Math.PI * i / nodeCount));
           nodePositions.add(new Point(x, y));
       }
   }
   @Override
   protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       g.setColor(Color.BLACK);
       // Draw links based on the adjacency matrix
       for (int i = 0; i < nodeCount; i++) {
           for (int j = i + 1; j < nodeCount; j++) {
               if (adjacencyMatrix[i][j] == 1) {
                   Point p1 = nodePositions.get(i);
                   Point p2 = nodePositions.get(j);
                   g.drawLine(p1.x, p1.y, p2.x, p2.y);
               }
           }
       }
       // Draw nodes
       for (int i = 0; i < nodeCount; i++) {
           Point p = nodePositions.get(i);
           g.setColor(Color.BLUE);
           g.fillOval(p.x - radius / 2, p.y - radius / 2, radius, radius);
           // Draw node label
           g.setColor(Color.WHITE);
           g.drawString("N" + i, p.x - 6, p.y + 5);
       }
   }
   public static void visualize(int[][] adjacencyMatrix, int nodeCount) {
       JFrame frame = new JFrame("Network Graph Visualization");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(500, 500);
       GraphVisualizer graphVisualizer = new GraphVisualizer(adjacencyMatrix, nodeCount);
       frame.add(graphVisualizer);
       frame.setVisible(true);
   }
}
