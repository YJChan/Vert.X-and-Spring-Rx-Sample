package task.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import task.entity.TodoEntity;

@Repository
public interface TodoRepository extends ReactiveCrudRepository<TodoEntity, Integer> {
}
