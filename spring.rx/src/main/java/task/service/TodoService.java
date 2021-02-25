package task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task.entity.TodoEntity;
import task.repository.TodoRepository;

import java.time.LocalDateTime;

@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public Boolean isValid(final TodoEntity task) {
        return task != null && !task.getDescription().isEmpty();
    }

    public Flux<TodoEntity> getAllTodo() {
        return this.repository.findAll();
    }

    @Transactional
    public Mono<TodoEntity> createTodo(final TodoEntity task) {
        return this.repository.save(task);
    }

    @Transactional
    public Mono<TodoEntity> updateTodo(final TodoEntity task) {
        return this.repository.findById(task.getId())
                .flatMap(t -> {
                    t.setName(task.getName());
                    t.setDescription(task.getDescription());
                    t.setDone(task.getDone());
                    return this.repository.save(t);
                });
    }

    @Transactional
    public Mono<Void> deleteTodo(final int id){
        return this.repository.findById(id)
                .flatMap(this.repository::delete);
    }

    public Mono<TodoEntity> getTodo(Integer id) {
        return this.repository.findById(id);
    }
}
