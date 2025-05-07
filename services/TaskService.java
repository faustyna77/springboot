package zaklad.pogrzebowy.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaklad.pogrzebowy.api.models.Task;
import zaklad.pogrzebowy.api.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {

    @Autowired
    private TaskRepository repository;

    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Task> findByStatus(Task.Status status) {
        return repository.findByStatus(status);
    }

    @Override
    public Task create(Task task) {
        return repository.save(task);
    }

    @Override
    public Task update(Long id, Task updatedTask) {
        return repository.findById(id)
                .map(existingTask -> {
                    existingTask.setTaskName(updatedTask.getTaskName());
                    existingTask.setDescription(updatedTask.getDescription());
                    existingTask.setStatus(updatedTask.getStatus());
                    existingTask.setPriority(updatedTask.getPriority());
                    existingTask.setDueDate(updatedTask.getDueDate());
                    existingTask.setOrder(updatedTask.getOrder());
                    return repository.save(existingTask);
                })
                .orElseThrow(() -> new RuntimeException("Zadanie nie znalezione"));
    }

    @Override
    public void delete(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getUsers() != null) {
            task.getUsers().clear(); // usunięcie przypisań
            repository.save(task);
        }

        repository.deleteById(id);
    }
}
