package com.vabp.project.service;

import com.vabp.project.exception.MemberNotFound;
import com.vabp.project.exception.ProjectNotFound;
import com.vabp.project.model.Document;
import com.vabp.project.model.Member;
import com.vabp.project.model.Project;
import com.vabp.project.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectService projectService;
    private final MemberService memberService;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, ProjectService projectService, MemberService memberService) {
        this.documentRepository = documentRepository;
        this.projectService = projectService;
        this.memberService = memberService;
    }

    @Override
    public Document createDocument(Document document) {
        Project project = this.projectService.getProjectById(document.getProject().getId());
        Member author = this.memberService.getMemberById(document.getAuthor().getId());
        System.out.println(author);
        if (author == null) {
            throw new MemberNotFound("Member not found");
        }

        if (project == null) {
            throw new ProjectNotFound("Project not found");
        }

        document.setAuthor(author);
        document.setProject(project);

        project.setDocument(document);

        documentRepository.save(document);
        projectService.updateProject(project.getId(), project);

        return document;
    }

    @Override
    public List<Document> getDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentById(UUID id) {
        return documentRepository.findById(id).orElseThrow();
    }

    @Override
    public Document updateDocument(UUID id, Document document) {
        Document documentToUpdate = documentRepository.findById(id).orElseThrow();

        if (document.getTitle() != null) {
            documentToUpdate.setTitle(document.getTitle());
        }

        if (document.getContent() != null) {
            documentToUpdate.setContent(document.getContent());
        }

        documentToUpdate.setUpdatedAt(new Date());

        documentRepository.save(documentToUpdate);

        return documentToUpdate;
    }

    @Override
    public void deleteDocument(UUID id) {
        Document documentToDelete = documentRepository.findById(id).orElseThrow();
        Project project = projectService.getProjectById(documentToDelete.getProject().getId());

        project.setDocument(null);

        documentRepository.deleteById(id);
    }
}
