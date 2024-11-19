package com.example.kanban.repository;
import com.example.kanban.model.Priority;
import com.example.kanban.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.priority = :priority")
    List<Task> findByPriority(@Param("priority") Priority priority);

    @Query("SELECT t FROM Task t WHERE t.dueDate = :dueDate")
    List<Task> findByDueDate(@Param("dueDate") LocalDate dueDate);
}