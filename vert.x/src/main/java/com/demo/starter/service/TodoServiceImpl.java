package com.demo.starter.service;

import com.demo.starter.entity.TodoEntity;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class TodoServiceImpl implements TodoService {

    private final Vertx vertx;
    private final MySQLPool pool;
    private final String DOCKER_HOST = "tododb";
    private final String LOCAL_HOST = "localhost";
    private final String DB_NAME = "tododb";
    private final String DB_USER = "appuser";
    private final String DB_PASSWORD = "password";

    public TodoServiceImpl(Vertx vertx) {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(3307)
                .setHost(DOCKER_HOST)
                .setDatabase(DB_NAME)
                .setUser(DB_USER)
                .setPassword(DB_PASSWORD);

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);
        this.vertx = vertx;
        String connectionString = "mysql://appuser:password@tododb:3306/tododb";
        this.pool = MySQLPool.pool(this.vertx, connectionString, poolOptions);
    }

    @Override
    public Future<TodoEntity> get(int id) {
        return this.pool.preparedQuery("SELECT * FROM todo WHERE id = ?")
            .execute(Tuple.of(id))
            .map(rows -> {
                TodoEntity todo = new TodoEntity();
                System.out.println("row size:" + rows.size());
                if (rows.size() == 0) {
                    return null;
                }
                for (Row row: rows) {
                    todo.setId(row.getInteger("id"));
                    todo.setName(row.getString("name"));
                    todo.setDesc(row.getString("description"));
                    todo.setDone(row.getBoolean("done"));
                }
                return todo;
            });
    }

    @Override
    public Future<List<TodoEntity>> getAll() {
        return this.pool.preparedQuery("SELECT * FROM todo")
            .execute()
            .map(rows -> {
                List<TodoEntity> list = new ArrayList<>();

                System.out.println("row size:" + rows.size());
                if (rows.size() == 0) {
                    return null;
                }
                for (Row row: rows) {
                    TodoEntity todo = new TodoEntity();
                    todo.setId(row.getInteger("id"));
                    todo.setName(row.getString("name"));
                    todo.setDesc(row.getString("description"));
                    todo.setDone(row.getBoolean("done"));
                    list.add(todo);
                }
                return list;
            });
    }

    @Override
    public Future<Boolean> create(TodoEntity todo) {
        return this.pool.withTransaction(sqlConnection -> {
            return sqlConnection.preparedQuery("INSERT INTO todo (name, description, done) VALUES (?, ?, ?)")
                    .execute(Tuple.of(todo.name, todo.desc, todo.done))
                    .map(rows -> rows.rowCount() > 0);
        }).map(s -> {
            return s;
        })
        .onSuccess(s -> System.out.println("successfully inserted"))
        .onFailure(err -> System.out.println("err: " + err.getMessage()));
    }

    @Override
    public Future<Boolean> delete(int id) {
        return this.pool.withTransaction(sqlConnection -> {
            return sqlConnection.preparedQuery("DELETE FROM todo WHERE id = ?")
                    .execute(Tuple.of(id))
                    .map(rows -> {
                        System.out.println("deleted rows: " + rows.rowCount());
                        return rows.rowCount() > 0;
                    });
        }).map(s -> {
            return s;
        })
        .onSuccess(s -> System.out.println("successfully delete single with id " + id))
        .onFailure(err -> System.out.println("err: " + err.getMessage()));
    }

    @Override
    public Future<Boolean> deleteAll() {
        return this.pool.withTransaction(sqlConnection -> {
            return sqlConnection.preparedQuery("DELETE FROM todo")
                    .execute()
                    .map(rows -> rows.rowCount() > 0);
        }).map(s -> {
            return s;
        })
        .onSuccess(s -> System.out.println("successfully deleted all"))
        .onFailure(err -> System.out.println("err: " + err.getMessage()));
    }

    @Override
    public Future<TodoEntity> update(int id, TodoEntity todo) {
        if (id != todo.getId()) {
            return Future.failedFuture("id does not match, action denied");
        }
        return this.pool.withTransaction(sqlConnection -> {
            return sqlConnection.preparedQuery("UPDATE todo SET name = ? , description = ?, done = ? WHERE id = ?")
                    .execute(Tuple.of(todo.name, todo.desc, todo.done, todo.id))
                    .flatMap(res -> sqlConnection.preparedQuery("SELECT * FROM todo WHERE id = ?")
                    .execute(Tuple.of(todo.id))
                    .map(rows -> {
                        TodoEntity updatedTodo = new TodoEntity();
                        System.out.println("row size:" + rows.rowCount());
                        if (rows.size() == 0) {
                            return null;
                        }
                        for (Row row: rows) {
                            updatedTodo.setId(row.getInteger("id"));
                            updatedTodo.setName(row.getString("name"));
                            updatedTodo.setDesc(row.getString("description"));
                            updatedTodo.setDone(row.getBoolean("done"));
                        }
                        return updatedTodo;
                    }));
            })
            .onSuccess(s -> System.out.println("successfully updated with id " + todo.getId()))
            .onFailure(err -> System.out.println("err: " + err.getMessage()));
    }

}
