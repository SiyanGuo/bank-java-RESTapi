package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.AccountNotFoundException;
import com.revature.model.Account;
import com.revature.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private AccountDao accountDao;
    private ClientDao clientDao;
    private AccountService accountService;

@BeforeEach
    public void setup(){
    accountDao = mock(AccountDao.class);
    clientDao = mock(ClientDao.class);
    accountService = new AccountService(accountDao, clientDao);
}

    //GET - Positive
    @Test
    public void test_getAccountsByClientId () throws SQLException, ClientNotFoundException {
        List<Account> fakeAccounts = new ArrayList<>();

        fakeAccounts.add(new Account(1, "Savings Account", 00001111, new BigDecimal(1994.09), "2022-03-13", 3));
        fakeAccounts.add(new Account(2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3));

        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        when(accountDao.getAccountsByClient(eq(3))).thenReturn(fakeAccounts);
        List<Account> actual = accountService.getAccountsByClient("3");

        List<Account> expected = new ArrayList<>(fakeAccounts);
        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void test_getOneAccountByAccountId () throws SQLException, ClientNotFoundException, AccountNotFoundException {

        Account fakeAccount =  new Account (2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3);
        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        when(accountDao.getAccountById(anyInt(), anyInt())).thenReturn(fakeAccount);
        Account actual = accountService.getAccountById("3", "2");

        Account expected = fakeAccount;

        Assertions.assertEquals(expected, actual);
    }

    //GET - Negative
    @Test
    public void test_getAccountByClientId_clientDoesNotExist() throws SQLException, ClientNotFoundException {

        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            accountService.getAccountsByClient("100");
        });
    }

    @Test
    public void test_getAccountByClientId_invalidId () throws ClientNotFoundException, SQLException {
        try{
            accountService.getAccountsByClient("go");
            fail();
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Client id must be a valid number";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_getAccountByClientId_sqlExceptionFromDao() throws SQLException {
        when(clientDao.getClientById(anyInt())).thenReturn(new Client());

        when(accountDao.getAccountsByClient(anyInt())).thenThrow(SQLException.class);
        Assertions.assertThrows(SQLException.class, () -> {
            accountService.getAccountsByClient("100");
        });
    }

    //POST - Positive
    @Test
    public void test_addAnAccount () throws SQLException, ClientNotFoundException {

        Account fakeAccount = new Account (2, "Savings Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3);
        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        when(accountDao.addAccount(eq(fakeAccount), anyInt())).thenReturn(fakeAccount);

        Account actual = accountService.addAccount(fakeAccount, "3");

        Account expected = fakeAccount;
        Assertions.assertEquals(expected, actual);

    }

    //POST - Negative
    @Test
    public void test_addAccount_withLeadingAndTrailingAccountTypeAndDateOpened() throws SQLException, AccountNotFoundException, ClientNotFoundException {
        Account fakeAccount = new Account (2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3);

        when(clientDao.getClientById(anyInt())).thenReturn(new Client());
        when(accountDao.addAccount(eq(fakeAccount), anyInt()))
                .thenReturn(new Account (2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3));

        Account actual = accountService.addAccount(
                new Account (2, "   Chequing Account   ", 00001112, new BigDecimal(412.09), "   2022-03-14   ", 3), "3"
        );

        Account expected = fakeAccount;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_addAccount_accountType() throws SQLException, ClientNotFoundException {

        try {
            when(clientDao.getClientById(anyInt())).thenReturn(new Client());
            accountService.addAccount(new Account (2, "Chequing Acco", 00001112, new BigDecimal(412.09), "2022-3-4", 3), "3");
            fail();
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Account type must be either Chequing Account or Savings Account. Account type input was Chequing Acco";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);

        }
    }

    @Test
    public void test_addAccount_dateFormat() throws SQLException {
        when(clientDao.getClientById(anyInt())).thenReturn(new Client());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            accountService.addAccount(new Account (2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-3-4", 3), "3");
        });
    }

    //PUT - Positive
    @Test
    public void test_editAccount () throws SQLException, ClientNotFoundException, AccountNotFoundException {
        Account fakeAccount = new Account (2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3);

        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        when(accountDao.getAccountById(eq(3), eq(2))).thenReturn(new Account());
        when(accountDao.updateAccount(eq(fakeAccount), anyInt(), anyInt())).thenReturn(true);
        boolean actual = accountService.updateAccount(fakeAccount,"3", "2");
        Assertions.assertTrue(true);
    }

    //PUT - Negative
    @Test
    public void test_editAccount_accountDoesNotExist() throws SQLException {
        Account mockAccount = mock(Account.class);
        when(clientDao.getClientById(anyInt())).thenReturn(new Client());

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.updateAccount(mockAccount, "1", "100");
        });
    }

    @Test
    public void test_editAccount_invalidId() throws ClientNotFoundException, SQLException, AccountNotFoundException {
        Account mockAccount = mock(Account.class);

        try{
            when(clientDao.getClientById(anyInt())).thenReturn(new Client());
            accountService.updateAccount(mockAccount, "1", "abc");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Client and/or Account id must be a valid number";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_editAccount_sqlException() throws SQLException {
        Account mockAccount = mock(Account.class);

        when(clientDao.getClientById(anyInt())).thenThrow(SQLException.class);
        Assertions.assertThrows(SQLException.class, () -> {
            accountService.updateAccount(mockAccount, "1", "1");
        });
    }

    //DELETE - Positive
    @Test
    public void test_deleteAccount() throws SQLException, ClientNotFoundException, AccountNotFoundException {

        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        when(accountDao.getAccountById(eq(3), eq(2))).thenReturn(new Account());
        when(accountDao.deleteAccount(anyInt(), anyInt())).thenReturn(true);
        boolean actual = accountService.deleteAccount("3", "2");
        Assertions.assertTrue(true);
    }

    //DELETE - negative
    @Test
    public void test_deleteAccount_accountDoesNotExist() throws SQLException {
        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.deleteAccount("3", "100");
        });
    }

    @Test
    public void test_deleteAccount_invalidId() throws ClientNotFoundException, SQLException, AccountNotFoundException {
        try {
            accountService.deleteAccount("3", "abc");
            fail();
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Client and/or Account id must be a valid number";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_deleteAccount_sqlException() throws SQLException {

        when(clientDao.getClientById(anyInt())).thenThrow(SQLException.class);

        Assertions.assertThrows(SQLException.class, () -> {
           accountService.deleteAccount("3", "2");
        });
    }
}
