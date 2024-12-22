package com.vabp.project.service;

import com.vabp.project.exception.MemberNotFound;
import com.vabp.project.exception.ProjectNotFound;
import com.vabp.project.model.Member;
import com.vabp.project.model.Project;
import com.vabp.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_Success() {
        Member manager = new Member("John Doe", "john.doe@example.com", "MANAGER", new ArrayList<>(), new ArrayList<>());
        manager.setId(UUID.randomUUID());
        Project project = new Project("Project A", "Description A", null, null, "IN_PROGRESS", manager, null, null, null);

        when(memberService.getMemberById(manager.getId())).thenReturn(manager);
        when(projectRepository.save(project)).thenReturn(project);

        Project createdProject = projectService.createProject(project);

        assertNotNull(createdProject);
        assertEquals("Project A", createdProject.getName());
        verify(memberService, times(1)).getMemberById(manager.getId());
        verify(memberService, times(1)).updateMember(manager.getId(), manager);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void createProject_ManagerNotFound() {
        Member manager = new Member();
        manager.setId(UUID.randomUUID());
        Project project = new Project();
        project.setManager(manager);

        when(memberService.getMemberById(manager.getId())).thenReturn(null);

        assertThrows(MemberNotFound.class, () -> projectService.createProject(project));
        verify(memberService, times(1)).getMemberById(manager.getId());
        verify(projectRepository, never()).save(any());
    }

    @Test
    void getAllProjects() {
        List<Project> projects = Arrays.asList(
                new Project("Project A", "Description A", null, null, "IN_PROGRESS", null, null, null, null),
                new Project("Project B", "Description B", null, null, "COMPLETED", null, null, null, null)
        );

        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> returnedProjects = projectService.getAllProjects();

        assertEquals(2, returnedProjects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getProjectById_Found() {
        UUID projectId = UUID.randomUUID();
        Project project = new Project("Project A", "Description A", null, null, "IN_PROGRESS", null, null, null, null);
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Project foundProject = projectService.getProjectById(projectId);

        assertNotNull(foundProject);
        assertEquals("Project A", foundProject.getName());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void getProjectById_NotFound() {
        UUID projectId = UUID.randomUUID();

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Project foundProject = projectService.getProjectById(projectId);

        assertNull(foundProject);
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void updateProject_Success() {
        UUID projectId = UUID.randomUUID();
        Project existingProject = new Project("Old Project", "Old Description", null, null, "IN_PROGRESS", null, null, null, null);
        existingProject.setId(projectId);

        Project updatedProjectData = new Project("Updated Project", "Updated Description", null, null, "COMPLETED", null, null, null, null);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        Project updatedProject = projectService.updateProject(projectId, updatedProjectData);

        assertNotNull(updatedProject);
        assertEquals("Updated Project", updatedProject.getName());
        assertEquals("Updated Description", updatedProject.getDescription());
        assertEquals("COMPLETED", updatedProject.getStatus());
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    void updateProject_NotFound() {
        UUID projectId = UUID.randomUUID();
        Project updatedProjectData = new Project();

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFound.class, () -> projectService.updateProject(projectId, updatedProjectData));
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any());
    }

    @Test
    void addMemberToProject_Success() {
        UUID projectId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();

        Member member = new Member("John Doe", "john.doe@example.com", "DEVELOPER", new ArrayList<>(), new ArrayList<>());
        member.setId(memberId);

        Project project = new Project("Project A", "Description A", null, null, "IN_PROGRESS", null, new ArrayList<>(), new ArrayList<>(), null);
        project.setId(projectId);

        when(memberService.getMemberById(memberId)).thenReturn(member);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        Member addedMember = projectService.addMemberToProject(projectId, memberId);

        assertNotNull(addedMember);
        assertTrue(project.getTeamMembers().contains(member));
        verify(memberService, times(1)).getMemberById(memberId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(memberService, times(1)).updateMember(memberId, member);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void addMemberToProject_MemberNotFound() {
        UUID projectId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();

        when(memberService.getMemberById(memberId)).thenReturn(null);

        assertThrows(MemberNotFound.class, () -> projectService.addMemberToProject(projectId, memberId));
        verify(memberService, times(1)).getMemberById(memberId);
        verify(projectRepository, never()).save(any());
    }

    @Test
    void addMemberToProject_ProjectNotFound() {
        UUID projectId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();

        Member member = new Member();
        member.setId(memberId);

        when(memberService.getMemberById(memberId)).thenReturn(member);
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFound.class, () -> projectService.addMemberToProject(projectId, memberId));
        verify(memberService, times(1)).getMemberById(memberId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any());
    }

    @Test
    void deleteProject() {
        UUID projectId = UUID.randomUUID();

        doNothing().when(projectRepository).removeProjectAssociations(projectId);
        doNothing().when(projectRepository).deleteById(projectId);

        projectService.deleteProject(projectId);

        verify(projectRepository, times(1)).removeProjectAssociations(projectId);
        verify(projectRepository, times(1)).deleteById(projectId);
    }
}
