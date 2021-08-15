package bepicky.bot.client.service;

import bepicky.common.domain.response.TagListResponse;
import bepicky.common.domain.response.TagResponse;

public interface ITagService {

    TagResponse subscribe(Long chatId, String key);

    TagResponse unsubscribe(Long chatId, String key);

    TagListResponse getAllBy(Long chatId);
}
