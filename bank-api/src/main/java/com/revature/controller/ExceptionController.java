package com.revature.controller;

import com.revature.exception.ClientNotFoundException;
import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionController implements Controller {

    private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    private ExceptionHandler ClientNotFound = (e, ctx) -> {
        logger.warn("User attempted to retrieve a client that was not found. Exception message is " + e.getMessage());

        ctx.status(404);
        ctx.json(e.getMessage());
    };

    private ExceptionHandler badArgument = (e, ctx) -> {
        logger.warn("User input a bad argument. Exception message is " + e.getMessage());

        ctx.status(400);
        ctx.json(e.getMessage());
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.exception(ClientNotFoundException.class, ClientNotFound);
        app.exception(IllegalArgumentException.class, badArgument);
    }
}
