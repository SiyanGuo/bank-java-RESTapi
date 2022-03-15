package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.AccountNotFoundException;
import com.revature.model.Account;
import com.revature.model.Client;

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
            throw new IllegalArgumentException("Client id must be a valid number");
        }
    };

    public List<Account> getAccountsByGreaterThan(String id, String greaterThan) throws ClientNotFoundException, SQLException, AccountNotFoundException {

        try {
            int clientId = Integer.parseInt(id);
            int amountGreater = Integer.parseInt(greaterThan);

            Client client = clientDao.getClientById(clientId);
            if (client == null) {
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }

            List<Account> accounts = accountDao.getAccountsByGreaterThan(clientId,amountGreater);
            if(accounts.isEmpty()) {
                throw new AccountNotFoundException("Account was not found that balance is greater than " + greaterThan + " for client with id " + clientId);
            }

            return this.accountDao.getAccountsByGreaterThan(clientId, amountGreater);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Parameters must be valid number");
        }
    };

    public List<Account> getAccountsByLessThan(String id, String lessThan) throws ClientNotFoundException,AccountNotFoundException, SQLException {

        try {
            int clientId = Integer.parseInt(id);
            int amountLess = Integer.parseInt(lessThan);

            Client client = clientDao.getClientById(clientId);
            if (client == null) {
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }

            List<Account> accounts = accountDao.getAccountsByLessThan(clientId, amountLess);
            if(accounts.isEmpty()) {
                 throw new AccountNotFoundException("Account was not found that balance is less than " + lessThan + " for client with id " + clientId);
            }

            return this.accountDao.getAccountsByLessThan(clientId, amountLess);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Parameters must be valid number");
        }
    };

    public List<Account> getAccountsByGreatAndLessThan(String id, String greaterThan, String lessThan) throws ClientNotFoundException, SQLException, AccountNotFoundException {

        try {
            int clientId = Integer.parseInt(id);
            int max = Integer.parseInt(lessThan);
            int min = Integer.parseInt(greaterThan);

            if (min >= max){
                throw new IllegalArgumentException( "Amount less than " + min + " and greater than " + max + " doesn't exist" );
            }

            Client client = clientDao.getClientById(clientId);
            if (client == null) {
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }

            List<Account> accounts = accountDao.getAccountsByGreatAndLessThan(clientId, min, max);
            if(accounts.isEmpty()) {
                throw new AccountNotFoundException("Account was not found that balance is less than " + max + " and greater than " + min + " for client with id " + clientId);
            }

            return this.accountDao.getAccountsByGreatAndLessThan(clientId, min, max);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Parameters must be valid number");
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
                throw new AccountNotFoundException("Account with id " + accountId + " of client with id " + clientId + " was not found");
            }

            return account;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Client and/or Account id must be a valid number");
        }
    };

    public Account addAccount(Account a, String clientId) throws SQLException, ClientNotFoundException {
        try {
            a.setAccountType(a.getAccountType().trim());
            a.setDateOpened(a.getDateOpened().trim());

            int id = Integer.parseInt(clientId);

            Client client = clientDao.getClientById(id);
            if (client == null) {
                throw new ClientNotFoundException("Client with id " + id + " was not found");
            }

            if (!a.getDateOpened().matches("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")) {
                throw new IllegalArgumentException("Date format must be YYYY-MM-DD. Date input was " + a.getDateOpened());
            }

            if ( !a.getAccountType().equals("Chequing Account") && !a.getAccountType().equals("Savings Account")) {
                throw new IllegalArgumentException("Account type must be either Chequing Account or Savings Account. Account type input was " + a.getAccountType());
            }

            return this.accountDao.addAccount(a, id);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client id must be a valid number");
        }
    };

    public boolean updateAccount (Account account, String idClient, String idAccount) throws ClientNotFoundException, AccountNotFoundException, SQLException{
        try {

            int clientId = Integer.parseInt(idClient);
            int accountId = Integer.parseInt(idAccount);

            Client client = clientDao.getClientById(clientId);
            if (client == null) {
                throw new ClientNotFoundException("User is trying to edit a client that does not exist. Client with id " + clientId + " was not found");
            }

            Account a = accountDao.getAccountById(clientId, accountId);
            if (a == null) {
                throw new AccountNotFoundException("User is trying to edit an account that does not exist. Account with id " + accountId + " of client with id " + clientId + " was not found");
            }

            return this.accountDao.updateAccount(account, clientId , accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client and/or Account id must be a valid number");
        }
    };

    public boolean deleteAccount(String idClient, String idAccount) throws ClientNotFoundException, SQLException, AccountNotFoundException {
        try {
            int clientId = Integer.parseInt(idClient);
            int accountId = Integer.parseInt(idAccount);

            if(clientDao.getClientById(clientId) == null ){
                throw new ClientNotFoundException("User is trying to delete a client that does not exist. Client with id " + clientId
                        + " was not found");
            };

            if(accountDao.getAccountById(clientId, accountId) == null ){
                throw new AccountNotFoundException("User is trying to delete an account that does not exist. Account with id " + accountId
                        + " of client with id " + clientId + " was not found");
            };

            boolean account = accountDao.deleteAccount(clientId, accountId);
            return account;

        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Client and/or Account id must be a valid number");
        }
    };
}