package com.vabp.project.service;

import com.vabp.project.exception.MemberNotFound;
import com.vabp.project.exception.ProjectNotFound;
import com.vabp.project.model.Document;
import com.vabp.project.model.Member;
import com.vabp.project.model.Project;
import com.vabp.project.repository.DocumentRepository;
import com.vabp.project.repository.MemberRepository;
import com.vabp.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectServiceImpl projectService;

    @Mock
    private MemberServiceImpl memberService;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createDocument() {
        Member author = new Member("John", "J@email.com", "MANAGER", null, null);
        Project project = new Project("Projeto 1", "Description", null, null, "finished", null, null, null, null);

        author.setId(UUID.randomUUID());
        project.setId(UUID.randomUUID());

        when(memberService.getMemberById(author.getId())).thenReturn(author);
        when(projectService.getProjectById(project.getId())).thenReturn(project);

        Document document = new Document();
        document.setTitle("Test Document");
        document.setContent("Text...............");
        document.setAuthor(author);
        document.setProject(project);

        Document documentReturned = documentService.createDocument(document);

        project.setDocument(document);

        verify(documentRepository, times(1)).save(document);
        verify(projectService, times(1)).updateProject(project.getId(), project);

        assertEquals(documentReturned, document);
    }

    @Test
    void createDocument_MemberNotFound() {
        Document document = new Document();
        UUID authorId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        document.setAuthor(new Member());
        document.getAuthor().setId(authorId);

        document.setProject(new Project());
        document.getProject().setId(projectId);

        when(memberService.getMemberById(authorId)).thenReturn(null);

        assertThrows(MemberNotFound.class, () -> documentService.createDocument(document));
        verify(memberService, times(1)).getMemberById(authorId);
        verify(documentRepository, never()).save(any());
    }

    @Test
    void createDocument_ProjectNotFound() {
        Document document = new Document();
        Member author = new Member("John", "J@email.com", "MANAGER", null, null);

        UUID authorId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        author.setId(authorId);

        document.setAuthor(author);
        document.setProject(new Project());
        document.getProject().setId(projectId);

        when(memberService.getMemberById(authorId)).thenReturn(author);
        when(projectService.getProjectById(projectId)).thenReturn(null);

        assertThrows(ProjectNotFound.class, () -> documentService.createDocument(document));
        verify(memberService, times(1)).getMemberById(authorId);
        verify(projectService, times(1)).getProjectById(projectId);
        verify(documentRepository, never()).save(any());
    }


    @Test
    void getDocuments() {
        List<Document> mockDocuments = List.of(
                new Document("Title 1", "Content 1", null, null),
                new Document("Title 2", "Content 2", null, null)
        );

        when(documentRepository.findAll()).thenReturn(mockDocuments);

        List<Document> documents = documentService.getDocuments();

        assertNotNull(documents);
        assertEquals(2, documents.size());
        assertEquals("Title 1", documents.get(0).getTitle());
        verify(documentRepository, times(1)).findAll();
    }

    @Test
    void getDocumentById() {
        UUID documentId = UUID.randomUUID();
        Document mockDocument = new Document("Title", "Content", null, null);
        mockDocument.setId(documentId);

        when(documentRepository.findById(documentId)).thenReturn(java.util.Optional.of(mockDocument));

        Document document = documentService.getDocumentById(documentId);

        assertNotNull(document);
        assertEquals("Title", document.getTitle());
        assertEquals(documentId, document.getId());
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    void updateDocument() {
        UUID documentId = UUID.randomUUID();
        Document mockDocument = new Document("Old Title", "Old Content", null, null);
        mockDocument.setId(documentId);

        Document updatedData = new Document("New Title", "New Content", null, null);

        when(documentRepository.findById(documentId)).thenReturn(java.util.Optional.of(mockDocument));

        Document updatedDocument = documentService.updateDocument(documentId, updatedData);

        assertNotNull(updatedDocument);
        assertEquals("New Title", updatedDocument.getTitle());
        assertEquals("New Content", updatedDocument.getContent());
        verify(documentRepository, times(1)).findById(documentId);
        verify(documentRepository, times(1)).save(mockDocument);
    }

    @Test
    void deleteDocument() {
        UUID documentId = UUID.randomUUID();
        Project project = new Project("Project Title", "Description", null, null, "active", null, null, null, null);
        Document mockDocument = new Document("Title", "Content", null, project);
        mockDocument.setId(documentId);

        project.setDocument(mockDocument);

        when(documentRepository.findById(documentId)).thenReturn(java.util.Optional.of(mockDocument));
        when(projectService.getProjectById(project.getId())).thenReturn(project);

        documentService.deleteDocument(documentId);

        assertNull(project.getDocument());
        verify(documentRepository, times(1)).findById(documentId);
        verify(projectService, times(1)).getProjectById(project.getId());
        verify(documentRepository, times(1)).deleteById(documentId);
    }
}