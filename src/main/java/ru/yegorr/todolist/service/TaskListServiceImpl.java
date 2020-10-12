package ru.yegorr.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yegorr.todolist.dto.request.ListRequest;
import ru.yegorr.todolist.dto.response.*;
import ru.yegorr.todolist.entity.*;
import ru.yegorr.todolist.exception.NotFoundException;
import ru.yegorr.todolist.repository.TaskListRepository;

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

    /**
     * Constructor
     *
     * @param taskListRepository taskListRepository
     */
    @Autowired
    public TaskListServiceImpl(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    // TODO: limit, sort, filter, opened and closed count
    @Override
    public ListsResponse getLists(Byte limit, String sort, String filter) {
        List<TaskListResponse> listOfLists = taskListRepository.findAll().stream()
                .map(TaskListServiceImpl::generateTaskListResponse)
                .collect(Collectors.toList());
        ListsResponse listsResponse = new ListsResponse();
        listsResponse.setOpenedListsCount(-1);
        listsResponse.setClosedListCount(-1);
        listsResponse.setLists(listOfLists);
        return listsResponse;
    }

    // TODO: task sorting
    @Override
    public FullTaskListResponse getList(long listId) throws NotFoundException {
        TaskListEntity taskList = taskListRepository.findById(listId).orElseThrow(() -> new NotFoundException(String.format("List %d", listId)));

        FullTaskListResponse fullTaskListResponse = new FullTaskListResponse();
        fullTaskListResponse.setId(taskList.getId());
        fullTaskListResponse.setName(taskList.getName());
        fullTaskListResponse.setCreationDate(taskList.getCreationDate());
        fullTaskListResponse.setUpdateDate(taskList.getUpdateDate());

        List<TaskResponse> tasks = new ArrayList<>();
        int openedTasksCount = 0;
        int closedTasksCount = 0;
        for (TaskEntity task : taskList.getTasks()) {
            if (task.isDone()) {
                openedTasksCount++;
            } else {
                closedTasksCount++;
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
        return null;
    }

    @Override
    public void deleteList(long listId) throws NotFoundException {
    }

    private static TaskListResponse generateTaskListResponse(TaskListEntity entity) {
        TaskListResponse taskListResponse = new TaskListResponse();
        taskListResponse.setId(entity.getId());
        taskListResponse.setName(entity.getName());
        taskListResponse.setCreationDate(entity.getCreationDate());
        taskListResponse.setUpdateDate(entity.getUpdateDate());
        return taskListResponse;
    }
}
