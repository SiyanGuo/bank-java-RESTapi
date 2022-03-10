package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Account;
import com.revature.model.Client;

import javax.security.auth.login.AccountNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class AccountService {

    private ClientDao clientDao;
    private AccountDao accountDao;

    public AccountService() {
        this.clientDao = new ClientDao();
        this.accountDao = new AccountDao();
    }

    public AccountService(AccountDao mockedAccountDao, ClientDao mockedClientDao) {
        this.accountDao = mockedAccountDao;
        this.clientDao = mockedClientDao;
    }

    public List<Account> getAccountsByClient(String id) throws ClientNotFoundException, SQLException {

       try {
           int clientId = Integer.parseInt(id);
           Client client = clientDao.getClientById(clientId);
           if (client == null) {
               throw new ClientNotFoundException("Client with id " + clientId + " was not found");
           }
           return this.accountDao.getAccountsByClient(clientId);
       } catch (NumberFormatException e){
            throw new IllegalArgumentException("Client ID must be a valid number");
        }
    };

    public Account getAccountById(String idClient, String idAccount) throws ClientNotFoundException, SQLException, AccountNotFoundException {
        try {
            int clientId = Integer.parseInt(idClient);
            int accountId = Integer.parseInt(idAccount);

            //check if client exists
            Client client = clientDao.getClientById(clientId);
            if (client == null) {
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }

            //check if account exists
            Account account = accountDao.getAccountById(clientId, accountId);
            if (account == null) {
                throw new AccountNotFoundException("Account with id " + accountId + " was not found");
            }

            return account;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Client and/or Account ID must be a valid number");
        }
    };

    public Account addAccount(Account account, String clientId) throws SQLException, ClientNotFoundException {
        try {
            validateAccountInformation(account);
            int id = Integer.parseInt(clientId);

            Client client = clientDao.getClientById(id);
            if (client == null) {
                throw new ClientNotFoundException("Client with id " + id + " was not found");
            }

            return this.accountDao.addAccount(account, id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client ID must be a valid number");
        }
    };


    public boolean updateAccount (Account account, String idClient, String idAccount) throws ClientNotFoundException, SQLException{
        try {
            //validateAccountInformation(account);
            int clientId = Integer.parseInt(idClient);
            int accountId = Integer.parseInt(idAccount);

            Client client = clientDao.getClientById(clientId);
            if (client == null) {
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }

            return this.accountDao.updateAccount(account, clientId , accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client and/or Account ID must be a valid number");
        }
    };

    private void validateAccountInformation(Account a) {
        a.setAccountType(a.getAccountType().trim());
        a.setAccountNumber(a.getAccountNumber().trim());
        a.setDateOpened(a.getDateOpened().trim());

//        if (!a.getDateOpened().matches("/^d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/")) {
//            throw new IllegalArgumentException("Date format must be YYYY-MM-DD. Date input was " + a.getDateOpened());
//        }
//
//        if (!a.getAccountNumber().matches("/d{8}/")) {
//            throw new IllegalArgumentException("Account Number must be 8 digits. Account Number input was " + a.getAccountNumber());
//        }

    };

}