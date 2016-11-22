package com.bitreight.tasklist.service.converter.impl;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.TaskPriority;
import com.bitreight.tasklist.service.converter.GenericDtoConverter;
import com.bitreight.tasklist.service.converter.TaskDtoConverter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TaskDtoConverterImpl implements TaskDtoConverter {

    private static final String DATE_PATTERN = "dd-MM-yyyy";

    @Override
    public Task convertDto(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(stringToSqlDate(taskDto.getDeadline()));
        task.setPriority(TaskPriority.values()[taskDto.getPriority()]);
        task.setVersion(taskDto.getVersion());
        return task;
    }

    @Override
    public TaskDto convertEntity(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setDeadline(sqlDateToString(task.getDeadline()));
        taskDto.setCompleted(task.isCompleted());
        taskDto.setPriority(task.getPriority().ordinal());
        taskDto.setVersion(task.getVersion());
        return taskDto;
    }

    private Date stringToSqlDate(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate localDate = LocalDate.parse(strDate, formatter);
        return Date.valueOf(localDate);
    }

    private String sqlDateToString(Date sqlDate) {
        DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        return formatter.format(sqlDate);
    }
}

