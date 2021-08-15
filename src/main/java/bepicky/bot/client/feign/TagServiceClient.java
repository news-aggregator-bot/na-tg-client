package bepicky.bot.client.feign;

import bepicky.common.domain.request.TagRequest;
import bepicky.common.domain.response.TagListResponse;
import bepicky.common.domain.response.TagResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bepicky-service", contextId = "tag-client", configuration = FeignClientConfiguration.class)
public interface TagServiceClient {

    @GetMapping("/tags/search/{key}")
    TagListResponse search(@PathVariable("key") String key);

    @GetMapping("/tags/reader/{chatId}")
    TagListResponse getAllBy(@PathVariable("chatId") Long chatId);

    @PostMapping("/tags/subscribe")
    TagResponse subscribe(@RequestBody TagRequest r);

    @PostMapping("/tags/unsubscribe")
    TagResponse unsubscribe(@RequestBody TagRequest r);
}
