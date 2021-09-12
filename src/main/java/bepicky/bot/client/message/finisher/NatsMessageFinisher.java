package bepicky.bot.client.message.finisher;

public interface NatsMessageFinisher {
    void finish(byte[] msg);

    String subject();
}
