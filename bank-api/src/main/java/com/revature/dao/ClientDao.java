package com.revature.dao;

import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {


    //Create - add a new client
    public Client addClient(Client client) throws SQLException {

        try (Connection con = ConnectionUtility.getConnection()){
            String sql = "INSERT INTO clients (first_name, last_name, age, phone_number) VALUES (?, ?, ?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setInt(3, client.getAge());
            pstmt.setString(4, client.getPhoneNumber());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int generatedId = rs.getInt(1); // 1st column of the ResultSet

            return new Client(generatedId, client.getFirstName(), client.getLastName(), client.getAge(), client.getPhoneNumber());

        }
    }

    //Read - get all clients
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()){
            String sql = "SELECT * FROM clients";
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");
                String phoneNumber = rs.getString("phone_number");

                clients.add(new Client(id, firstName, lastName, age, phoneNumber));
            }
        }
    return clients;
    }

    public Client getClientById(int id) throws SQLException{
        try (Connection con = ConnectionUtility.getConnection()){
            String sql = "SELECT * FROM clients WHERE id =?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");
                String phoneNumber = rs.getString("phone_number");
                return new Client(id, firstName, lastName, age, phoneNumber);
            }
             return null; //why not here?
        }
       // return null;
    }


    //Update - update an existing client
    public Client updateClient(Client client) throws SQLException{
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE clients " +
                    "SET first_name = ?, " +
                    "last_name = ?, " +
                    "age = ? " +
                    "phone_number = ? " +
                    "WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setInt(3, client.getAge());
            pstmt.setString(4, client.getPhoneNumber());
            pstmt.setInt(5, client.getId());

            pstmt.executeUpdate();
        }
        return client;
    }

    //Delete - remove an existing client
    public boolean deleteClientById(int id) throws SQLException{
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM clients WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, id);

            int numberOfRecordsDeleted = pstmt.executeUpdate();

            if (numberOfRecordsDeleted == 1) {
                return true;
            }
        }
        return false;
    }

}
