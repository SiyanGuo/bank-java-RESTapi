package com.revature.main;

import com.revature.controller.ClientController;
import com.revature.controller.Controller;
import com.revature.controller.ExceptionController;
import com.revature.dao.AccountDao;
import com.revature.model.Account;
import io.javalin.Javalin;
import io.javalin.http.util.JsonEscapeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;


public class Driver {
  public static Logger logger = LoggerFactory.getLogger(Driver.class);


   public static void main(String[] args) throws SQLException {


        Javalin app = Javalin.create();
        app.before((ctx) -> {
            logger.info(ctx.method() + " request received for " + ctx.path());
        });

        mapControllers(app, new ClientController(), new ExceptionController());
        app.start();
    }

    public static void mapControllers(Javalin app, Controller... controllers){
        for (Controller c: controllers) {
            c.mapEndpoints(app);
        }
    }
}
