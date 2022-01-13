package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.message.template.TemplateNewsNote;
import bepicky.bot.client.service.INewsService;
import bepicky.bot.client.service.IValueNormalisationService;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.EntityType;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsSearchMessageHandler extends AbstractListMessageHandler {

    @Value("${search.news.pageSize}")
    private int searchPageSize;

    @Autowired
    private INewsService newsService;

    @Autowired
    private IValueNormalisationService valueNormalisationService;

    @Override
    public HandleResult handle(CallbackCommand command) {
        var key = String.valueOf(command.getId());
        var searchResponse = newsService.search(
            command.getChatId(),
            key,
            command.getPage(),
            searchPageSize
        );

        var templateDtos = searchResponse.getList()
            .stream()
            .map(dto -> {
                var normalisedDate = valueNormalisationService.normaliseDate(
                    dto.getDate(),
                    searchResponse.getReader().getLang()
                );
                return new TemplateNewsNote(dto, normalisedDate);
            })
            .collect(Collectors.toList());

        String text = templateContext.processTemplate(
            TemplateNames.SEARCH_NOTE, searchResponse.getReader().getLang(),
            ImmutableMap.<String, Object>builder()
                .put("key", key)
                .put("page", command.getPage())
                .put("total_pages", searchResponse.getTotalPages())
                .put("total", searchResponse.getTotalElements())
                .put("notes", templateDtos)
                .build()
        );

        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        List<InlineMarkupBuilder.InlineButton> pagination = pagination(
            command.getPage(),
            searchResponse,
            markup,
            key
        );
        markup.addButtons(pagination);
        return new HandleResult(text, markup.build(), false);
    }

    @Override
    public EntityType entityType() {
        return EntityType.NEWS_NOTE;
    }

    @Override
    public CommandType commandType() {
        return CommandType.SEARCH;
    }

}
