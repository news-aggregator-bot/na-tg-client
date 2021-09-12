package bepicky.bot.client.service;

import bepicky.common.domain.response.CategoryListResponse;

public interface ICategoryService {

    CategoryListResponse list(long chatId, String type);

    CategoryListResponse list(long chatId, String type, int page, int pageSize);

    CategoryListResponse list(long chatId, long parentId, int page, int pageSize);
}
