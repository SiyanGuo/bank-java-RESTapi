package com.revature.controller;

import com.revature.model.Client;
import com.revature.service.ClientService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;


public class ClientController implements Controller {
    private ClientService clientService;

    public ClientController(){
        this.clientService = new ClientService();
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

        ctx.status(200);
        ctx.json(client);
    };

    private Handler deleteClient = (ctx) ->{
        String id = ctx.pathParam("clientId");
        boolean client = clientService.deleteClient(id);

        ctx.status(200);
        ctx.json("Client with id " + id + " has been deleted");
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/clients", getAllClients);
        app.get("/clients/{clientId}", getClientById);
        app.post("/clients", addClient);
        app.put("/clients/{clientId}", editClient);
        app.delete("/clients/{clientId}", deleteClient);
    }
}
