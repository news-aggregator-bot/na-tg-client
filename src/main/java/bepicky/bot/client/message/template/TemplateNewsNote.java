package bepicky.bot.client.message.template;

import bepicky.common.domain.dto.NewsNoteDto;
import lombok.Data;

import java.util.Date;

@Data
public class TemplateNewsNote {

    private final String title;
    private final String url;
    private final String source;
    private final String date;

    public TemplateNewsNote(NewsNoteDto dto, String date) {
        this.title = dto.getTitle();
        this.url = dto.getUrl();
        this.source = dto.getSources();
        this.date = date;
    }
}
