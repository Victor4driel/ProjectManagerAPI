package com.vabp.project.repository;

import com.vabp.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Modifying
    @Query(value = "DELETE FROM project_member WHERE project_id = :projectId", nativeQuery = true)
    void removeProjectAssociations(@Param("projectId") UUID projectId);
}
