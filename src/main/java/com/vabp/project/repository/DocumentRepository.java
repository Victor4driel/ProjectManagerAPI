package com.vabp.project.repository;

import com.vabp.project.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    void deleteDocumentByProjectId(UUID projectId);
}
