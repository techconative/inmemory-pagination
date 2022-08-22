package com.techconative.inmemory.pagination.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techconative.inmemory.pagination.core.IPaginationService;
import com.techconative.inmemory.pagination.modal.OrderingCriteria;
import com.techconative.inmemory.pagination.modal.PageResult;
import com.techconative.inmemory.pagination.modal.PaginationCriteria;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public abstract class PaginationService<T> implements IPaginationService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public PageResult getPageResult(PaginationCriteria criteria) {

        PageResult pageResult = new PageResult();

        List<Map<String,String>> rawData = convert(getRawData());


        //Step 1 : Apply filtering
        List<Map<String,String>> filteredList = applyFiltering(criteria,rawData);


        //Step 2 : Apply search
        List<Map<String,String>> searchResultList = applySearch(criteria,filteredList);

        //Step 3 : Apply sorting
        LinkedList<Map<String,String>> sortedList = applySorting(criteria, searchResultList);

        //Step 4 : Apply limit and offset
        LinkedList<Map<String,String>> resultList =  applyPagination(criteria,sortedList);


        pageResult.setData(resultList);
        pageResult.setLimit(criteria.getLimit());
        pageResult.setPageNumber(criteria.getPageNumber());
        pageResult.setFilteredCount(resultList.size());
        pageResult.setTotalCount(rawData.size());

        return pageResult;
    }



    private List<Map<String, String>> convert(List rawData){
        return rawData.stream().map(item-> {
            return objectMapper.convertValue(item,Map.class);
        }).toList();
    }
    private List<Map<String, String>> applyFiltering(PaginationCriteria criteria, List<Map<String,String>> rawData) {

        //rawData.stream().anyMatch(map -> map.containsValue(criteria.get))
        //TODO : filter the values based on the cirteria and return the list.
        return rawData;
    }


    private LinkedList<Map<String, String>> applySorting(PaginationCriteria criteria, List<Map<String, String>> filteredList) {
        LinkedList<Map<String, String>> sortedList = null;
        if(criteria.getSort().equals(OrderingCriteria.DESC)){
            sortedList = filteredList.stream()
                    .sorted(Comparator.comparing(m -> String.valueOf(m.get(criteria.getColumn())),
                                                 Comparator.nullsFirst(Comparator.reverseOrder()))).collect(
                            Collectors.toCollection(LinkedList::new));
        }
        else{
            sortedList = filteredList.stream().sorted(Comparator.comparing(m ->String.valueOf(m.get(criteria.getColumn())),
                                                              Comparator.nullsFirst(Comparator.naturalOrder()))).collect(
                    Collectors.toCollection(LinkedList::new));
        }
        return sortedList;
    }

    private List<Map<String, String>> applySearch(PaginationCriteria criteria, List<Map<String, String>> filteredList) {

        //TODO: serach for the result based on criteria and return the list
        return filteredList;
    }

    private LinkedList applyPagination(PaginationCriteria criteria, LinkedList sortedList) {
        int skipCount = (criteria.getPageNumber() - 1) * criteria.getLimit();
        System.out.println("skipcount = " + skipCount);
        return (LinkedList) sortedList.stream().skip(skipCount).limit(criteria.getLimit()).collect(Collectors.toCollection(LinkedList::new));
    }



    protected abstract List<T> getRawData();
}