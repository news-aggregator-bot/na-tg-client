package bepicky.bot.client.service;

import bepicky.bot.client.feign.LanguageServiceClient;
import bepicky.common.domain.response.LanguageListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageService implements ILanguageService {

    @Autowired
    private LanguageServiceClient langClient;

    @Override
    public LanguageListResponse list(long chatId, int page, int size) {
        return langClient.list(chatId, page, size);
    }

}
