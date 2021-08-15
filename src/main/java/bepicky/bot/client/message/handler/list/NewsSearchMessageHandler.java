package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.message.template.TemplateNewsNote;
import bepicky.bot.client.service.INewsService;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.EntityType;
import bepicky.common.domain.response.NewsSearchResponse;
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

    @Override
    public HandleResult handle(CallbackCommand command) {
        String key = String.valueOf(command.getId());
        NewsSearchResponse searchResponse = newsService.search(
            command.getChatId(),
            key,
            command.getPage(),
            searchPageSize
        );

        List<TemplateNewsNote> templateDtos = searchResponse.getList().stream()
            .map(TemplateNewsNote::new)
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
