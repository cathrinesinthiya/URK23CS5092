package salon_appointment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class AppointmentBooking {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Salon Appointment Booking");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Customer Name:");
        JTextField nameField = new JTextField();

        JLabel serviceLabel = new JLabel("Service:");
        JComboBox<String> serviceBox = new JComboBox<>(new String[]{"Haircut", "Facial", "Massage"});

        JLabel staffLabel = new JLabel("Staff:");
        JComboBox<String> staffBox = new JComboBox<>(new String[]{"Staff 1", "Staff 2", "Staff 3"});

        JLabel dateLabel = new JLabel("Date (DD/MM/YYYY):");
        JTextField dateField = new JTextField("DD/MM/YYYY");

        JButton bookButton = new JButton("Book Appointment");
        JButton viewButton = new JButton("View Appointments");

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(serviceLabel);
        frame.add(serviceBox);
        frame.add(staffLabel);
        frame.add(staffBox);
        frame.add(dateLabel);
        frame.add(dateField);
        frame.add(bookButton);
        frame.add(viewButton);

        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String service = (String) serviceBox.getSelectedItem();
                String staff = (String) staffBox.getSelectedItem();
                String date = dateField.getText().trim();

                if (!isValidDate(date)) {
                    JOptionPane.showMessageDialog(null, "Please enter the date in DD/MM/YYYY format.");
                    return;
                }

                String formattedDate = convertDateToDBFormat(date);
                if (formattedDate == null) return;

                try {
                    addAppointment(name, service, staff, formattedDate);
                    JOptionPane.showMessageDialog(null, "Appointment Booked! Please be on time.");
                    frame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error while booking: " + ex.getMessage());
                } catch (java.sql.SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM Appointments")) {

                    StringBuilder appointments = new StringBuilder();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("customer_name");
                        String service = rs.getString("service");
                        String staff = rs.getString("staff");
                        Date date = rs.getDate("appointment_date");

                        appointments.append("ID: ").append(id)
                                .append(", Name: ").append(name)
                                .append(", Service: ").append(service)
                                .append(", Staff: ").append(staff)
                                .append(", Date: ").append(date)
                                .append("\n");
                    }

                    if (appointments.length() == 0) {
                        JOptionPane.showMessageDialog(null, "No appointments found.");
                    } else {
                        JTextArea textArea = new JTextArea(appointments.toString());
                        textArea.setEditable(false);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(400, 300));
                        JOptionPane.showMessageDialog(null, scrollPane, "All Appointments", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (java.sql.SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        frame.setVisible(true);
    }

    public static boolean isValidDate(String date) {
        return date.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    public static String convertDateToDBFormat(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            inputFormat.setLenient(false);
            java.util.Date parsedDate = inputFormat.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            return sqlDate.toString(); // YYYY-MM-DD
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid date format. Please use DD/MM/YYYY.");
            return null;
        }
    }

    public static void addAppointment(String name, String service, String staff, String date) throws SQLException, java.sql.SQLException {
        String query = "INSERT INTO Appointments (customer_name, service, staff, appointment_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, service);
            stmt.setString(3, staff);
            stmt.setString(4, date);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                String confirmation = String.format("Appointment Booked:\nName: %s\nService: %s\nStaff: %s\nDate: %s",
                        name, service, staff, date);
                JOptionPane.showMessageDialog(null, confirmation);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to book appointment.");
            }
        }
    }
}
