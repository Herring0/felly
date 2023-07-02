package com.herring.felly.controller.api;

import com.herring.felly.model.ClientModel;
import com.herring.felly.payload.response.ClientsList;
import com.herring.felly.payload.response.ErrorResponse;
import com.herring.felly.payload.response.MessageResponse;
import com.herring.felly.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/clients")
public class ClientEndpoint {

    @Autowired
    private ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@PathVariable String id) {
        ClientModel client = clientService.getClient(id);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(1, "Client not found"));
        }

    }

    @GetMapping("")
    public ResponseEntity<?> getAllClients() {
        return ResponseEntity.ok(new ClientsList(clientService.getAllClients()));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveClients() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/blocklist")
    public ResponseEntity<?> getBlockedClients() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}/traffic")
    public ResponseEntity<?> getClientTraffic(@PathVariable String id, @RequestParam(required = false) int last_days, @RequestParam String start_date, @RequestParam String end_date) {
        return ResponseEntity.ok(null);
    }

    @PostMapping(value={"/",""})
    public ResponseEntity<?> createClient(@RequestBody String id) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<?> blockClient(@PathVariable String id) {
        int code = clientService.blockClient(id);

        if (code == 0) {
            return ResponseEntity.ok(new MessageResponse(id, code, "The client has been blocked"));
        } else if (code == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(code, "Client already blocked"));
        }

        clientService.unblockClient(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{id}/unblock")
    public ResponseEntity<?> unblockClient(@PathVariable String id) {
        int code = clientService.unblockClient(id);

        if (code == 0) {
            return ResponseEntity.ok(new MessageResponse(id, code, "The client has been unblocked"));
        } else if (code == 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(code, "The client is not blocked"));
        }

        clientService.unblockClient(id);
        return ResponseEntity.ok(null);
    }
}
