package ru.yegorr.todolist.service;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.ListRequest;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.exception.*;
import ru.yegorr.todolist.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yegorr.todolist.service.UserSecurityInformation.*;

/**
 * Реализация TaskListService
 */
@Service
@Transactional
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private PagingFilterSortingProvider<TaskListEntity> listProvider;

    private PagingFilterSortingProvider<TaskEntity> taskProvider;

    /**
     * Конструктор
     *
     * @param taskListRepository репозиторий списков
     * @param taskRepository     репозиторий заданий
     * @param userRepository     репозиторий пользователей
     */
    @Autowired
    public TaskListServiceImpl(TaskListRepository taskListRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.taskListRepository = taskListRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ListsResponse getLists(Integer limit, String sort, String filter, Integer offset, UUID userId) throws ApplicationException {
        checkAdminOrThisUser(userId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User %s", userId));
        }

        List<TaskListEntity> listOfLists = listProvider.getResult(limit, offset, sort, filter, userId);

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
    public FullTaskListResponse getList(UUID listId, Integer limit, String sort, String filter, Integer offset, UUID userId)
            throws ApplicationException {
        TaskListEntity taskList = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %s", listId)));
        if (!taskList.getUser().getId().equals(userId)) {
            throw new NotFoundException(String.format("List %s for user %s", listId, userId));
        }
        checkAdminOrThisUser(userId);

        FullTaskListResponse fullTaskListResponse = new FullTaskListResponse();
        fullTaskListResponse.setId(taskList.getId());
        fullTaskListResponse.setName(taskList.getName());
        fullTaskListResponse.setCreationTime(taskList.getCreationTime());
        fullTaskListResponse.setUpdateTime(taskList.getUpdateTime());
        fullTaskListResponse.setColor(taskList.getColor());
        fullTaskListResponse.setPriority(taskList.getPriority());

        long totalTasksCount = taskRepository.countAllByTaskList_Id(listId);

        List<TaskEntity> tasksEntities = taskProvider.getResult(limit, offset, sort, filter, listId);

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
            taskResponse.setUserId(userId);
            tasks.add(taskResponse);
        }
        fullTaskListResponse.setOpenedTasksCount(openedTasksCount);
        fullTaskListResponse.setClosedTasksCount(closedTasksCount);
        fullTaskListResponse.setTasks(tasks);
        fullTaskListResponse.setTotalTasksCount(totalTasksCount);
        fullTaskListResponse.setClosed(closed);
        fullTaskListResponse.setUserId(userId);
        return fullTaskListResponse;
    }

    @Override
    public TaskListResponse createList(ListRequest listRequest, UUID userId) throws ApplicationException {
        checkAdminOrThisUser(userId);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User %s", userId)));
        TaskListEntity taskList = new TaskListEntity();

        LocalDateTime time = LocalDateTime.now();
        taskList.setName(listRequest.getName());
        taskList.setColor(listRequest.getColor() == null ? 0 : listRequest.getColor());
        taskList.setPriority(listRequest.getPriority() == null ? Priority.VERY_LOW : listRequest.getPriority());
        taskList.setCreationTime(time);
        taskList.setUpdateTime(time);
        taskList.setId(UUID.randomUUID());
        taskList.setUser(userEntity);
        taskListRepository.save(taskList);

        return generateTaskListResponse(taskList);
    }

    @Override
    public TaskListResponse changeList(ListRequest listRequest, UUID listId, UUID userId) throws ApplicationException {
        TaskListEntity taskList = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %s", listId)));
        if (!taskList.getUser().getId().equals(userId)) {
            throw new NotFoundException(String.format("List %s for user %s", listId, userId));
        }
        checkAdminOrThisUser(userId);
        taskList.setName(listRequest.getName());
        taskList.setUpdateTime(LocalDateTime.now());
        taskList.setColor(listRequest.getColor() == null ? 0 : listRequest.getColor());
        taskList.setPriority(listRequest.getPriority() == null ? Priority.VERY_LOW : listRequest.getPriority());
        return generateTaskListResponse(taskList);
    }

    @Override
    public void deleteList(UUID listId, UUID userId) throws ApplicationException {
        TaskListEntity taskListEntity = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %s", listId)));
        if (!taskListEntity.getUser().getId().equals(userId)) {
            throw new NotFoundException(String.format("List %s for user %s", listId, userId));
        }
        checkAdminOrThisUser(userId);

        taskListRepository.delete(taskListEntity);
    }

    @Override
    public void deleteAllLists() throws ForbiddenException {
        if (!isAdmin()) {
            throw new ForbiddenException();
        }
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
        taskListResponse.setUserId(entity.getUser().getId());

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

    /**
     * Сеттер для listProvider
     */
    @Autowired
    public void setListProvider(PagingFilterSortingProvider<TaskListEntity> listProvider) {
        this.listProvider = listProvider;
    }

    /**
     * Сеттер для taskProvider
     */
    @Autowired
    public void setTaskProvider(PagingFilterSortingProvider<TaskEntity> taskProvider) {
        this.taskProvider = taskProvider;
    }
}
