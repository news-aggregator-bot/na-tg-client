package bepicky.bot.client.message.async;

import bepicky.bot.core.cmd.CallbackCommand;

public interface NatsMessageStarter {
    void start(CallbackCommand cc);

    String startSubject();
}
