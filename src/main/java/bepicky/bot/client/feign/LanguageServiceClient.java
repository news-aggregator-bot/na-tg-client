package bepicky.bot.client.feign;

import bepicky.common.domain.response.LanguageListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bepicky-service", contextId = "language-client", configuration = FeignClientConfiguration.class)
public interface LanguageServiceClient {

    @GetMapping("/language/list")
    LanguageListResponse list(
        @RequestParam("chat_id") long chatId,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );
}
