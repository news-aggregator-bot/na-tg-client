package bepicky.bot.client.service;

import bepicky.bot.client.feign.TagServiceClient;
import bepicky.common.domain.request.TagRequest;
import bepicky.common.domain.response.TagListResponse;
import bepicky.common.domain.response.TagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService implements ITagService {

    @Autowired
    private TagServiceClient tagServiceClient;

    @Override
    public TagResponse subscribe(Long chatId, String key) {
        TagRequest req = new TagRequest();
        req.setChatId(chatId);
        req.setValue(key);
        return tagServiceClient.subscribe(req);
    }

    @Override
    public TagResponse unsubscribe(Long chatId, String key) {
        TagRequest req = new TagRequest();
        req.setChatId(chatId);
        req.setValue(key);
        return tagServiceClient.unsubscribe(req);
    }

    @Override
    public TagListResponse getAllBy(Long chatId) {
        return tagServiceClient.getAllBy(chatId);
    }
}
