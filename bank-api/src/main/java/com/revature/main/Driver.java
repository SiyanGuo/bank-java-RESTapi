package com.revature.main;

import com.revature.dao.ClientDao;
import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.sql.SQLException;

public class Driver {
    public static void main(String[] args) {

        ClientDao clientDao = new ClientDao();
        Client client = new Client(7,"Harry", "Jo", 60, "647-100-1019");
        try {
//          System.out.println(clientDao.getAllClients());
//          System.out.println(clientDao.getClientById(1));
//          clientDao.deleteClientById(2);
//          clientDao.addClient(client);
//          clientDao.updateClient(client);
            System.out.println(clientDao.getAllClients());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
