package io.github.simplydemo.utils;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

public class HttpContext {

    private final RoutingContext routingContext;

    private HttpContext(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public static HttpContext from(RoutingContext routingContext) {
        return new HttpContext(routingContext);
    }

    public HttpContext response(JsonObject body) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(body.encode());
        return this;
    }

    public String pathParam(String name) {
        return routingContext.pathParam(name);
    }

    public String queryParam(String name) {
        return routingContext.queryParam(name).get(0);
    }

    public Map<String, String> headers() {
        return (Map<String, String>) routingContext.request().headers().entries();
    }

    public HttpServerRequest request() {
        return routingContext.request();
    }

    public HttpServerResponse response() {
        return routingContext.response();
    }

    public void fail(int statusCode, String message) {
        routingContext.fail(statusCode, new RuntimeException(message));
    }
}
