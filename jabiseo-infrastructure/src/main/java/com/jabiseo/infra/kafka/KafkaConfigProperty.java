package com.jabiseo.infra.kafka;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@ConfigurationProperties("spring.kafka")
public class KafkaConfigProperty {

    private final List<String> bootstrapServers;

    public KafkaConfigProperty(List<String> bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

}
