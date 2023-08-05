package com.herring.felly.controller.api;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.payload.request.PaymentRequest;
import com.herring.felly.payload.response.ErrorResponse;
import com.herring.felly.service.ClientService;
import com.herring.felly.service.PaymentService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = {"/pay", ""})
    public ResponseEntity<?> pay(@RequestBody PaymentRequest request) {
        if (clientService.getClient(request.getClient()) != null) {
            return ResponseEntity.ok(paymentService.addPayment(request.getClient(), request.getDays()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1, "Client not found"));
        }

    }

    @PostMapping(value = {"/create", ""})
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        if (clientService.getClient(request.getClient()) != null) {
            return ResponseEntity.ok(paymentService.addPayment(request.getClient(), request.getDays()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(1, "Client not found"));
        }

    }

    @GetMapping("")
    public ResponseEntity<?> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("")
    public ResponseEntity<?> getPaymentsByClient(@RequestParam(required = false) String client) {
        ClientDocument clientDocument = clientService.getClient(client);
        if (client != null) {
            return ResponseEntity.ok(paymentService.getClientPayments(clientDocument.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(1, "Client not found"));
        }

    }
}
