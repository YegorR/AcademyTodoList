package ru.yegorr.todolist.service;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.ListRequest;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.exception.*;
import ru.yegorr.todolist.repository.*;
import ru.yegorr.todolist.service.filtering.*;
import ru.yegorr.todolist.service.paging.OffsetLimitRequest;
import ru.yegorr.todolist.service.sorting.ListsSorter;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация TaskListService
 */
@Service
@Transactional
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    private final TaskRepository taskRepository;

    private final ListsSorter listsSorter, tasksSorter;

    private final ActionParser listsActionParser, tasksActionParser;

    private static final int DEFAULT_LIMIT = 10, MAX_LIMIT = 100;

    private static final int DEFAULT_OFFSET = 0;

    private static final String CREATION_TIME_PROPERTY = "creationTime", UPDATE_TIME_PROPERTY = "updateTime", NAME_PROPERTY = "name",
            PRIORITY_PROPERTY = "priority", DONE_PROPERTY = "done", DESTINATION_DATE_PROPERTY = "destinationDate";

    private static final String CREATION_TIME_QUERY = "creation_time", UPDATE_TIME_QUERY = "update_time", DESTINATION_DATE_QUERY = "destination_date";

    /**
     * Конструктор
     *
     * @param taskListRepository репозиторий списков
     * @param taskRepository     репозиторий заданий
     */
    @Autowired
    public TaskListServiceImpl(TaskListRepository taskListRepository, TaskRepository taskRepository) {
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;

        Map<String, String> propertyMapping = Map.of(CREATION_TIME_QUERY, CREATION_TIME_PROPERTY, UPDATE_TIME_QUERY, UPDATE_TIME_PROPERTY,
                DESTINATION_DATE_QUERY, DESTINATION_DATE_PROPERTY
        );

        listsActionParser = new ActionParser(
                Map.of(CREATION_TIME_PROPERTY, ActionParser.PropertyType.TIME,
                        UPDATE_TIME_PROPERTY, ActionParser.PropertyType.TIME,
                        NAME_PROPERTY, ActionParser.PropertyType.STRING,
                        PRIORITY_PROPERTY, ActionParser.PropertyType.PRIORITY
                ),
                propertyMapping
        );

        tasksActionParser = new ActionParser(
                Map.of(CREATION_TIME_PROPERTY, ActionParser.PropertyType.TIME,
                        UPDATE_TIME_PROPERTY, ActionParser.PropertyType.TIME,
                        NAME_PROPERTY, ActionParser.PropertyType.STRING,
                        PRIORITY_PROPERTY, ActionParser.PropertyType.PRIORITY,
                        DONE_PROPERTY, ActionParser.PropertyType.BOOLEAN,
                        DESTINATION_DATE_PROPERTY, ActionParser.PropertyType.DATE
                ),
                propertyMapping
        );

        listsSorter = new ListsSorter(Set.of(CREATION_TIME_PROPERTY, UPDATE_TIME_PROPERTY, NAME_PROPERTY, PRIORITY_PROPERTY), propertyMapping);
        tasksSorter = new ListsSorter(Set.of(
                CREATION_TIME_PROPERTY, UPDATE_TIME_PROPERTY, NAME_PROPERTY, PRIORITY_PROPERTY, DONE_PROPERTY, DESTINATION_DATE_PROPERTY
        ), propertyMapping);
    }

    @Override
    public ListsResponse getLists(Integer limit, String sort, String filter, Integer offset) throws ValidationFailsException {
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        } else if (limit > MAX_LIMIT) {
            limit = DEFAULT_LIMIT;
        }

        if (offset == null) {
            offset = 0;
        }

        List<Sort.Order> orders = listsSorter.handleSortQuery(sort);
        if (orders == null) {
            orders = List.of(Sort.Order.desc(UPDATE_TIME_PROPERTY));
        }

        List<TaskListEntity> listOfLists = taskListRepository.findAll(
                new FilterSpecification<>(listsActionParser.parse(filter)),
                new OffsetLimitRequest(offset, limit, Sort.by(orders))
        ).toList();

        long totalListCount = taskListRepository.count();
        OpenedAndClosedListsCount openedAndClosedLists = countOpenedAndClosedLists(listOfLists);
        List<TaskListResponse> taskListResponseList = listOfLists.stream()
                .map(TaskListServiceImpl::generateTaskListResponse)
                .collect(Collectors.toList());
        ListsResponse listsResponse = new ListsResponse();
        listsResponse.setOpenedListsCount(openedAndClosedLists.getOpenedListsCount());
        listsResponse.setClosedListCount(openedAndClosedLists.getClosedListsCount());
        listsResponse.setLists(taskListResponseList);
        listsResponse.setTotalListsCount(totalListCount);
        return listsResponse;
    }

    @Value
    private static class OpenedAndClosedListsCount {

        int openedListsCount;

        int closedListsCount;
    }

    private static OpenedAndClosedListsCount countOpenedAndClosedLists(List<TaskListEntity> lists) {
        int openedListsCount = 0;
        int closedListsCount = 0;
        for (TaskListEntity taskList : lists) {
            if (taskList.getTasks().isEmpty()) {
                continue;
            }
            if (isListOpened(taskList)) {
                openedListsCount++;
            } else {
                closedListsCount++;
            }
        }
        return new OpenedAndClosedListsCount(openedListsCount, closedListsCount);
    }

    private static boolean isListOpened(TaskListEntity taskList) {
        for (TaskEntity task : taskList.getTasks()) {
            if (!task.isDone()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FullTaskListResponse getList(UUID listId, Integer limit, String sort, String filter, Integer offset)
            throws NotFoundException, ValidationFailsException {
        TaskListEntity taskList = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %s", listId)));

        List<Sort.Order> orders = tasksSorter.handleSortQuery(sort);
        if (orders == null) {
            orders = List.of(Sort.Order.desc(UPDATE_TIME_PROPERTY));
        }

        FullTaskListResponse fullTaskListResponse = new FullTaskListResponse();
        fullTaskListResponse.setId(taskList.getId());
        fullTaskListResponse.setName(taskList.getName());
        fullTaskListResponse.setCreationTime(taskList.getCreationTime());
        fullTaskListResponse.setUpdateTime(taskList.getUpdateTime());
        fullTaskListResponse.setColor(taskList.getColor());
        fullTaskListResponse.setPriority(taskList.getPriority());

        long totalTasksCount = taskRepository.countAllByTaskList_Id(listId);

        if (limit == null) {
            limit = DEFAULT_LIMIT;
        } else if (limit > MAX_LIMIT) {
            limit = DEFAULT_LIMIT;
        }
        if (offset == null) {
            offset = DEFAULT_OFFSET;
        }

        List<TaskEntity> tasksEntities = taskRepository.findAll(
                new FilterSpecification<>(tasksActionParser.parse(filter), listId, "taskList"),
                new OffsetLimitRequest(offset, limit, Sort.by(orders))
        ).stream().collect(Collectors.toList());

        List<TaskResponse> tasks = new ArrayList<>();
        int openedTasksCount = 0;
        int closedTasksCount = 0;
        boolean closed = true;
        for (TaskEntity task : tasksEntities) {
            if (task.isDone()) {
                closedTasksCount++;
            } else {
                openedTasksCount++;
                closed = false;
            }
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setId(task.getId());
            taskResponse.setListId(taskList.getId());
            taskResponse.setCreationTime(task.getCreationTime());
            taskResponse.setUpdateTime(task.getUpdateTime());
            taskResponse.setDescription(task.getDescription());
            taskResponse.setDone(task.isDone());
            taskResponse.setPriority(task.getPriority());
            taskResponse.setName(task.getName());
            taskResponse.setDestinationDate(task.getDestinationDate());
            tasks.add(taskResponse);
        }
        fullTaskListResponse.setOpenedTasksCount(openedTasksCount);
        fullTaskListResponse.setClosedTasksCount(closedTasksCount);
        fullTaskListResponse.setTasks(tasks);
        fullTaskListResponse.setTotalTasksCount(totalTasksCount);
        fullTaskListResponse.setClosed(closed);
        return fullTaskListResponse;
    }

    @Override
    public TaskListResponse createList(ListRequest listRequest) {
        TaskListEntity taskList = new TaskListEntity();

        LocalDateTime time = LocalDateTime.now();
        taskList.setName(listRequest.getName());
        taskList.setColor(listRequest.getColor() == null ? 0 : listRequest.getColor());
        taskList.setPriority(listRequest.getPriority() == null ? Priority.VERY_LOW : listRequest.getPriority());
        taskList.setCreationTime(time);
        taskList.setUpdateTime(time);
        taskList.setId(UUID.randomUUID());
        taskListRepository.save(taskList);

        return generateTaskListResponse(taskList);
    }

    @Override
    public TaskListResponse changeList(ListRequest listRequest, UUID listId) throws NotFoundException {
        TaskListEntity taskList = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %s", listId)));
        taskList.setName(listRequest.getName());
        taskList.setUpdateTime(LocalDateTime.now());
        taskList.setColor(listRequest.getColor() == null ? 0 : listRequest.getColor());
        taskList.setPriority(listRequest.getPriority() == null ? Priority.VERY_LOW : listRequest.getPriority());
        return generateTaskListResponse(taskList);
    }

    @Override
    public void deleteList(UUID listId) throws NotFoundException {
        if (!taskListRepository.existsById(listId)) {
            throw new NotFoundException(String.format("List %s", listId));
        }

        taskListRepository.deleteById(listId);
    }

    @Override
    public void deleteAllLists() {
        taskListRepository.deleteAll();
    }

    private static TaskListResponse generateTaskListResponse(TaskListEntity entity) {
        TaskListResponse taskListResponse = new TaskListResponse();
        taskListResponse.setId(entity.getId());
        taskListResponse.setName(entity.getName());
        taskListResponse.setCreationTime(entity.getCreationTime());
        taskListResponse.setUpdateTime(entity.getUpdateTime());
        taskListResponse.setColor(entity.getColor());
        taskListResponse.setPriority(entity.getPriority());

        boolean closed = true;
        if (entity.getTasks() != null) {
            for (TaskEntity task : entity.getTasks()) {
                if (!task.isDone()) {
                    closed = false;
                    break;
                }
            }
        }

        taskListResponse.setClosed(closed);
        return taskListResponse;
    }
}
