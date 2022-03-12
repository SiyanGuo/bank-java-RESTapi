package com.revature.controller;

import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.AccountService;
import com.revature.service.ClientService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.List;


public class ClientController implements Controller {
    private ClientService clientService;
    private AccountService accountService;

    public ClientController(){
        this.clientService = new ClientService();
        this.accountService = new AccountService();
    };

    private Handler getAllClients = (ctx) ->{
        List<Client> clients = clientService.getAllClients();
        ctx.json(clients);
    };

    private Handler getClientById = (ctx) ->{
        String id = ctx.pathParam("clientId");
        Client client = clientService.getClientById(id);
        ctx.json(client);
    };

    private Handler addClient = (ctx) -> {
        Client clientToAdd = ctx.bodyAsClass(Client.class);
        Client client = clientService.addClient(clientToAdd);

        ctx.status(201);
        ctx.json(client);
    };

    private Handler editClient = (ctx) ->{
        String id = ctx.pathParam("clientId");
        Client clientToEdit = ctx.bodyAsClass(Client.class);
        Client client = clientService.editClient(id, clientToEdit);

        ctx.status(204);
        ctx.json("Client information has been updated");
    };

    private Handler deleteClient = (ctx) -> {
        String id = ctx.pathParam("clientId");
        boolean client = clientService.deleteClient(id);

        ctx.status(204);
        ctx.json("Client with id " + id + " has been deleted");
    };

    private Handler getAccountsByClient = (ctx) -> {

        String greaterThan = ctx.queryParam("amountGreaterThan");
        String lessThan = ctx.queryParam("amountLessThan");
        String id = ctx.pathParam("clientId");
        List<Account> accounts ;
        if (lessThan!=null && greaterThan!=null ) {
            accounts = accountService.getAccountsByGreatAndLessThan(id, greaterThan, lessThan);
        } else if (lessThan!=null ) {
            accounts = accountService.getAccountsByLessThan(id, lessThan);
        } else if (greaterThan!=null ){
            accounts = accountService.getAccountsByGreaterThan(id, greaterThan);
        } else {
            accounts = accountService.getAccountsByClient(id);
        }
        if (accounts.isEmpty()) {
            ctx.json("Account was not found");
        } else {
            ctx.json(accounts);
        }

    };

    private Handler getAccountById = (ctx) -> {
        String clientId = ctx.pathParam("clientId");
        String accountId = ctx.pathParam("accountId");
        Account account = accountService.getAccountById(clientId, accountId);

        ctx.json(account);
    };

    private Handler addAccount = (ctx) ->{
        String clientId = ctx.pathParam("clientId");
        Account accountToAdd = ctx.bodyAsClass(Account.class);
        Account account = accountService.addAccount(accountToAdd, clientId);

        ctx.status(201);
        ctx.json(account);
    };

    private Handler updateAccount = (ctx) -> {
        String clientId = ctx.pathParam("clientId");
        String accountId = ctx.pathParam("accountId");
        Account accountToEdit = ctx.bodyAsClass(Account.class);
        boolean account = accountService.updateAccount(accountToEdit, clientId, accountId);

        ctx.status(204);
        ctx.json("Account information has been updated");
    };

    private Handler deleteAccount = (ctx) -> {
        String clientId = ctx.pathParam("clientId");
        String accountId = ctx.pathParam("accountId");
        boolean account = accountService.deleteAccount(clientId, accountId);

        ctx.status(204);
        ctx.json("Account with id " + accountId + " of client with id " + clientId + " has been deleted");

    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/clients", getAllClients);
        app.get("/clients/{clientId}", getClientById);
        app.post("/clients", addClient);
        app.put("/clients/{clientId}", editClient);
        app.delete("/clients/{clientId}", deleteClient);
        app.post("/clients/{clientId}/accounts", addAccount);
        app.get("/clients/{clientId}/accounts", getAccountsByClient);
        app.get("/clients/{clientId}/accounts/{accountId}", getAccountById);
        app.put("/clients/{clientId}/accounts/{accountId}", updateAccount);
        app.delete("/clients/{clientId}/accounts/{accountId}", deleteAccount);
    }
}
