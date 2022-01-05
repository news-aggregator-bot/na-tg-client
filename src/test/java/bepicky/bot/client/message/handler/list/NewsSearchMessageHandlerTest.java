package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.TestEntities;
import bepicky.bot.client.service.INewsService;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.EntityType;
import bepicky.common.domain.dto.NewsNoteDto;
import bepicky.common.domain.response.NewsSearchResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static bepicky.bot.client.TestEntitiesGenerator.generateArticles;

@ExtendWith(MockitoExtension.class)
class NewsSearchMessageHandlerTest extends ListHandlerTestSupport {

    private static final long CHAT_ID = -1L;

    @InjectMocks
    private NewsSearchMessageHandler newsSearchMessageHandler;

    @Mock
    private INewsService newsService;

    @Test
    public void handle_ValidCmd_ReturnExpectedMessage() {
        var key = "key";
        var searchCmd = CallbackCommand.of(
            CommandType.SEARCH, EntityType.NEWS_NOTE, 1, key
        );
        searchCmd.setChatId(CHAT_ID);
        var page = TestEntities.sourcePageDto("source_1");
        var articles = generateArticles(3, List.of(page));
        var searchResponse = buildSearchResponse(
            key,
            searchCmd.getPage(),
            articles
        );
        Mockito.when(newsService.search(
                    Mockito.eq(searchCmd.getChatId()),
                    Mockito.eq(key),
                    Mockito.eq(searchCmd.getPage()),
                    Mockito.anyInt()
                )
            )
            .thenReturn(searchResponse);

        var result = newsSearchMessageHandler.handle(searchCmd);

        Assertions.assertEquals("Search results for key <b>key</b>\n" +
            "Found 100 news notes. Page 1/0\n" +
            "\n" +
            "<a href=\"https://url0\">title0</a>\n" +
            "source_1 / Jan 1, 1970\n" +
            "\n" +
            "<a href=\"https://url1\">title1</a>\n" +
            "source_1 / Jan 1, 1970\n" +
            "\n" +
            "<a href=\"https://url2\">title2</a>\n" +
            "source_1 / Jan 1, 1970\n" +
            "\n" +
            "\n", result.getText());
    }

    @Test
    public void verifyStaticContext() {
        Assertions.assertEquals(CommandType.SEARCH, newsSearchMessageHandler.commandType());
        Assertions.assertEquals(EntityType.NEWS_NOTE, newsSearchMessageHandler.entityType());
    }

    private NewsSearchResponse buildSearchResponse(
        String key,
        int page,
        List<NewsNoteDto> articles
    ) {
        NewsSearchResponse searchResponse = new NewsSearchResponse();
        searchResponse.setKey(key);
        searchResponse.setTotalElements(100);
        searchResponse.setFirst(page == 1);
        searchResponse.setList(articles);
        searchResponse.setReader(TestEntities.readerDto(CHAT_ID, TestEntities.en()));
        return searchResponse;
    }

}