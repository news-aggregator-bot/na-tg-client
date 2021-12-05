package bepicky.bot.client.service;

import bepicky.bot.client.feign.ReaderServiceClient;
import bepicky.bot.client.message.nats.AdminMessagePublisher;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.dto.StatusReaderDto;
import bepicky.common.domain.request.ReaderRequest;
import bepicky.common.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ReaderService implements IReaderService {

    private final ReaderServiceClient readerClient;
    private final AdminMessagePublisher adminMessagePublisher;

    @Override
    public ReaderDto register(ReaderRequest readerRequest) {
        log.info("reader: registration:{}", readerRequest.toString());
        try {
            adminMessagePublisher.publish(
                "REGISTRATION: reader registration",
                readerRequest.toString()
            );
            return readerClient.register(readerRequest);
        } catch (Exception e) {
            log.error("reader: registration failed {}", e.getMessage());
            adminMessagePublisher.publish(
                "FAILED REGISTRATION: reader registration",
                readerRequest.toString(),
                e.getMessage()
            );
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ReaderDto find(Long chatId) {
        return checkReader(chatId, readerClient.find(chatId));
    }

    @Override
    public ReaderDto enable(Long chatId) {
        return checkReader(chatId, readerClient.enable(chatId));
    }

    @Override
    public ReaderDto disable(Long chatId) {
        return checkReader(chatId, readerClient.disable(chatId));
    }

    @Override
    public ReaderDto pause(Long chatId) {
        return checkReader(chatId, readerClient.pause(chatId));
    }

    @Override
    public ReaderDto settings(Long chatId) {
        return checkReader(chatId, readerClient.settings(chatId));
    }

    @Override
    public StatusReaderDto getStatus(Long chatId) {
        return readerClient.getStatus(chatId);
    }

    private ReaderDto checkReader(Long chatId, ReaderDto readerDto) {
        return Optional.ofNullable(readerDto)
            .orElseThrow(() -> new ResourceNotFoundException("404:reader:" + chatId));
    }
}
