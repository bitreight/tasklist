package com.bitreight.tasklist.converter;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.entity.Task;
import com.bitreight.tasklist.entity.TaskPriority;
import com.bitreight.tasklist.service.converter.TaskDtoConverter;
import com.bitreight.tasklist.service.converter.impl.TaskDtoConverterImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTaskDtoConverter {

    private TaskDtoConverter taskConverter = new TaskDtoConverterImpl();

    private static TaskDto taskDto = new TaskDto();
    private static Task task = new Task();

    private static List<Task> tasks = new ArrayList<>(1);
    private static List<TaskDto> taskDtos = new ArrayList<>(1);

    @BeforeClass
    public static void setUpBeforeClass() {
        taskDto.setId(1);
        taskDto.setTitle("test_task");
        taskDto.setDescription("test_desc");
        taskDto.setPriority(0);
        taskDto.setDeadline("22-11-2016");
        taskDto.setVersion(5);

        LocalDate localDate = LocalDate.of(2016, 11, 22);
        task.setId(1);
        task.setTitle("test_task");
        task.setDescription("test_desc");
        task.setPriority(TaskPriority.HIGH);
        task.setDeadline(Date.valueOf(localDate));
        task.setVersion(5);
    }

    @Test
    public void testConvertTaskDto() {
        Task actualTask = taskConverter.convertDto(taskDto);
        assertEquals(actualTask, task);
    }

    @Test
    public void testConvertTaskEntity() {
        TaskDto actualTaskDto = taskConverter.convertEntity(task);
        assertEquals(actualTaskDto, taskDto);
    }

    @Test
    public void testConvertTaskDtos() {
        List<Task> actualTasks = taskConverter.convertDtos(taskDtos);
        assertEquals(actualTasks, tasks);
    }

    @Test
    public void testConvertTaskEntities() {
        List<TaskDto> actualTasksDtos = taskConverter.convertEntities(tasks);
        assertEquals(actualTasksDtos, taskDtos);
    }

    @Test
    public void testConvertTaskDtos_emptyList() {
        List<Task> actualTasks = taskConverter.convertDtos(new ArrayList<>(0));
        assertTrue(actualTasks.isEmpty());
    }

    @Test
    public void testConvertTaskEntities_emptyList() {
        List<TaskDto> actualTaskDtos = taskConverter.convertEntities(new ArrayList<>(0));
        assertTrue(actualTaskDtos.isEmpty());
    }
}