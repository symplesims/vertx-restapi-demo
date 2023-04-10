package io.github.simplydemo;

import io.github.simplydemo.handler.UserHandler;
import io.github.simplydemo.model.User;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class RestDemoVerticleTest {

    private static final Logger LOG = LoggerFactory.getLogger(RestDemoVerticleTest.class);

    @Test
    @DisplayName("Test get operation from UserHandler")
    void test_get(final Vertx vertx, final VertxTestContext context) throws Throwable {
        final WebClient webClient = WebClient.create(vertx);
        vertx.deployVerticle(new RestDemoVerticle(), context.succeeding(id -> {
            webClient.get(RestDemoVerticle.PORT, "localhost", String.format("%s/hello", UserHandler.BASE_URI))
                    //.as(BodyCodec.string())
                    .send(context.succeeding(res -> {
                        User user = res.bodyAsJson(User.class);
                        LOG.info(user);
                        context.verify(() -> {
                            Assertions.assertEquals(res.statusCode(), 200);
                            context.completeNow();
                        });
                    }));
        }));
    }

    @Test
    @DisplayName("Test post operation from UserHandler")
    void test_post(final Vertx vertx, final VertxTestContext context) throws Throwable {
        final WebClient webClient = WebClient.create(vertx);
        vertx.deployVerticle(new RestDemoVerticle(), context.succeeding(id -> {
            webClient.post(RestDemoVerticle.PORT, "localhost", UserHandler.BASE_URI)
                    .as(BodyCodec.string())
                    .sendJson(new User("1001", "symplesims"), context.succeeding(res -> {
                        context.verify(() -> {
                            Assertions.assertEquals(res.statusCode(), 201);
                            context.completeNow();
                        });
                    }));
        }));
    }


    @Test
    @DisplayName("Test get operation 10 times from UserHandler with checkpoint")
    void test_get_10_times_with_checkpoints(final Vertx vertx, final VertxTestContext context) throws Throwable {
        final WebClient webClient = WebClient.create(vertx);
        final Checkpoint handlerCheckpoint = context.checkpoint();
        // request 10 times and check with request checkpoint
        final Checkpoint requestCheckpoint = context.checkpoint(10);
        vertx.deployVerticle(new RestDemoVerticle(), context.succeeding(id -> {
            handlerCheckpoint.flag();
            for (int i = 0; i < 10; i++) {
                webClient.get(RestDemoVerticle.PORT, "localhost", "/users/hello").as(BodyCodec.string()).send(context.succeeding(res -> {
                    context.verify(() -> {
                        Assertions.assertEquals(res.statusCode(), 200);
                        requestCheckpoint.flag();
                    });
                }));
            }
        }));
    }


}

