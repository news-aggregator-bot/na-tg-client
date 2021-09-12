package bepicky.bot.client.service;

import bepicky.bot.client.feign.CategoryServiceClient;
import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryServiceClient categoryClient;

    @Override
    public CategoryListResponse list(long chatId, String type) {
        return categoryClient.list(chatId, type);
    }

    @Override
    public CategoryListResponse list(long chatId, String type, int page, int pageSize) {
        return categoryClient.list(chatId, page, pageSize, type);
    }

    @Override
    public CategoryListResponse list(long chatId, long parentId, int page, int pageSize) {
        return categoryClient.sublist(chatId, parentId, page, pageSize);
    }

}
