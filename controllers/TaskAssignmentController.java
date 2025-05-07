package zaklad.pogrzebowy.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zaklad.pogrzebowy.api.models.TaskAssignment;
import zaklad.pogrzebowy.api.services.TaskAssignmentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/assignments")
@CrossOrigin(origins = "*")
public class TaskAssignmentController {

    @Autowired
    private TaskAssignmentService service;

    @GetMapping
    public List<TaskAssignment> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<TaskAssignment> getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<TaskAssignment> getByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/task/{taskId}")
    public List<TaskAssignment> getByTaskId(@PathVariable Long taskId) {
        return service.findByTaskId(taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskAssignment create(@RequestBody TaskAssignment assignment) {
        return service.create(assignment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
