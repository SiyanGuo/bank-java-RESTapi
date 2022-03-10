package com.revature.main;

import com.revature.controller.ClientController;
import com.revature.controller.Controller;
import com.revature.controller.ExceptionController;
import io.javalin.Javalin;


public class Driver {
    public static void main(String[] args) {

        String url = System.getenv("db_password");
        System.out.println(url);

        Javalin app = Javalin.create();
        mapControllers(app, new ClientController(), new ExceptionController());
        app.start();
    }

    public static void mapControllers(Javalin app, Controller... controllers){
        for (Controller c: controllers) {
            c.mapEndpoints(app);
        }
    }
}
