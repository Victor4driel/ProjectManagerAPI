package com.vabp.project.service;

import com.vabp.project.model.Document;

import java.util.List;
import java.util.UUID;

public interface DocumentService {

    Document createDocument(Document document);

    List<Document> getDocuments();

    Document getDocumentById(UUID id);

    Document updateDocument(UUID id, Document document);

    void deleteDocument(UUID id);
}
