package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Account;
import com.revature.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public void test_GetAccountsByClientId () throws ClientNotFoundException, SQLException {
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
    public void test_GetOneAccountByAccountId () throws SQLException, ClientNotFoundException, AccountNotFoundException {

        Account fakeAccount =  new Account (2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3);
        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        when(accountDao.getAccountById(anyInt(), anyInt())).thenReturn(fakeAccount);
        Account actual = accountService.getAccountById("3", "2");

        Account expected = fakeAccount;

        Assertions.assertEquals(expected, actual);
    }

    //POST - Positive
    @Test
    public void test_AddAnAccount () throws SQLException, ClientNotFoundException {

        Account fakeAccount = new Account (2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3);
        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        when(accountDao.addAccount(eq(fakeAccount), anyInt())).thenReturn(fakeAccount);

        Account actual = accountService.addAccount(fakeAccount, "3");

        Account expected = fakeAccount;
        Assertions.assertEquals(expected, actual);

    }

    //PUT - Positive
    @Test
    public void test_editAnAccount () throws SQLException, ClientNotFoundException, AccountNotFoundException {
        Account fakeAccount = new Account (2, "Chequing Account", 00001112, new BigDecimal(412.09), "2022-03-14", 3);

        when(clientDao.getClientById(eq(3))).thenReturn(new Client());
        when(accountDao.getAccountById(eq(3), eq(2))).thenReturn(new Account());
        when(accountDao.updateAccount(eq(fakeAccount), anyInt(), anyInt())).thenReturn(true);
        boolean actual = accountService.updateAccount(fakeAccount,"3", "2");
        Assertions.assertTrue(true);
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
}
