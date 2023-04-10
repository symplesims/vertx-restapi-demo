package io.github.simplydemo.handler;

import io.github.simplydemo.model.User;
import io.github.simplydemo.utils.HttpContext;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class UserHandler implements Handler<HttpContext> {

    public static final String BASE_URI = "/users";

    public void handleAddUser(RoutingContext routingContext) {
        // HttpContext.from(routingContext).pathParam()
        User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
        System.out.println("user: " + user);
        // 데이터베이스에 유저 정보 저장
        routingContext.response().setStatusCode(201).end();
    }

    public void handleGetUser(RoutingContext routingContext) {
        String userId = routingContext.request().getParam("userId");
        User user = new User(userId, "world");
        routingContext.response().putHeader("content-type", "application/json")
                .setStatusCode(200)
                .end(Json.encodePrettily(user));
    }

    @Override
    public void handle(HttpContext httpContext) {
        httpContext.response()
                .putHeader("content-type", "text/plain")
                .end("Hava a nice day.");
    }

}
