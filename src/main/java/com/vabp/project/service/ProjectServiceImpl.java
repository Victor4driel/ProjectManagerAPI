package com.vabp.project.service;

import com.vabp.project.exception.MemberNotFound;
import com.vabp.project.exception.ProjectNotFound;
import com.vabp.project.model.Member;
import com.vabp.project.model.Project;
import com.vabp.project.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberService memberService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, MemberService memberService) {
        this.projectRepository = projectRepository;
        this.memberService = memberService;
    }

    @Override
    public Project createProject(Project project) {
        Member manager = memberService.getMemberById(project.getManager().getId());

        if (manager == null) {
            throw new MemberNotFound("Member not found");
        }

        manager.addProject(project);

        project.setManager(manager);

        projectRepository.save(project);
        memberService.updateMember(manager.getId(), manager);

        return project;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(UUID id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Override
    public Project updateProject(UUID id, Project project) {
        Project projectToUpdate = projectRepository.findById(id).orElse(null);

        if (projectToUpdate == null) {
            throw new ProjectNotFound("Project not found");
        }

        if (project.getName() != null) {
            projectToUpdate.setName(project.getName());
        }

        if (project.getDescription() != null) {
            projectToUpdate.setDescription(project.getDescription());
        }

        if (project.getStartDate() != null) {
            projectToUpdate.setStartDate(project.getStartDate());
        }

        if (project.getEndDate() != null) {
            projectToUpdate.setEndDate(project.getEndDate());
        }

        if (project.getStatus() != null) {
            projectToUpdate.setStatus(project.getStatus());
        }

        projectRepository.save(projectToUpdate);

        return projectToUpdate;
    }

    @Override
    public Member addMemberToProject(UUID projectId, UUID memberId) {
        Member member = memberService.getMemberById(memberId);
        Project project = getProjectById(projectId);

        if (member == null)  {
            throw new MemberNotFound("Member not found");
        }

        if (project == null)  {
            throw new ProjectNotFound("Project not found");
        }

        member.addProject(project);
        project.addMemberToProject(member);

        memberService.updateMember(member.getId(), member);
        projectRepository.save(project);

        return member;
    }

    @Override
    @Transactional
    public void deleteProject(UUID id) {
       projectRepository.removeProjectAssociations(id);
       projectRepository.deleteById(id);
    }
}
