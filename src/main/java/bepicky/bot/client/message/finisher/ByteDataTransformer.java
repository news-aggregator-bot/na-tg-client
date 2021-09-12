package bepicky.bot.client.message.finisher;

import bepicky.common.data.DataTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ByteDataTransformer implements DataTransformer<byte[]> {

    @Autowired
    private ObjectMapper om;

    @Override
    public <O> O transform(byte[] data, Class<O> type) {
        try {
            return om.readValue(data, type);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
