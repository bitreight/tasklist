package com.bitreight.tasklist.service.converter.impl;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.TaskPriority;
import com.bitreight.tasklist.service.converter.TaskDtoConverter;
import com.bitreight.tasklist.util.date.DateHelper;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoConverterImpl implements TaskDtoConverter {

    @Override
    public Task convertDto(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(DateHelper.dateFromString(taskDto.getDeadline()));
        task.setPriority(TaskPriority.values()[taskDto.getPriority()]);
        task.setCompleted(taskDto.isCompleted());
        task.setVersion(taskDto.getVersion());
        return task;
    }

    @Override
    public TaskDto convertEntity(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setDeadline(DateHelper.stringFromDate(task.getDeadline()));
        taskDto.setCompleted(task.isCompleted());
        taskDto.setPriority(task.getPriority().ordinal());
        taskDto.setVersion(task.getVersion());
        return taskDto;
    }
}

