package com.revature.service;

import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {

    private ClientDao clientDao;

    public ClientService() {
        this.clientDao = new ClientDao();
    }

    public ClientService(ClientDao mockedObject) {
        this.clientDao = mockedObject;
    }

    public List<Client> getAllClients() throws SQLException {
        return this.clientDao.getAllClients();
    }

    public Client getClientById(String id) throws SQLException, ClientNotFoundException {
        try {

            int clientId = Integer.parseInt(id);
            Client client = clientDao.getClientById(clientId);

            if(client == null){
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }
            return client;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Client ID must be a valid number");
        }
    }
}
