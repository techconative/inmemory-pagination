package com.techconative.inmemory.pagination;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techconative.inmemory.pagination.core.IPaginationService;
import com.techconative.inmemory.pagination.modal.Feed;
import com.techconative.inmemory.pagination.modal.OrderingCriteria;
import com.techconative.inmemory.pagination.modal.PageResult;
import com.techconative.inmemory.pagination.modal.PaginationCriteria;
import com.techconative.inmemory.pagination.service.PaginationService;

import java.io.IOException;
import java.util.List;


public class FeedService extends PaginationService<Feed> {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    protected List<Feed> getRawData() {

        try {
            return mapper.readValue(this.getClass().getClassLoader()
                            .getResourceAsStream("Feeds_with_500_Records.json"),
                    new TypeReference<List<Feed>>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        IPaginationService t = new FeedService();

        PaginationCriteria criteria = new PaginationCriteria();
//        criteria.setFilter("title#QQQ~name#bharath~createdAt#2020-01-02T10:41:58.617+00:00");
//        criteria.setFilter("createdAt:2020-01");
//        criteria.setFilter("title:QQQ~name:bharath~msg:null");
//        criteria.setFilter("title:QQQ~name:bharath~createdAt:2020-01-02T10:41:58.6177919+00:00");
//        criteria.setFilter("title:QQQ~name:bharath~userId:4051");
//        criteria.setFilter("Gender:Male~State:Ohio");
        criteria.setFilter("likeDislike/userAction#2~title#QQQ");
//        criteria.setFilter("multiMedia/name#AAAA");
        criteria.setLimit(10);
        criteria.setColumn("id");
        criteria.setSort(OrderingCriteria.ASC);
        criteria.setPageNumber(1);
        //https://www.abc.com/users?filter=multimedia.name:krishnan~lastname:gopal&query=techconative&column=email&order=desc
        //criteria.setFilter("firstname:krishnan~lastname:gopal");

        PageResult pageResult = t.getPageResult(criteria);

        System.out.println("pageResult data = " + pageResult.getData());
        System.out.println("pageResult size = " + pageResult.getData().size());
        System.out.println("pageResult total count = " + pageResult.getTotalCount());

    }
}
