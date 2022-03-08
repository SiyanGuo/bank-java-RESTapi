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
            String expectedMessage = "Client ID must be a valid number";
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
}
