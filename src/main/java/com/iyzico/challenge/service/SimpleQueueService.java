package com.iyzico.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class SimpleQueueService {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleQueueService.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private ApplicationContext applicationContext;

    public void processQueue() {
        LOG.info("processing payment queue");
        QueueRunnable runnable = applicationContext.getBean(QueueRunnable.class);
        executorService.submit(runnable);
    }
}