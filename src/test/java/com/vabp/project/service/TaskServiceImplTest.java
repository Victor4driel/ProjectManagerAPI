package com.vabp.project.service;

import com.vabp.project.exception.MemberNotFound;
import com.vabp.project.exception.ProjectNotFound;
import com.vabp.project.exception.TaskNotFound;
import com.vabp.project.model.Member;
import com.vabp.project.model.Project;
import com.vabp.project.model.Task;
import com.vabp.project.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task mockTask;
    private Member mockMember;
    private Project mockProject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockTask = new Task();
        mockTask.setId(UUID.randomUUID());
        mockTask.setName("Test Task");

        mockMember = new Member();
        mockMember.setId(UUID.randomUUID());

        mockProject = new Project();
        mockProject.setId(UUID.randomUUID());
    }

    @Test
    void createTask_success() {
        mockTask.setProject(mockProject);
        mockTask.setResponsible(mockMember);

        when(projectService.getProjectById(mockProject.getId())).thenReturn(mockProject);
        when(memberService.getMemberById(mockMember.getId())).thenReturn(mockMember);
        when(taskRepository.save(mockTask)).thenReturn(mockTask);

        Task createdTask = taskService.createTask(mockTask);

        assertNotNull(createdTask);
        assertEquals(mockTask.getName(), createdTask.getName());
        verify(taskRepository, times(1)).save(mockTask);
    }

    @Test
    void createTask_memberNotFound() {
        mockTask.setProject(mockProject);
        mockTask.setResponsible(mockMember);

        when(projectService.getProjectById(mockProject.getId())).thenReturn(mockProject);
        when(memberService.getMemberById(mockMember.getId())).thenReturn(null);

        assertThrows(MemberNotFound.class, () -> taskService.createTask(mockTask));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createTask_projectNotFound() {
        mockTask.setProject(mockProject);
        mockTask.setResponsible(mockMember);

        when(projectService.getProjectById(mockProject.getId())).thenReturn(null);
        when(memberService.getMemberById(mockMember.getId())).thenReturn(mockMember);

        assertThrows(ProjectNotFound.class, () -> taskService.createTask(mockTask));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void getTasks_success() {
        when(taskRepository.findAll()).thenReturn(List.of(mockTask));

        List<Task> tasks = taskService.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_success() {
        when(taskRepository.findById(mockTask.getId())).thenReturn(Optional.of(mockTask));

        Task foundTask = taskService.getTaskById(mockTask.getId());

        assertNotNull(foundTask);
        assertEquals(mockTask.getName(), foundTask.getName());
        verify(taskRepository, times(1)).findById(mockTask.getId());
    }

    @Test
    void getTaskById_notFound() {
        when(taskRepository.findById(mockTask.getId())).thenReturn(Optional.empty());

        Task foundTask = taskService.getTaskById(mockTask.getId());

        assertNull(foundTask);
    }

    @Test
    void updateTask_success() {
        Task updatedTask = new Task();
        updatedTask.setName("Updated Task");

        when(taskRepository.findById(mockTask.getId())).thenReturn(Optional.of(mockTask));
        when(taskRepository.save(mockTask)).thenReturn(mockTask);

        Task result = taskService.updateTask(mockTask.getId(), updatedTask);

        assertNotNull(result);
        assertEquals("Updated Task", result.getName());
        verify(taskRepository, times(1)).save(mockTask);
    }

    @Test
    void updateTask_taskNotFound() {
        when(taskRepository.findById(mockTask.getId())).thenReturn(Optional.empty());

        assertThrows(TaskNotFound.class, () -> taskService.updateTask(mockTask.getId(), mockTask));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask_success() {
        when(taskRepository.findById(mockTask.getId())).thenReturn(Optional.of(mockTask));
        when(memberService.getMemberById(any())).thenReturn(mockMember);
        when(projectService.getProjectById(any())).thenReturn(mockProject);

        taskService.deleteTask(mockTask.getId());

        verify(taskRepository, times(1)).deleteById(mockTask.getId());
    }

    @Test
    void deleteTask_notFound() {
        when(taskRepository.findById(mockTask.getId())).thenReturn(Optional.empty());

        taskService.deleteTask(mockTask.getId());

        verify(taskRepository, never()).deleteById(any());
    }
}
