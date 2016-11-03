package com.bitreight.tasklist.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private Date deadline;

    @Column(name = "task_priority")
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projects_id", foreignKey = @ForeignKey(name = "fk_tasks_projects"))
    private Project project;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (isCompleted != task.isCompleted) return false;
        if (title != null ? !title.equals(task.title) : task.title != null) return false;
        if (description != null ? !description.equals(task.description) : task.description != null) return false;
        if (deadline != null ? !deadline.equals(task.deadline) : task.deadline != null) return false;
        if (priority != task.priority) return false;
        return project != null ? project.equals(task.project) : task.project == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }
}
