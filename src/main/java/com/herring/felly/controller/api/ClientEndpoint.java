package com.herring.felly.controller.api;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.payload.response.ClientsList;
import com.herring.felly.payload.response.ErrorResponse;
import com.herring.felly.payload.response.MessageResponse;
import com.herring.felly.security.jwt.AuthEntryPointJwt;
import com.herring.felly.service.ClientService;
import com.herring.felly.service.TrafficService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/clients", produces = "application/json")
public class ClientEndpoint {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TrafficService trafficService;

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@PathVariable String id) {
        ClientDocument client = clientService.getClient(id);
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
        ClientsList clients = new ClientsList(clientService.getActiveClients());
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/blocklist")
    public ResponseEntity<?> getBlockedClients() {
        ClientsList clients = new ClientsList(clientService.getClientsBlocklist());
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}/traffic")
    public ResponseEntity<?> getClientTraffic(@PathVariable String id,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date) {

        logger.info("start_date: {}", start_date);
        logger.info("end_date: {}", end_date);

        if (start_date == null && end_date == null) {
            return ResponseEntity.ok(trafficService.getClientTraffic(id));
        }  else if (start_date != null && end_date == null) {
            return ResponseEntity.ok(trafficService.getClientTrafficStartGreaterThan(id, start_date));
        } else if (start_date == null && end_date != null) {
            return ResponseEntity.ok(trafficService.getClientTrafficStartLessThan(id, end_date));
        } else {
            return ResponseEntity.ok(trafficService.getClientTraffic(id, start_date, end_date));
        }
    }

    @PostMapping(value={"/",""})
    public ResponseEntity<?> createClient(@RequestBody Map<String, String> request) {
        int code = clientService.createClient(request.get("id"));
        if (code == 0) {
            ClientDocument client = clientService.getClient(request.get("id"));
            return ResponseEntity.ok(client);
        } else if (code == 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(code, "Client already exists"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(code, "ERROR"));
        }

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

    @GetMapping("/{id}/file")
    public ResponseEntity<?> getFile(@PathVariable String id) {
        FileSystemResource resource = new FileSystemResource("/etc/openvpn/clients/" + id + "/" + id + ".ovpn");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ContentDisposition disposition = ContentDisposition
                .inline()
                .filename(resource.getFilename())
                .build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
