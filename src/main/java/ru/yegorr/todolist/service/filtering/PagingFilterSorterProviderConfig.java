package ru.yegorr.todolist.service.filtering;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.Sort;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.repository.*;
import ru.yegorr.todolist.service.PagingFilterSortingProvider;

import java.util.*;

/**
 * Настраивает PagingFilterSorterProvider
 */
@Configuration
public class PagingFilterSorterProviderConfig {

    private static final int DEFAULT_LIMIT = 10, MAX_LIMIT = 100;

    private static final int DEFAULT_OFFSET = 0;

    private static final String CREATION_TIME_PROPERTY = "creationTime", UPDATE_TIME_PROPERTY = "updateTime", NAME_PROPERTY = "name",
            PRIORITY_PROPERTY = "priority", DONE_PROPERTY = "done", DESTINATION_DATE_PROPERTY = "destinationDate";

    private static final String CREATION_TIME_QUERY = "creation_time", UPDATE_TIME_QUERY = "update_time", DESTINATION_DATE_QUERY = "destination_date";

    private final Map<String, String> propertyMapping = Map.of(CREATION_TIME_QUERY, CREATION_TIME_PROPERTY, UPDATE_TIME_QUERY, UPDATE_TIME_PROPERTY,
            DESTINATION_DATE_QUERY, DESTINATION_DATE_PROPERTY
    );

    private final TaskListRepository taskListRepository;

    private final TaskRepository taskRepository;

    /**
     * Конструктор
     */
    @Autowired
    public PagingFilterSorterProviderConfig(TaskListRepository taskListRepository, TaskRepository taskRepository) {
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Генерирует бин для списков
     */
    @Bean
    public PagingFilterSortingProvider<TaskListEntity> listProvider() {
        return new PagingFilterSortingProvider<>(
                MAX_LIMIT, DEFAULT_LIMIT, DEFAULT_OFFSET, Set.of(CREATION_TIME_PROPERTY, UPDATE_TIME_PROPERTY, NAME_PROPERTY, PRIORITY_PROPERTY),
                Sort.Order.desc(UPDATE_TIME_PROPERTY), Map.of(CREATION_TIME_PROPERTY, ActionParser.PropertyType.TIME,
                UPDATE_TIME_PROPERTY, ActionParser.PropertyType.TIME,
                NAME_PROPERTY, ActionParser.PropertyType.STRING,
                PRIORITY_PROPERTY, ActionParser.PropertyType.PRIORITY
        ), propertyMapping, "user", taskListRepository
        );
    }

    /**
     * Генерирует бин для заданий
     */
    @Bean
    public PagingFilterSortingProvider<TaskEntity> taskProvider() {
        return new PagingFilterSortingProvider<>(
                MAX_LIMIT, DEFAULT_LIMIT, DEFAULT_OFFSET, Set.of(
                CREATION_TIME_PROPERTY, UPDATE_TIME_PROPERTY, NAME_PROPERTY, PRIORITY_PROPERTY, DONE_PROPERTY, DESTINATION_DATE_PROPERTY
        ), Sort.Order.desc(UPDATE_TIME_PROPERTY), Map.of(CREATION_TIME_PROPERTY, ActionParser.PropertyType.TIME,
                UPDATE_TIME_PROPERTY, ActionParser.PropertyType.TIME,
                NAME_PROPERTY, ActionParser.PropertyType.STRING,
                PRIORITY_PROPERTY, ActionParser.PropertyType.PRIORITY,
                DONE_PROPERTY, ActionParser.PropertyType.BOOLEAN,
                DESTINATION_DATE_PROPERTY, ActionParser.PropertyType.DATE
        ), propertyMapping, "list", taskRepository
        );
    }
}
