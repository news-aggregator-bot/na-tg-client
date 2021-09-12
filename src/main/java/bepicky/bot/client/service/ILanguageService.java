package bepicky.bot.client.service;

import bepicky.common.domain.response.LanguageListResponse;

public interface ILanguageService {

    LanguageListResponse list(long chatId, int page, int size);

}
