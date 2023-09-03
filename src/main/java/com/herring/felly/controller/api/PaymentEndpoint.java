package com.herring.felly.controller.api;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.document.PaymentDocument;
import com.herring.felly.enums.PaymentStatus;
import com.herring.felly.payload.response.ErrorResponse;
import com.herring.felly.service.ClientService;
import com.herring.felly.service.PaymentService;
import com.herring.felly.service.TariffService;
import org.bson.types.ObjectId;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/payments", produces = "application/json")
public class PaymentEndpoint {

    private final PaymentService paymentService;
    private final ClientService clientService;
    private final TariffService tariffService;

    public PaymentEndpoint(PaymentService paymentService, ClientService clientService, TariffService tariffService) {
        this.paymentService = paymentService;
        this.clientService = clientService;
        this.tariffService = tariffService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(@PathVariable ObjectId id) {
        PaymentDocument payment = paymentService.getPaymentById(id);

        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Payment not found."));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getPayments(@RequestParam(required = false) String client,
                                         @RequestParam(required = false) PaymentStatus status) {
        List<PaymentDocument> payments;

        if (client != null) {
            ClientDocument clientDocument = clientService.getClient(client);
            if (clientDocument != null) {
                payments = paymentService.getClientPayments(clientDocument.getName());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Client not found."));
            }
        } else {
            payments = paymentService.getAllPayments();
        }

        if (status != null) {
            payments = payments.stream().filter(payment -> payment.getStatus() == status).collect(Collectors.toList());
        }

        List<PaymentDocument> finalPayments = payments;
        return ResponseEntity.ok(new HashMap<String, List<PaymentDocument>>() {{
            put("payments", finalPayments);
        }});
    }

    @PostMapping("")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDocument payment) {

        if (clientService.getClient(payment.getClient()) != null) {
            if (tariffService.getTariffById(payment.getTariffId()) != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(payment));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Tariff not found."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Client not found."));
        }
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmPayment(@PathVariable ObjectId id) {
        PaymentDocument payment = paymentService.getPaymentById(id);
        if (payment != null) {
            if (payment.getStatus() == PaymentStatus.PAID) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(6, "Payment already confirmed."));
            } else if (payment.getStatus() == PaymentStatus.CANCELLED) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(7, "Payment cancelled."));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(paymentService.confirmPayment(payment));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Payment not found."));
        }
    }
}
