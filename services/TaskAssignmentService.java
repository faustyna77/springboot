package zaklad.pogrzebowy.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaklad.pogrzebowy.api.models.TaskAssignment;
import zaklad.pogrzebowy.api.repositories.TaskAssignmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskAssignmentService implements ITaskAssignmentService {

    @Autowired
    private TaskAssignmentRepository repository;

    @Override
    public List<TaskAssignment> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TaskAssignment> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<TaskAssignment> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<TaskAssignment> findByTaskId(Long taskId) {
        return repository.findByTaskId(taskId);
    }

    @Override
    public TaskAssignment create(TaskAssignment assignment) {
        return repository.save(assignment);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
