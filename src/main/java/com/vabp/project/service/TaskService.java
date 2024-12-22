package com.vabp.project.service;

import com.vabp.project.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    Task createTask(Task task);

    List<Task> getTasks();

    Task getTaskById(UUID id);

    List<Task> getTasksByUserId(UUID userId);

    Task updateTask(UUID id, Task task);

    void deleteTask(UUID id);
}
