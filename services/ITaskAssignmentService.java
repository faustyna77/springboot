package zaklad.pogrzebowy.api.services;

import zaklad.pogrzebowy.api.models.TaskAssignment;

import java.util.List;
import java.util.Optional;

public interface ITaskAssignmentService {
    List<TaskAssignment> findAll();
    Optional<TaskAssignment> findById(Long id);
    List<TaskAssignment> findByUserId(Long userId);
    List<TaskAssignment> findByTaskId(Long taskId);
    TaskAssignment create(TaskAssignment assignment);
    void delete(Long id);
}
