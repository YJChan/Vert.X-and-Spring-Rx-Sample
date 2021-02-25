package task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task.entity.TodoEntity;
import task.service.TodoService;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping()
    public ResponseEntity<Flux<TodoEntity>> getAllTodo() {
        return ResponseEntity.ok(this.service.getAllTodo());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Mono<TodoEntity>> getTodo(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.service.getTodo(id));
    }

    @PostMapping()
    public ResponseEntity<Mono<TodoEntity>> createTodo(@RequestBody TodoEntity task) {
        System.out.println(task);
        if (service.isValid(task)) {
            return ResponseEntity.ok(this.service.createTodo(task));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping()
    public ResponseEntity<Mono<TodoEntity>> updateTodo(@RequestBody TodoEntity task) {
        if (service.isValid(task)) {
            return ResponseEntity.ok(this.service.updateTodo(task));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Mono<Void>> deleteTodo(@PathVariable("id") Integer id) {
        if (id > 0) {
            return ResponseEntity.ok(this.service.deleteTodo(id));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
