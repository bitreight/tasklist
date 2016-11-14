package com.bitreight.tasklist.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TaskDto {

    private int id;

    @NotNull
    @Size(min = 1, max = 45)
    private String title;

    @Size(max = 500)
    private String description;

    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}")
    private String deadline;

    @Min(0)
    @Max(2)
    private int priority;

    private boolean isCompleted;

    @Min(1)
    private long version;

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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDto taskDto = (TaskDto) o;

        if (id != taskDto.id) return false;
        if (priority != taskDto.priority) return false;
        if (isCompleted != taskDto.isCompleted) return false;
        if (version != taskDto.version) return false;
        if (title != null ? !title.equals(taskDto.title) : taskDto.title != null) return false;
        if (description != null ? !description.equals(taskDto.description) : taskDto.description != null) return false;
        return deadline != null ? deadline.equals(taskDto.deadline) : taskDto.deadline == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
        result = 31 * result + priority;
        result = 31 * result + (isCompleted ? 1 : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "TaskDto [" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                ", priority=" + priority +
                ", isCompleted=" + isCompleted +
                ", version=" + version +
                ']';
    }
}