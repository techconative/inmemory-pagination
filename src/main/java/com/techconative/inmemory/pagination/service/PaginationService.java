package com.techconative.inmemory.pagination.service;

import com.techconative.inmemory.pagination.core.IPaginationService;
import com.techconative.inmemory.pagination.modal.PageResult;
import com.techconative.inmemory.pagination.modal.PaginationCriteria;
import com.techconative.inmemory.pagination.util.InmemoryFOPS;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class PaginationService<T> implements IPaginationService {

    /**
     * The central method to extend the library as a service.
     *
     * @param criteria contains filter, search, sort and pagination constraints
     * @return PageResult of the provided data as per constraints
     * @since 1.0.0
     */
    @Override
    public PageResult getPageResult(PaginationCriteria criteria) {

        List<T> rawData = getRawData();

        return InmemoryFOPS.processData(rawData, criteria);
    }

    /**
     * Method to fetch data for processing
     *
     * @return List of data
     * @since 1.0.0
     */
    protected abstract List<T> getRawData();
}
