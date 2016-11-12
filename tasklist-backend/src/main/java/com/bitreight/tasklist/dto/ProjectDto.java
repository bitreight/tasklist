package com.bitreight.tasklist.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProjectDto {

    private int id;

    @NotNull
    @Size(min = 1, max = 20)
    private String title;

    @Size(max = 500)
    private String description;

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
}