package bepicky.bot.client.message.async;

public interface NatsMessageFinisher {
    void finish(byte[] msg);

    String finishSubject();
}
