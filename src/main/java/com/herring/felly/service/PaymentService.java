package com.herring.felly.service;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.document.PaymentDocument;
import com.herring.felly.enums.PaymentStatus;
import com.herring.felly.repository.PaymentRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ClientService clientService;
    private final TariffService tariffService;

    public PaymentService(PaymentRepository paymentRepository, ClientService clientService, TariffService tariffService) {
        this.paymentRepository = paymentRepository;
        this.clientService = clientService;
        this.tariffService = tariffService;
    }

    public PaymentDocument getPaymentById(ObjectId id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public List<PaymentDocument> getClientPayments(String client) {
        return paymentRepository.findAllByClient(client);
    }

    public PaymentDocument getClientLastPayment(String client) {
        return paymentRepository.findFirstByClient(client).orElse(null);
    }

    @Transactional
    public PaymentDocument createPayment(PaymentDocument payment) {
        paymentRepository.save(payment);
        return payment;
    }

    @Transactional
    public PaymentDocument confirmPayment(PaymentDocument paymentDocument) {

        ClientDocument client = clientService.getClient(paymentDocument.getClient());

        if (client != null) {
            LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.now());

            if (now.isAfter(client.getExpireAt())) {
                client.setExpireAt(now.plusDays(tariffService.getTariffById(paymentDocument.getTariffId()).getDuration_in_days()));
            } else {
                client.setExpireAt(client.getExpireAt().plusDays(tariffService.getTariffById(paymentDocument.getTariffId()).getDuration_in_days()));
            }

            client.setPaid(true);
            clientService.saveClient(client);
            paymentDocument.setStatus(PaymentStatus.PAID);
        }
        return paymentRepository.save(paymentDocument);
    }

    public List<PaymentDocument> getAllPayments() {
        return paymentRepository.findAll();
    }

}
