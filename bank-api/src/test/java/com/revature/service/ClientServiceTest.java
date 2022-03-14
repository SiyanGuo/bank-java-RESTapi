package com.revature.service;

import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientServiceTest {

    //GET - Positive
    @Test
    public void testGetAllClients() throws SQLException {

        ClientDao mockedObject = mock(ClientDao.class);

        List<Client> fakeClients = new ArrayList<>();
        fakeClients.add(new Client(1, "Ada", "Wu", 55, "647-999-0000"));
        fakeClients.add(new Client(2, "Bob", "Doe", 15, "647-888-0000"));

        when(mockedObject.getAllClients()).thenReturn(fakeClients);
        ClientService clientService = new ClientService(mockedObject);

        List<Client> actual = clientService.getAllClients();

        List<Client> expected = new ArrayList<>(fakeClients);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetClientById_positiveTest() throws SQLException, ClientNotFoundException {

        ClientDao mockDao = mock(ClientDao.class);

        when(mockDao.getClientById(eq(9))).thenReturn(
                new Client(9, "UserTest", "Test", 26, "647-100-0000")
        );

        ClientService clientService = new ClientService(mockDao);

        Client actual = clientService.getClientById("9");

        Client expected = new Client(9, "UserTest", "Test", 26,"647-100-0000" );
        Assertions.assertEquals(expected, actual);
    }

    //GET - Negative
    @Test
    public void test_getClientById_clientDoesNotExist() throws SQLException, ClientNotFoundException {

        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.getClientById("100");
        });
    }

    @Test
    public void test_getClientById_invalidId() throws SQLException, ClientNotFoundException {

        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        try {
            clientService.getClientById("xyz");
            fail();
        } catch(IllegalArgumentException e) {
            String expectedMessage = "Client id must be a valid number";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_getClientById_sqlExceptionFromDao() throws SQLException {

        ClientDao mockDao = mock(ClientDao.class);

        when(mockDao.getClientById(anyInt())).thenThrow(SQLException.class);

        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(SQLException.class, () -> {
            clientService.getClientById("100");
        });
    }

    //POST - Positive
    @Test
    public void test_addClient_positiveTest() throws SQLException{
        ClientDao mockDao = mock(ClientDao.class);

        when(mockDao.addClient(eq(new Client(7, "Serena", "Guo", 22, "647-000-0001"))))
                .thenReturn(new Client(7, "Serena", "Guo", 22, "647-000-0001"));

        ClientService clientService = new ClientService(mockDao);

        Client actual = clientService.addClient(new Client(7, "Serena", "Guo", 22, "647-000-0001"));
        Client expected = new Client(7, "Serena", "Guo", 22, "647-000-0001");
        Assertions.assertEquals(expected, actual);
    }

    //Post - Negative
    @Test
    public void test_addClient_withLeadingAndTrailingNamesAndPhoneNumber() throws SQLException {
        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        when(mockDao.addClient(eq(new Client(0, "Serena", "Guo", 22, "647-000-0001"))))
                .thenReturn(new Client(0, "Serena", "Guo", 22, "647-000-0001"));

        Client actual = clientService.addClient(
                new Client(0, "   Serena  ", "   Guo  ", 22, "   647-000-0001   ")
        );

        Client expected = new Client(0, "Serena", "Guo", 22, "647-000-0001");
        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void test_addClient_nonAlphabeticalCharactersInFirstName() throws SQLException {

        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.addClient(new Client(0, "Serena123", "Guo", 22, "647-000-0001"));
        });
    }

    @Test
    public void test_addClient_nonAlphabeticalCharactersInLastName() {

        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.addClient(new Client(0, "Serena", "Guo56", 22, "647-000-0001"));
        });
    }

    @Test
    public void test_addClient_negativeAge() {

        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.addClient(new Client(0, "Serena", "Guo", -10, "647-000-0001"));
        });
    }

    @Test
    public void test_addClient_phoneNumber(){
        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.addClient(new Client(0, "Serena", "Guo", -10, "647-00-0001"));
        });
    }

    //PUT - Positive
    @Test
    public void test_editClientById() throws SQLException, ClientNotFoundException {
        ClientDao mockDao = mock(ClientDao.class);
        Client fakeClient = new Client(7, "Serena", "Guo", 22, "647-000-0001");
        when(mockDao.getClientById(anyInt())).thenReturn(new Client());
        when(mockDao.updateClient(fakeClient))
                .thenReturn(new Client(7, "Serena", "Guo", 22, "647-000-0001"));

        ClientService clientService = new ClientService(mockDao);
        Client actual = clientService.editClient("7", fakeClient);
        Client expected = new Client(7, "Serena", "Guo", 22, "647-000-0001");
        Assertions.assertEquals(expected, actual);

    }

    //PUT - Negative
    @Test
    public void test_editClientById_clientDoesNotExist() throws SQLException, ClientNotFoundException {

        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);
        Client mockClient = mock(Client.class);
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.editClient("100", mockClient);
        });
    }

    @Test
    public void test_editClientById_invalidId() throws SQLException, ClientNotFoundException {

        ClientDao mockDao = mock(ClientDao.class);
        Client mockClient = mock(Client.class);
        ClientService clientService = new ClientService(mockDao);

        try {
            clientService.editClient("xyz", mockClient);
            fail();
        } catch(IllegalArgumentException e) {
            String expectedMessage = "Client id must be a valid number";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_editClientById_sqlExceptionFromDao() throws SQLException {

        ClientDao mockDao = mock(ClientDao.class);
        Client mockClient = mock(Client.class);
        when(mockDao.getClientById(anyInt())).thenThrow(SQLException.class);

        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(SQLException.class, () -> {
            clientService.editClient("100", mockClient);
        });
    }

    //DELETE - Positive
    @Test
    public void test_deleteClientById() throws SQLException, ClientNotFoundException {
        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        when(mockDao.getClientById(eq(1))).thenReturn(new Client());
        when(mockDao.deleteClientById(anyInt())).thenReturn(true);

        boolean actual = clientService.deleteClient("1");
        Assertions.assertTrue(true);
    }

    //DELETE - Negative
    @Test
    public void test_deleteClientById_clientDoesNotExist() throws SQLException, ClientNotFoundException {

        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.deleteClient("100");
        });
    }

    @Test
    public void test_deleteClientById_invalidId() throws SQLException, ClientNotFoundException {

        ClientDao mockDao = mock(ClientDao.class);
        ClientService clientService = new ClientService(mockDao);

        try {
            clientService.deleteClient("xyz");
            fail();
        } catch(IllegalArgumentException e) {
            String expectedMessage = "Client id must be a valid number";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_deleteClientById_sqlExceptionFromDao() throws SQLException {

        ClientDao mockDao = mock(ClientDao.class);
        when(mockDao.getClientById(anyInt())).thenThrow(SQLException.class);

        ClientService clientService = new ClientService(mockDao);

        Assertions.assertThrows(SQLException.class, () -> {
            clientService.deleteClient("100");
        });
    }

}
