package com.herring.felly.service;

import com.herring.felly.document.PaymentDocument;
import com.herring.felly.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PaymentService {

    final private PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<PaymentDocument> getClientPayments(String client) {
        return paymentRepository.findAllByClient(client);
    }

    public PaymentDocument getClientLastPayment(String client) {
        return paymentRepository.findFirstByClient(client).orElse(null);
    }

    public PaymentDocument addPayment(String client, int days) {
        PaymentDocument lastPayment = paymentRepository.findTopByOrderByExpireAtDesc(client).orElse(null);
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        PaymentDocument payment;

        if (lastPayment != null) {
            if (now.isAfter(lastPayment.getExpireAt())) {
                payment = paymentRepository.save(new PaymentDocument(client, now.plusDays(days)));
            } else {
                payment = paymentRepository.save(new PaymentDocument(client, lastPayment.getExpireAt().plusDays(days)));
            }
        } else {
            payment = paymentRepository.save(new PaymentDocument(client, now.plusDays(days)));
        }

        return payment;
    }

    @Transactional
    public PaymentDocument createPayment(PaymentDocument payment) {
        paymentRepository.save(payment);

        return payment;
    }

    @Transactional
    public PaymentDocument confirmPayment(String paymentId) {
        PaymentDocument payment = paymentRepository.findById(paymentId).orElse(null);

        return payment;
    }

}
