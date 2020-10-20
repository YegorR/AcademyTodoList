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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of TaskListService
 */
@Service
@Transactional
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    private final TaskRepository taskRepository;

    private ListsSorter listsSorter;

    private final ActionParser listsActionParser, tasksActionParser;

    /**
     * Constructor
     *
     * @param taskListRepository taskListRepository
     * @param taskRepository     taskRepository
     */
    @Autowired
    public TaskListServiceImpl(TaskListRepository taskListRepository, TaskRepository taskRepository) {
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;

        listsActionParser = new ActionParser(
                Map.of("creationDate", ActionParser.PropertyType.DATE,
                        "updateDate", ActionParser.PropertyType.DATE,
                        "name", ActionParser.PropertyType.STRING
                ),
                Map.of("creation_date", "creationDate", "update_date", "updateDate")
        );

        tasksActionParser = new ActionParser(
                Map.of("creationDate", ActionParser.PropertyType.DATE,
                        "updateDate", ActionParser.PropertyType.DATE,
                        "name", ActionParser.PropertyType.STRING,
                        "priority", ActionParser.PropertyType.INTEGER,
                        "done", ActionParser.PropertyType.BOOLEAN
                ),
                Map.of("creation_date", "creationDate", "update_date", "updateDate")
        );
    }

    @Override
    public ListsResponse getLists(Integer limit, String sort, String filter, Integer offset) throws ValidationFailsException {
        if (limit == null) {
            limit = 10;
        } else if (limit > 100) {
            limit = 10;
        }
        if (offset == null) {
            offset = 0;
        }

        List<Sort.Order> orders = listsSorter.handleSortQuery(sort, Map.of(
                "update_date", "updateDate", "creation_date", "creationDate", "name", "name"
        ));
        if (orders == null) {
            orders = List.of(Sort.Order.desc("updateDate"));
        }

        List<TaskListEntity> listOfLists = taskListRepository.findAll(
                new FilterSpecification<>(listsActionParser.parse(filter)),
                new OffsetLimitRequest(offset, limit, Sort.by(orders))
        ).toList();
        OpenedAndClosedListsCount openedAndClosedLists = countOpenedAndClosedLists(listOfLists);
        List<TaskListResponse> taskListResponseList = listOfLists.stream()
                .map(TaskListServiceImpl::generateTaskListResponse)
                .collect(Collectors.toList());
        ListsResponse listsResponse = new ListsResponse();
        listsResponse.setOpenedListsCount(openedAndClosedLists.getOpenedListsCount());
        listsResponse.setClosedListCount(openedAndClosedLists.getClosedListsCount());
        listsResponse.setLists(taskListResponseList);
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
    public FullTaskListResponse getList(long listId, Integer limit, String sort, String filter, Integer offset)
            throws NotFoundException, ValidationFailsException {
        TaskListEntity taskList = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %d", listId)));

        List<Sort.Order> orders = listsSorter.handleSortQuery(sort, Map.of(
                "update_date", "updateDate", "creation_date", "creationDate", "name", "name",
                "done", "done", "priority", "priority"
        ));
        if (orders == null) {
            orders = List.of(Sort.Order.desc("updateDate"));
        }

        FullTaskListResponse fullTaskListResponse = new FullTaskListResponse();
        fullTaskListResponse.setId(taskList.getId());
        fullTaskListResponse.setName(taskList.getName());
        fullTaskListResponse.setCreationDate(taskList.getCreationDate());
        fullTaskListResponse.setUpdateDate(taskList.getUpdateDate());

        if (limit == null) {
            limit = 10;
        } else if (limit > 100) {
            limit = 10;
        }
        if (offset == null) {
            offset = 0;
        }

        List<TaskEntity> tasksEntities = taskRepository.findAll(
                new FilterSpecification<>(tasksActionParser.parse(filter), listId, "taskList"),
                new OffsetLimitRequest(offset, limit, Sort.by(orders))
        ).stream().collect(Collectors.toList());

        List<TaskResponse> tasks = new ArrayList<>();
        int openedTasksCount = 0;
        int closedTasksCount = 0;
        for (TaskEntity task : tasksEntities) {
            if (task.isDone()) {
                closedTasksCount++;
            } else {
                openedTasksCount++;
            }
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setId(task.getId());
            taskResponse.setListId(taskList.getId());
            taskResponse.setCreationDate(task.getCreationDate());
            taskResponse.setUpdateDate(task.getUpdateDate());
            taskResponse.setDescription(task.getDescription());
            taskResponse.setDone(task.isDone());
            taskResponse.setPriority(task.getPriority());
            taskResponse.setName(task.getName());
            tasks.add(taskResponse);
        }
        fullTaskListResponse.setOpenedTasksCount(openedTasksCount);
        fullTaskListResponse.setClosedTasksCount(closedTasksCount);
        fullTaskListResponse.setTasks(tasks);
        return fullTaskListResponse;
    }

    @Override
    public TaskListResponse createList(ListRequest listRequest) {
        TaskListEntity taskList = new TaskListEntity();

        LocalDate date = LocalDate.now();
        taskList.setName(listRequest.getName());
        taskList.setCreationDate(date);
        taskList.setUpdateDate(date);
        taskListRepository.save(taskList);

        return generateTaskListResponse(taskList);
    }

    @Override
    public TaskListResponse changeList(ListRequest listRequest, long listId) throws NotFoundException {
        TaskListEntity taskList = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %d", listId)));
        taskList.setName(listRequest.getName());
        taskList.setUpdateDate(LocalDate.now());
        return generateTaskListResponse(taskList);
    }

    @Override
    public void deleteList(long listId) throws NotFoundException {
        if (!taskListRepository.existsById(listId)) {
            throw new NotFoundException(String.format("List %d", listId));
        }

        taskListRepository.deleteById(listId);
    }

    private static TaskListResponse generateTaskListResponse(TaskListEntity entity) {
        TaskListResponse taskListResponse = new TaskListResponse();
        taskListResponse.setId(entity.getId());
        taskListResponse.setName(entity.getName());
        taskListResponse.setCreationDate(entity.getCreationDate());
        taskListResponse.setUpdateDate(entity.getUpdateDate());
        return taskListResponse;
    }

    /**
     * Set listsSorter
     *
     * @param listsSorter listSorter
     */
    @Autowired
    public void setListsSorter(ListsSorter listsSorter) {
        this.listsSorter = listsSorter;
    }
}
