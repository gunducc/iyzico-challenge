package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;

@Service
@Transactional
public class IyzicoPaymentService {

    private Logger logger = LoggerFactory.getLogger(IyzicoPaymentService.class);

    private BankService bankService;
    private PaymentRepository paymentRepository;

   private SimpleQueueService simpleQueueService;

    @Autowired
    private BlockingQueue<Payment> paymentQueue;

    public IyzicoPaymentService(BankService bankService, PaymentRepository paymentRepository, SimpleQueueService simpleQueueService) {
        this.bankService = bankService;
        this.paymentRepository = paymentRepository;
        this.simpleQueueService = simpleQueueService;
    }

    @Transactional(propagation = Propagation.NEVER)
    public boolean pay(BigDecimal price) {
        try {
            //pay with bank
            BankPaymentRequest request = new BankPaymentRequest();
            request.setPrice(price);
            BankPaymentResponse response = bankService.pay(request);

            //insert records
            Payment payment = new Payment();
            payment.setBankResponse(response.getResultCode());
            payment.setPrice(price);
            //paymentRepository.save(payment);
            paymentQueue.add(payment);
            logger.info("Payment queued successfully!");
            simpleQueueService.processQueue();
            return true;
        } catch (Exception e){
            logger.info("Payment failed!");
            return false;
        }
    }
}
