package com.vabp.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private UUID id;

    private String name;

    private String description;

    private Date dueDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    @JsonIgnoreProperties(value = {"projects", "tasks"})
    private Member responsible;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties(value = {"manager", "teamMembers", "tasks"})
    private Project project;

    public Task() {
    }

    public Task(String name, String description, Date dueDate, String status, Member responsible, Project project) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.responsible = responsible;
        this.project = project;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Member getResponsible() {
        return responsible;
    }

    public void setResponsible(Member responsible) {
        this.responsible = responsible;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
