package ru.yegorr.todolist.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.yegorr.todolist.exception.ValidationFailsException;
import ru.yegorr.todolist.service.filtering.*;
import ru.yegorr.todolist.service.paging.OffsetLimitRequest;
import ru.yegorr.todolist.service.sorting.ListsSorter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Предоставляет единую "точку" для сортирования, пагинации и фильтрования
 *
 * @param <T> entity
 */
public class PagingFilterSortingProvider<T> {

    private final int maxLimit, defaultLimit, defaultOffset;

    private final Set<String> sortProps;

    private final Sort.Order defaultSortOrder;

    private final Map<String, ActionParser.PropertyType> filterProps;

    private final Map<String, String> propsMapping;

    private final String parent;

    private final ActionParser actionParser;

    private final ListsSorter listsSorter;

    private final JpaSpecificationExecutor<T> repository;

    /**
     * Конструктор
     *
     * @param maxLimit максимальный лимит
     * @param defaultLimit лимит по-умолчанию
     * @param defaultOffset смещение по-умолчанию
     * @param sortProps свойства для соритровки
     * @param defaultSortOrder стандартная сортировка
     * @param filterProps свойства для фильтрации
     * @param propsMapping маппинг "свойство в запросе" - "свойство в entity"
     * @param parent свойство родителя
     * @param repository репозиторий
     */
    public PagingFilterSortingProvider(
            int maxLimit, int defaultLimit, int defaultOffset, Set<String> sortProps, Sort.Order defaultSortOrder,
            Map<String, ActionParser.PropertyType> filterProps,
            Map<String, String> propsMapping,
            String parent, JpaSpecificationExecutor<T> repository
    ) {
        this.maxLimit = maxLimit;
        this.defaultLimit = defaultLimit;
        this.defaultOffset = defaultOffset;
        this.sortProps = sortProps;
        this.defaultSortOrder = defaultSortOrder;
        this.filterProps = filterProps;
        this.propsMapping = propsMapping;
        this.parent = parent;
        this.repository = repository;

        actionParser = new ActionParser(filterProps, propsMapping);
        listsSorter = new ListsSorter(sortProps, propsMapping);
    }

    /**
     * Получить множество объектов
     *
     * @param limit лимит
     * @param offset смещение
     * @param sort запрос сортировки
     * @param filter запрос фильтрации
     * @param parentKey id родителя
     * @return список объектов
     * @throws ValidationFailsException если запрос фильтрации или сортировки неверен
     */
    public List<T> getResult(Integer limit, Integer offset, String sort, String filter, UUID parentKey) throws ValidationFailsException {
        if (limit == null || limit > maxLimit) {
            limit = defaultLimit;
        }

        if (offset == null) {
            offset = defaultOffset;
        }

        List<Sort.Order> orderList = listsSorter.handleSortQuery(sort);
        if (orderList == null) {
            orderList = List.of(defaultSortOrder);
        }

        return repository.findAll(
                new FilterSpecification<>(actionParser.parse(filter), parentKey, parent),
                new OffsetLimitRequest(offset, limit, Sort.by(orderList))
        ).stream().collect(Collectors.toList());
    }
}
