package io.github.simplydemo;

import io.github.simplydemo.handler.UserHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestDemoVerticle extends AbstractVerticle {

    public static final int PORT = 8080;

    UserHandler userHandler = new UserHandler();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route(HttpMethod.GET, "/health")
                .handler(this::handleHealth);

        router.route(HttpMethod.POST, "/users")
                .handler(BodyHandler.create())
                .handler(userHandler::handleAddUser);

        router.route(HttpMethod.GET, "/users/:userId")
                .handler(userHandler::handleGetUser);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(PORT);
    }

    private void handleHealth(RoutingContext routingContext) {
        routingContext.response().putHeader("content-type", "text/plain")
                .setStatusCode(200)
                .end("OK");
    }
}


