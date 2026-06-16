package com.loganalyzer.platform.ingection.producer;

import com.loganalyzer.platform.ingection.entity.RawLogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RawLogProducer {

    private final KafkaTemplate<
            String,
            RawLogEvent
            > kafkaTemplate;

    private static final String TOPIC =
            "raw-logs";

    public void publish(
            RawLogEvent event
    ) {

        kafkaTemplate.send(
                TOPIC,
                event.sourceId().toString(),
                event
        );
    }
}