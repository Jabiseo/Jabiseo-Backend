package com.jabiseo.notification.consumer;


import com.jabiseo.domain.notification.domain.NotificationSend;
import com.jabiseo.domain.notification.domain.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class NotificationConsumer {

    private final NotificationSender sender;

    @KafkaListener(topics = "${notification.topic}", groupId = "${notification.group-id}", containerFactory = "notificationContainerFactory")
    public void listen(ConsumerRecord<String, NotificationSend> record, Acknowledgment ask) throws Exception {
//        sender.send(record.value());
        ask.acknowledge();
    }
}
