package ru.yegorr.todolist.service.paging;

import org.springframework.data.domain.*;

/**
 * Provides pagination by offset and limit
 */
public class OffsetLimitRequest implements Pageable {
    private final int offset;

    private final int limit;

    private final Sort sort;

    /**
     * Constructor
     *
     * @param offset offset
     * @param limit limit
     * @param sort sorting
     */
    public OffsetLimitRequest(int offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be more or equal 0");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("Offset must be more or equal 0");
        }
        if (sort == null) {
            throw new IllegalArgumentException("Sort must not be null");
        }

        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    /**
     * Constructor
     *
     * @param offset offset
     * @param limit limit
     */
    public OffsetLimitRequest(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetLimitRequest(offset + limit, limit, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        int newOffset = offset - limit;
        if (newOffset < 0) {
            newOffset = 0;
        }
        return new OffsetLimitRequest(newOffset, limit, sort);
    }

    @Override
    public Pageable first() {
        return new OffsetLimitRequest(0, limit, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset != 0;
    }
}
