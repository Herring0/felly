package com.herring.felly.controller.api;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.document.PaymentDocument;
import com.herring.felly.payload.request.PaymentRequest;
import com.herring.felly.payload.response.ErrorResponse;
import com.herring.felly.service.ClientService;
import com.herring.felly.service.PaymentService;
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

    public PaymentEndpoint(PaymentService paymentService, ClientService clientService) {
        this.paymentService = paymentService;
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(@PathVariable ObjectId id) {
        PaymentDocument payment = paymentService.getPaymentById(id);

        if(payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Payment not found"));
        }
    }

    @PostMapping(value = {"/create", ""})
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        if (clientService.getClient(request.getClient()) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.addPayment(request.getClient(), request.getDays()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Client not found"));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getPayments(@RequestParam(required = false) String client) {
        List<PaymentDocument> payments = paymentService.getAllPayments();
        ClientDocument clientDocument = clientService.getClient(client);

        if (clientDocument != null) {
            payments = payments.stream().filter(payment -> payment.getClient() == clientDocument.getName()).collect(Collectors.toList());
        }

        List<PaymentDocument> finalPayments = payments;
        return ResponseEntity.ok(new HashMap<String, List<PaymentDocument>>() {{
            put("payments", finalPayments);
        }});
    }
}
