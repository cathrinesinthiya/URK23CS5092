package salon_appointment;

import java.sql.*;
import javax.swing.*;
import java.awt.*;

public class CustomerData {
    public static void fetchCustomers() throws java.sql.SQLException {
        String query = "SELECT * FROM NewCustomers";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Create a StringBuilder to display customer data in the GUI (JTextArea)
            StringBuilder customerInfo = new StringBuilder();
            while (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String registrationDate = rs.getString("registration_date");
                
                customerInfo.append(name).append(" | ").append(phone).append(" | ")
                            .append(email).append(" | ").append(address).append(" | ")
                            .append(registrationDate).append("\n");
            }

            // Create a JTextArea to display the customer data
            JTextArea textArea = new JTextArea(customerInfo.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Arial", Font.PLAIN, 14));  // Set font size and type
            textArea.setBackground(new Color(230, 230, 255)); // Set background color of the text area
            textArea.setForeground(new Color(0, 0, 0));  // Set text color (black)

            // Create a JScrollPane to make the text area scrollable
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400)); // Set the size of the scrollable area

            // Create a JFrame to display the customer data
            JFrame frame = new JFrame("Customer Data");
            frame.getContentPane().setBackground(new Color(255, 228, 225));  // Set background color of the frame
            frame.add(scrollPane);
            frame.setSize(600, 500);  // Set window size
            frame.setLocationRelativeTo(null);  // Center the frame on the screen
            frame.setVisible(true);

        }
    }
} 
