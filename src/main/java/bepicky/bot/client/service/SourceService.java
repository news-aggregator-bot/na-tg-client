package bepicky.bot.client.service;

import bepicky.bot.client.feign.SourceServiceClient;
import bepicky.common.domain.response.SourceListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService implements ISourceService {

    @Autowired
    private SourceServiceClient sourceServiceClient;

    @Override
    public SourceListResponse list(long chatId, int page, int size) {
        return sourceServiceClient.list(chatId, page, size);
    }

}
