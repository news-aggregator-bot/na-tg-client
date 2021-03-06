package bepicky.bot.client.feign;

import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bepicky-service", contextId = "category-client", configuration = FeignClientConfiguration.class)
public interface CategoryServiceClient {

    @GetMapping("/category/{type}/reader/{chatId}/list")
    CategoryListResponse list(
        @PathVariable("chatId") long chatId,
        @PathVariable("type") String type
    );

    @GetMapping("/category/list")
    CategoryListResponse list(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("type") String type
    );

    @GetMapping("/category/list")
    CategoryListResponse sublist(
        @RequestParam("chat_id") long chatId,
        @RequestParam("parent_id") long parentId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

}
