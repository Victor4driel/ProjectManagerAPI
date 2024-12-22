package com.vabp.project.service;

import com.vabp.project.model.Member;
import com.vabp.project.model.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    Project createProject(Project project);

    List<Project> getAllProjects();

    Project getProjectById(UUID id);

    Project updateProject(UUID id, Project project);

    Member addMemberToProject(UUID projectId, UUID memberId);

    void deleteProject(UUID id);
}
