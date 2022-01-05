package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.TestEntities;
import bepicky.bot.client.TestEntitiesGenerator;
import bepicky.bot.client.config.TemplateConfig;
import bepicky.bot.client.service.ITagService;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.domain.dto.LanguageDto;
import bepicky.common.domain.response.ErrorResponse;
import bepicky.common.domain.response.TagResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static bepicky.bot.client.TestEntities.CHAT_ID;
import static bepicky.bot.client.TestEntities.ukr;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class TagSubscribeMessageHandlerTest {

    private static String key = "key";
    private static CallbackCommand pickTagCmd = CallbackCommand.of(
        CommandType.PICK, EntityType.TAG, 1, key
    );

    protected TemplateConfig templateConfig = new TemplateConfig();

    @InjectMocks
    private TagSubscribeMessageHandler tagSubscribeMessageHandler;

    @Spy
    protected MessageTemplateContext templateContext =
        new MessageTemplateContext(templateConfig.templateConfiguration());

    @Mock
    private ITagService tagService;

    @Test
    public void handleTagSubscribe_EnReader_ValidTag_BuildCorrectMessageBasedOnResponse() {

        var page = TestEntities.sourcePageDto("source_1");
        var newsArticles = TestEntitiesGenerator.generateArticles(5, List.of(page));
        var tr = new TagResponse();
        tr.setReader(TestEntities.readerDto(CHAT_ID, TestEntities.en()));
        tr.setTag(TestEntities.tagDto(key));
        tr.setNews(newsArticles);

        Mockito.when(tagService.subscribe(Mockito.anyLong(), Mockito.eq(key))).thenReturn(tr);

        var subscriptionResult =
            tagSubscribeMessageHandler.handle(pickTagCmd);

        assertEquals("You've just subscribed for tag #<b>key</b>\n" +
            "\n" +
            "Latest 5 news by #<b>key</b>\n" +
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
            "<a href=\"https://url3\">title3</a>\n" +
            "source_1 / Jan 1, 1970\n" +
            "\n" +
            "<a href=\"https://url4\">title4</a>\n" +
            "source_1 / Jan 1, 1970\n", subscriptionResult.getText());
        assertFalse(subscriptionResult.isPreviewPage());
    }

    @Test
    public void handleTagSubscribe_RuReader_ValidTag_BuildCorrectMessageBasedOnResponse() {
        var page = TestEntities.sourcePageDto("source_1");
        var newsArticles = TestEntitiesGenerator.generateArticles(5, List.of(page));
        var tr = new TagResponse();
        tr.setReader(TestEntities.readerDto(CHAT_ID, TestEntities.ru()));
        tr.setTag(TestEntities.tagDto(key));
        tr.setNews(newsArticles);

        Mockito.when(tagService.subscribe(Mockito.anyLong(), Mockito.eq(key))).thenReturn(tr);

        var subscriptionResult =
            tagSubscribeMessageHandler.handle(pickTagCmd);

        assertEquals("Ты только что подписался на #<b>key</b>\n" +
            "\n" +
            "Последние 5 новостей по тэгу #<b>key</b>\n" +
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
            "<a href=\"https://url3\">title3</a>\n" +
            "source_1 / Jan 1, 1970\n" +
            "\n" +
            "<a href=\"https://url4\">title4</a>\n" +
            "source_1 / Jan 1, 1970\n" +
            "\n", subscriptionResult.getText());
        assertFalse(subscriptionResult.isPreviewPage());
    }

    @Test
    public void handleTagSubscribe_UkrReader_ValidTag_BuildCorrectMessageBasedOnResponse() {
        var page = TestEntities.sourcePageDto("source_1");
        var newsArticles = TestEntitiesGenerator.generateArticles(5, List.of(page));
        var tr = new TagResponse();
        tr.setReader(TestEntities.readerDto(CHAT_ID, TestEntities.ukr()));
        tr.setTag(TestEntities.tagDto(key));
        tr.setNews(newsArticles);

        Mockito.when(tagService.subscribe(Mockito.anyLong(), Mockito.eq(key))).thenReturn(tr);

        var subscriptionResult =
            tagSubscribeMessageHandler.handle(pickTagCmd);

        assertEquals("Ти щойно підписався на #<b>key</b>\n" +
            "\n" +
            "Останні 5 новин за тегом #<b>key</b>\n" +
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
            "<a href=\"https://url3\">title3</a>\n" +
            "source_1 / Jan 1, 1970\n" +
            "\n" +
            "<a href=\"https://url4\">title4</a>\n" +
            "source_1 / Jan 1, 1970\n" +
            "\n", subscriptionResult.getText());
        assertFalse(subscriptionResult.isPreviewPage());
    }

    private static Stream<Arguments> provideInvalidTagResponses() {
        String enTagLimitMsg = "You reached your tag limit amount.\n" +
            "\n" +
            "Unsubscribe from any or upgrade subscription.";
        String ukrTagLimitMsg = "Ти дійшов до свого максимуму у тегах.\n" +
            "\n" +
            "Відпишись від будь якого та спробуй знов.";
        String ruTagLimitMsg = "Ты достиг максимального количества тэгов.\n" +
            "\n" +
            "Отпишись от любого и попробуй снова.";

        TagResponse noReaderTagResponse = new TagResponse();
        ErrorResponse error = new ErrorResponse(400, "tag");
        noReaderTagResponse.setError(error);

        return Stream.of(
            Arguments.of(errorTagResponse(TestEntities.en()), enTagLimitMsg),
            Arguments.of(errorTagResponse(ukr()), ukrTagLimitMsg),
            Arguments.of(errorTagResponse(TestEntities.ru()), ruTagLimitMsg),
            Arguments.of(noReaderTagResponse, "Shit happens!")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTagResponses")
    public void handleTagSubscribe_EnReader_InvalidTag_BuildsErrorMessage(TagResponse tr, String expectedMessage) {
        var key = "key";
        var pickTagCmd = CallbackCommand.of(
            CommandType.PICK, EntityType.TAG, 1, key
        );

        Mockito.when(tagService.subscribe(Mockito.anyLong(), Mockito.eq(key))).thenReturn(tr);

        var subscriptionResult =
            tagSubscribeMessageHandler.handle(pickTagCmd);

        assertEquals(expectedMessage, subscriptionResult.getText());
    }

    private static TagResponse errorTagResponse(LanguageDto readerLang) {
        var tr = new TagResponse();
        var error = new ErrorResponse(403, "tag");
        tr.setError(error);
        tr.setReader(TestEntities.readerDto(CHAT_ID, readerLang));
        return tr;
    }
}