package com.iyzico.challenge.configuration;

import com.iyzico.challenge.entity.Payment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class QueueConfig {

    @Bean
    public BlockingQueue<Payment> paymentQueue() {
        return new PriorityBlockingQueue<>();
    }

    @Bean
    public ExecutorService threadPoolTaskExecutor() {
        return new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
}
