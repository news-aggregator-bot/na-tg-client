package bepicky.bot.client;

import bepicky.common.domain.dto.LanguageDto;
import bepicky.common.domain.dto.NewsNoteDto;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.dto.SourcePageDto;
import bepicky.common.domain.dto.TagDto;

import java.util.Date;
import java.util.List;

public class TestEntities {

    public static final Long CHAT_ID = -1L;

    public static NewsNoteDto newsArticle(Long noteId, List<SourcePageDto> pages) {
        NewsNoteDto newsNoteDto = new NewsNoteDto();
        newsNoteDto.setNoteId(noteId);
        newsNoteDto.setDate(new Date(newsNoteDto.getNoteId()));
        newsNoteDto.setUrl("https://url" + newsNoteDto.getNoteId());
        newsNoteDto.setTitle("title" + newsNoteDto.getNoteId());
        newsNoteDto.setSourcePages(pages);
        return newsNoteDto;
    }

    public static ReaderDto readerDto(Long chatId, LanguageDto primary) {
        ReaderDto dto = new ReaderDto();
        dto.setChatId(chatId);
        dto.setPrimaryLanguage(primary);
        return dto;
    }

    public static LanguageDto en() {
        LanguageDto en = new LanguageDto();
        en.setLang("en");
        en.setName("en");
        en.setLocalized("English");
        return en;
    }

    public static LanguageDto ru() {
        LanguageDto ru = new LanguageDto();
        ru.setLang("ru");
        ru.setName("ru");
        ru.setLocalized("ru");
        return ru;
    }

    public static LanguageDto ukr() {
        LanguageDto ukr = new LanguageDto();
        ukr.setLang("ukr");
        ukr.setName("ukr");
        ukr.setLocalized("ukr");
        return ukr;
    }

    public static SourcePageDto sourcePageDto(String sourceName) {
        SourcePageDto sourcePageDto = new SourcePageDto();
        sourcePageDto.setSourceName(sourceName);
        sourcePageDto.setUrl("https://url");
        sourcePageDto.setLanguages(List.of(en()));
        return sourcePageDto;
    }

    public static TagDto tagDto(String value) {
        TagDto tagDto = new TagDto();
        tagDto.setValue(value);
        return tagDto;
    }
}
