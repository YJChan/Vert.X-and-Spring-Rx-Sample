package com.demo.starter.service;

import com.demo.starter.entity.TodoEntity;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public interface TodoService {

    Future<TodoEntity> get(int id);

    Future<List<TodoEntity>> getAll();

    Future<Boolean> create(TodoEntity todo);

    Future<Boolean> delete(int id);

    Future<Boolean> deleteAll();

    Future<TodoEntity> update(int id, TodoEntity todo);
}
