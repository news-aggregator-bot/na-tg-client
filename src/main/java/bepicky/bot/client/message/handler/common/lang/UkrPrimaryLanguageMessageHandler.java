package bepicky.bot.client.message.handler.common.lang;

import org.springframework.stereotype.Component;

@Component
public class UkrPrimaryLanguageMessageHandler extends PrimaryLanguageMessageHandler {

    @Override
    public String trigger() {
        return "/ukr";
    }
}
