package mini_proj_dsa;

import java.util.*;

public class Computer_Network {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
            // Display menu options
            System.out.println("\nNetwork Simulator Menu:");
            System.out.println("1.Visual Representation of Network Topology ");
            System.out.println("2.ASCII Representation of Network Topology");
            System.out.println("3.Optimized Route in a Network ");
            System.out.println("4.Calculate Fault Tolerance");
            System.out.println("5.Performance Analysis");
            System.out.println("6.Visual Representation of disabled nodes");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
               
            }

            switch (choice) {
                case 1:
                    Network_Visual.main(new String[0]);
                    break;
                case 2:
                    Network_ASCII.main(new String[0]);
                    break;
                case 3:
                    MSTNetworkGUI.main(new String[0]);
                    break;
                case 4:
                    Fault_Tolerance.main(new String[0]);
                    break;
                case 5:
                    Performance_Analysis.main(new String[0]);
                    break;
                case 6:
                    Disabled_Visual.main(new String[0]);
                    break;
                case 7:
                    scanner.close();
                    return;
                default:
                    System.out.println("Not a valid input");
            }
        }
    }

