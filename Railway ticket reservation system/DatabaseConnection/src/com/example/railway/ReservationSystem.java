package com.example.railway;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationSystem extends JFrame {
    private JTextField nameField;
    private JTextField trainField;
    private JTextField seatField;
    private JTextField dateField;
    private JTextArea outputArea;

    public ReservationSystem() {
        setTitle("Railway Ticket Reservation System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Passenger Name:");
        nameField = new JTextField();
        JLabel trainLabel = new JLabel("Train Number:");
        trainField = new JTextField();
        JLabel seatLabel = new JLabel("Seat Number:");
        seatField = new JTextField();
        JLabel dateLabel = new JLabel("Journey Date (YYYY-MM-DD):");
        dateField = new JTextField();

        JButton bookButton = new JButton("Book Ticket");
        JButton viewButton = new JButton("View Tickets");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(trainLabel);
        panel.add(trainField);
        panel.add(seatLabel);
        panel.add(seatField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(bookButton);
        panel.add(viewButton);

        add(panel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String train = trainField.getText();
                String seat = seatField.getText();
                String date = dateField.getText();
                bookTicket(name, train, seat, date);
                outputArea.append("Ticket booked for " + name + " on train " + train + " for seat " + seat + " on " + date + "\n");
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTickets();
            }
        });
    }

    public void bookTicket(String passengerName, String trainNumber, String seatNumber, String journeyDate) {
        String sql = "INSERT INTO tickets (passenger_name, train_number, seat_number, journey_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, passengerName);
            pstmt.setString(2, trainNumber);
            pstmt.setString(3, seatNumber);
            pstmt.setDate(4, java.sql.Date.valueOf(journeyDate));
            pstmt.executeUpdate();
            System.out.println("Ticket booked successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewTickets() {
        String sql = "SELECT * FROM tickets";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            StringBuilder builder = new StringBuilder();
            while (rs.next()) {
                builder.append("ID: ").append(rs.getInt("id"))
                        .append(", Passenger: ").append(rs.getString("passenger_name"))
                        .append(", Train: ").append(rs.getString("train_number"))
                        .append(", Seat: ").append(rs.getString("seat_number"))
                        .append(", Date: ").append(rs.getDate("journey_date")).append("\n");
            }
            outputArea.setText(builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ReservationSystem().setVisible(true);
            }
        });
    }
}
