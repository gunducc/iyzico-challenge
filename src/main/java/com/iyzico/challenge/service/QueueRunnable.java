package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
public class QueueRunnable implements Runnable {

    @Autowired
    BlockingQueue<Payment> paymentQueue;

    @Autowired
    PaymentRepository paymentRepository;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public void run() {

        try {
            while (true) {
                Thread.sleep(15000);
                Payment payment = paymentQueue.take();
                paymentRepository.save(payment);
                LOG.info("queue executed for: " + Thread.currentThread().getName() + " ,payment: " + payment.getPrice());
            }
        } catch (InterruptedException e) {
            LOG.error("Exception in thread: " + e);
            Thread.currentThread().interrupt();
        }
    }



}