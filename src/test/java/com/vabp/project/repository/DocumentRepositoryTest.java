package com.vabp.project.repository;

import com.vabp.project.model.Document;
import com.vabp.project.model.Member;
import com.vabp.project.model.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DocumentRepositoryTest {

    private final DocumentRepository documentRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public DocumentRepositoryTest(DocumentRepository documentRepository, MemberRepository memberRepository, ProjectRepository projectRepository) {
        this.documentRepository = documentRepository;
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
    }

    @Test
    @DisplayName("Should delete successfully from DB")
    void deleteDocumentByProjectId() {
        Member author = createMember();
        Project project = createProject();
        List<Document> documents;

        createDocument(author, project);

        documents = documentRepository.findAll();

        assertEquals(1, documents.size());

        documentRepository.deleteDocumentByProjectId(project.getId());

        documents = documentRepository.findAll();

        assertEquals(0, documents.size());
    }

    private Document createDocument(Member author, Project project) {
        Document document = new Document();

        document.setTitle("document title");
        document.setContent("document content");
        document.setAuthor(author);
        document.setProject(project);

        documentRepository.save(document);

        return document;
    }

    private Member createMember() {
        Member member = new Member();

        member.setName("user test");
        member.setEmail("test@email.com");
        member.setRole("MANAGER");

        memberRepository.save(member);

        return member;
    }

    private Project createProject() {
        Project project = new Project();

        project.setName("project name");
        project.setDescription("project description");

        projectRepository.save(project);

        return project;
    }
}