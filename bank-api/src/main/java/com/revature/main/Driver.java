package com.revature.main;

import com.revature.controller.ClientController;
import com.revature.controller.Controller;
import io.javalin.Javalin;


public class Driver {
    public static void main(String[] args) {

        Javalin app = Javalin.create();
        mapControllers(app, new ClientController());
        app.start();
    }

    public static void mapControllers(Javalin app, Controller... controllers){
        for (Controller c: controllers) {
            c.mapEndpoints(app);
        }
    }
}
