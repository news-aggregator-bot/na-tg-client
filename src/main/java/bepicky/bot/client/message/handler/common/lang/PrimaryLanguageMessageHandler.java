package bepicky.bot.client.message.handler.common.lang;

import bepicky.bot.client.message.async.NatsMessageFinisher;
import bepicky.bot.client.message.button.MenuKeyboardBuilder;
import bepicky.bot.client.message.button.ReplyMarkupBuilder;
import bepicky.bot.client.message.nats.StartFinishMessagePublisher;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.core.BotRouter;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.data.DataTransformer;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.msg.LanguageCommandMsg;
import bepicky.common.msg.MsgCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class PrimaryLanguageMessageHandler implements MessageHandler, NatsMessageFinisher {

    @Autowired
    private StartFinishMessagePublisher startFinishMessagePublisher;

    @Autowired
    private DataTransformer<byte[]> byteTransformer;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    private MenuKeyboardBuilder menuKeyboardBuilder;

    @Autowired
    private BotRouter router;

    @Value("${topics.reader.language.start}")
    private String readerLangStart;

    @Value("${topics.reader.language.finish}")
    private String readerLangFinish;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {

        LanguageCommandMsg lang = new LanguageCommandMsg(
            cc.getChatId(),
            getKey(cc.getTrigger()),
            MsgCommand.PICK
        );
        startFinishMessagePublisher.publish(readerLangStart, finishSubject(), lang);
        return null;
    }

    @Override
    public void finish(byte[] msg) {
        ReaderDto reader = byteTransformer.transform(msg, ReaderDto.class);
        String text = templateContext.processTemplate(
            TemplateNames.PICK_READER_LANG,
            reader.getLang()
        );
        ReplyMarkupBuilder menu = menuKeyboardBuilder.getMenu(reader.getLang());
        router.sendNew(reader.getChatId(), text, menu.build());
    }

    @Override
    public String finishSubject() {
        return readerLangFinish + "." + getKey(trigger());
    }

    private String getKey(String trigger) {
        return trigger.substring(1);
    }
}
