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

            if (client == null) {
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }
            return client;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client ID must be a valid number");
        }
    }

    public Client addClient(Client c) throws SQLException {

        validateClientInformation(c);
        return this.clientDao.addClient(c);
    }

    public Client editClient(String id, Client c) throws SQLException, ClientNotFoundException {

       try {
           int clientId = Integer.parseInt(id);

           if (clientDao.getClientById(clientId) == null) {
               throw new ClientNotFoundException("User is trying to edit a client that does not exist. Client with id " + clientId
                       + " was not found");
           }
           validateClientInformation(c);

           c.setId(clientId);
           return this.clientDao.updateClient(c);
       } catch (NumberFormatException e){
           throw new IllegalArgumentException("Id provided for student must be a valid int");
       }

    };

    public boolean deleteClient(String id) throws SQLException, ClientNotFoundException{
        try {
            int clientId = Integer.parseInt(id);

            if(clientDao.getClientById(clientId) == null ){
                throw new ClientNotFoundException("User is trying to delete a client that does not exist. Client with id " + clientId
                        + " was not found");
            };
            boolean client = clientDao.deleteClientById(clientId);
            return client;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Id provided for student must be a valid int");
        }
    };

    public void validateClientInformation(Client c) {
        c.setFirstName(c.getFirstName().trim());
        c.setLastName(c.getLastName().trim());

        if (!c.getFirstName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("First name must only have alphabetical characters. First name input was " + c.getFirstName());
        }
        if (!c.getLastName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Last name must only have alphabetical characters. Last name input was " + c.getLastName());
        }
        if (c.getAge() < 0) {
            throw new IllegalArgumentException("Adding a client with age < 0 is not valid. Age provided was " + c.getAge());
        }
        ;
    }
}