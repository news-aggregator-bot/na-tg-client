package bepicky.bot.client.message.template;

import bepicky.common.domain.dto.NewsNoteDto;
import lombok.Data;

import java.util.Date;

@Data
public class TemplateNewsNote {

    private final String title;
    private final String url;
    private final String source;
    private final Date date;
    private final String region;
    private final String common;

    public TemplateNewsNote(NewsNoteDto dto) {
        this.title = dto.getTitle();
        this.url = dto.getUrl();
        this.source = dto.getSources();
        this.date = dto.getDate();
        this.region = dto.getRegions();
        this.common = dto.getCommons();
    }
}
