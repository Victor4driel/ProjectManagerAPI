package com.vabp.project.repository;

import com.vabp.project.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findTaskByResponsibleId(UUID memberId);

    void deleteTaskByProjectId(UUID projectId);
}
