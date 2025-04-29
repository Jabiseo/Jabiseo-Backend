package com.jabiseo.api.config.async;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfig {

    @Bean(name = "notificationTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("NotificationTask- ");
        executor.initialize();
        return executor;
    }

    @Bean(name = "notificationFCMExecutor")
    public TaskExecutor fcmExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(70);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("NotificationFCM- ");
        executor.initialize();
        return executor;
    }
}
