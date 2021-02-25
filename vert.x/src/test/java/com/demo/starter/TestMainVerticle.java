package com.demo.starter;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.awaitCompletion(5, TimeUnit.SECONDS);
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    }
    testContext.completeNow();
  }

  @AfterEach
  void vertical_takedown(Vertx vertx, VertxTestContext testContext) throws Throwable {
    if (testContext.failed()) {
      throw testContext.causeOfFailure();
    }
    testContext.completeNow();
  }
}
