package com.demo.starter;

import com.demo.starter.entity.TodoEntity;
import com.demo.starter.service.TodoService;
import com.demo.starter.service.TodoServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {
  private TodoService todoService;

  public static void main(String[] args) {
      Vertx.vertx().deployVerticle(new MainVerticle());
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    this.todoService = new TodoServiceImpl(vertx);

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/todo").handler(this::getAllTodo);
    router.get("/todo/:id").handler(this::getTodo);
    router.post("/todo").handler(this::createTodo);
    router.put("/todo/:id").handler(ctx -> {
        try {
            updateTodo(ctx);
        } catch (Throwable throwable) {
            ctx.fail(400);
        }
    });
    router.delete("/todo/:id").handler(this::deleteTodo);
    router.delete("/todo").handler(this::deleteAllTodo);
    router.route("/*").handler(StaticHandler.create());

    server.requestHandler(router)
            .listen(8080)
            .onSuccess(s ->
                    System.out.println(
                            "HTTP server started on port " + s.actualPort()
                    )
            )
            .onFailure(s -> System.out.println("Fail to start server"));
  }

  private void getTodo(RoutingContext ctx) {
    int id = Integer.parseInt(ctx.pathParam("id"));
    todoService.get(id)
            .onSuccess(todo -> {
                JsonObject result = new JsonObject();
                if (todo != null) {
                    // JsonObject json = JsonObject.mapFrom(todo);
                    result.put("status", true);
                    result.put("body", todo);
                } else {
                    result.put("status", false);
                }

                ctx.json(result);
            })
            .onFailure(err -> {
              System.out.println("error :" + err.getMessage());
              ctx.fail(500);
            });
  }

  private void createTodo(RoutingContext ctx) {
//      TodoEntity todo = new TodoEntity();
//      todo.setName("second task");
//      todo.setDesc("this must be complete!!");
//      todo.setDone(false);

      JsonObject todoJson = ctx.getBodyAsJson();
      TodoEntity todo = todoJson.mapTo(TodoEntity.class);

      todoService.create(todo)
        .onSuccess(status -> {
            JsonObject result = new JsonObject();
            result.put("status", status);
            ctx.json(result);
        }).onFailure(err -> {
          System.out.println("error :" + err.getMessage());
          ctx.fail(500);
      });
  }

  private void getAllTodo(RoutingContext ctx) {
      todoService.getAll()
              .onSuccess(todoEntities -> {
                  JsonObject result = new JsonObject();
                  result.put("status", true);
                  result.put("body", todoEntities);
                  ctx.json(result);
              }).onFailure(err -> {
                  System.out.println("error :" + err.getMessage());
                  ctx.fail(500);
              });
  }

    private void updateTodo(RoutingContext ctx) throws Throwable {
        int id = Integer.parseInt(ctx.pathParam("id"));
        JsonObject todoJson = ctx.getBodyAsJson();
        TodoEntity todo = todoJson.mapTo(TodoEntity.class);
        todoService.update(id, todo)
                .onSuccess(todoEntity -> {
                    JsonObject result = new JsonObject();
                    result.put("status", true);
                    result.put("body", todoEntity);
                    ctx.json(result);
                }).onFailure(err -> {
                    System.out.println("error :" + err.getMessage());
                    ctx.fail(500);
                });
    }

    private void deleteTodo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        todoService.delete(id)
                .onSuccess(status -> {
                    JsonObject result = new JsonObject();
                    result.put("status", status);
                    ctx.json(result);
                }).onFailure(err -> {
                    System.out.println("error :" + err.getMessage());
                    ctx.fail(500);
                });

    }

    private void deleteAllTodo(RoutingContext ctx) {
        todoService.deleteAll()
                .onSuccess(status -> {
                    JsonObject result = new JsonObject();
                    result.put("status", status);
                    ctx.json(result);
                }).onFailure(err -> {
            System.out.println("error :" + err.getMessage());
            ctx.fail(500);
        });
    }

    private void errorHandler(RoutingContext ctx) {
      ctx.fail(400);
    }
}
