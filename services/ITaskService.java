package zaklad.pogrzebowy.api.services;



import zaklad.pogrzebowy.api.models.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskService {

    List<Task> findAll();
    Optional<Task> findById(Long id);
    List<Task> findByStatus(Task.Status status);
    Task create(Task task);
    Task update(Long id, Task task);
    void delete(Long id);
}
