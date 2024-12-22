package com.vabp.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private Date startDate;

    private Date endDate;

    private String status;

    @ManyToOne
    @JsonIgnoreProperties(value = {"projects", "tasks"})
    private Member manager;

    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties(value = {"projects", "tasks"})
    private List<Member> teamMembers;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    @OneToOne
    @JsonIgnoreProperties("project")
    private Document document;

    public Project() {
    }

    public Project(String name, String description, Date startDate, Date endDate, String status, Member manager, List<Member> teamMembers, List<Task> tasks, Document document) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.manager = manager;
        this.teamMembers = teamMembers;
        this.tasks = tasks;
        this.document = document;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Member getManager() {
        return manager;
    }

    public void setManager(Member manager) {
        this.manager = manager;
    }

    public List<Member> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Member> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Member addMemberToProject(Member member) {
        this.getTeamMembers().add(member);

        return member;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
