package com.vabp.project.service;

import com.vabp.project.exception.MemberNotFound;
import com.vabp.project.exception.ProjectNotFound;
import com.vabp.project.exception.TaskNotFound;
import com.vabp.project.model.Member;
import com.vabp.project.model.Project;
import com.vabp.project.model.Task;
import com.vabp.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final MemberService memberService;
    private final ProjectService projectService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, MemberService memberService, ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.memberService = memberService;
        this.projectService = projectService;
    }

    @Override
    public Task createTask(Task task) {
        Project project = projectService.getProjectById(task.getProject().getId());
        Member member = memberService.getMemberById(task.getResponsible().getId());

        if (member == null)  {
            throw new MemberNotFound("Member not found");
        }

        if (project == null)  {
            throw new ProjectNotFound("Project not found");
        }

        project.getTasks().add(task);
        member.getTasks().add(task);

        task.setResponsible(member);
        task.setProject(project);

        taskRepository.save(task);

        projectService.updateProject(project.getId(), project);
        memberService.updateMember(member.getId(), member);

        return task;
    }

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public List<Task> getTasksByUserId(UUID userId) {
        return taskRepository.findTaskByResponsibleId(userId);
    }

    @Override
    public Task updateTask(UUID id, Task task) {
        Task taskToUpdate = taskRepository.findById(id).orElse(null);

        if (taskToUpdate == null) {
           throw new TaskNotFound("Task not found");
        }

        if (task.getName() != null) {
            taskToUpdate.setName(task.getName());
        }

        if (task.getDescription() != null) {
            taskToUpdate.setDescription(task.getDescription());
        }

        if (task.getDueDate() != null) {
            taskToUpdate.setDueDate(task.getDueDate());
        }

        if (task.getStatus() != null) {
            taskToUpdate.setStatus(task.getStatus());
        }

        if (task.getResponsible() != null) {
            Member responsible = memberService.getMemberById(task.getResponsible().getId());

            if (responsible == null) {
               throw new MemberNotFound("Member not found");
            }

            taskToUpdate.setResponsible(responsible);
        }

        taskRepository.save(taskToUpdate);

        return taskToUpdate;
    }

    @Override
    public void deleteTask(UUID id) {
        Task taskToDelete = taskRepository.findById(id).orElse(null);

        if (taskToDelete != null) {
            Member member = memberService.getMemberById(taskToDelete.getResponsible().getId());
            Project project = projectService.getProjectById(taskToDelete.getProject().getId());

            if (member != null && project != null) {
                member.getTasks().remove(taskToDelete);
                project.getTasks().remove(taskToDelete);

                memberService.updateMember(member.getId(), member);
                projectService.updateProject(project.getId(), project);

                taskRepository.deleteById(id);
            }
        }
    }
}
