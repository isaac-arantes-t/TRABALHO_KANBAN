package com.example.kanban.service;


import com.example.kanban.exception.TaskAlreadyCompletedException;
import com.example.kanban.exception.TaskNotFoundException;
import com.example.kanban.model.Status;
import com.example.kanban.model.Task;
import com.example.kanban.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;


@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Map<Status, List<Task>> generateTaskReport() {
        return taskRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Task::getStatus,
                        Collectors.toList()
                ));
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findAll().stream()
                .filter(task -> task.getDueDate() != null &&
                        task.getDueDate().isBefore(LocalDate.now()) &&
                        task.getStatus() != Status.DONE)
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream()
                .sorted((task1, task2) -> {
                    if (task1.getStatus() != task2.getStatus()) {
                        return task1.getStatus().compareTo(task2.getStatus());
                    }
                    return task1.getPriority().compareTo(task2.getPriority());
                })
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getPriority().toString().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByDueDate(LocalDate dueDate) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getDueDate() != null && task.getDueDate().isEqual(dueDate))
                .collect(Collectors.toList());
    }


    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setPriority(updatedTask.getPriority());
            return taskRepository.save(task);
        }).orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    public Task moveTask(Long id) {
        return taskRepository.findById(id).map(task -> {
            switch (task.getStatus()) {
                case TODO -> task.setStatus(Status.IN_PROGRESS);
                case IN_PROGRESS -> task.setStatus(Status.DONE);
                case DONE -> throw new TaskAlreadyCompletedException("A tarefa já está concluída e não pode ser movida.");
            }
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }


    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}


