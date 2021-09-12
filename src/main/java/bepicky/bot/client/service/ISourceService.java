package bepicky.bot.client.service;

import bepicky.common.domain.response.SourceListResponse;

public interface ISourceService {

    SourceListResponse list(long chatId, int page, int size);

}
