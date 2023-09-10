package com.herring.felly.controller.api;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.exceptions.FellyRecordAlreadyExistsException;
import com.herring.felly.payload.response.*;
import com.herring.felly.security.jwt.AuthEntryPointJwt;
import com.herring.felly.service.ClientService;
import com.herring.felly.service.TrafficService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/clients", produces = "application/json")
public class ClientEndpoint {

    private final ClientService clientService;

    private final TrafficService trafficService;

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    public ClientEndpoint(ClientService clientService, TrafficService trafficService) {
        this.clientService = clientService;
        this.trafficService = trafficService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@PathVariable String id) {
        ClientDocument client = clientService.getClient(id);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Client not found."));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getClients(@RequestParam(required = false) Boolean active,
                                        @RequestParam(required = false) Boolean blocked,
                                        @RequestParam(required = false) Boolean paid,
                                        @RequestParam(required = false) String keyword) {

        List<ClientDocument> clients;

        if (keyword != null) {
            clients = clientService.searchClients(keyword);
        } else {
            clients = clientService.getAllClients();
        }

        if (active != null)
            clients = clients.stream().filter(client -> client.isActive() == active).collect(Collectors.toList());
        if (blocked != null)
            clients = clients.stream().filter(client -> client.isBlocked() == blocked).collect(Collectors.toList());
        if (paid != null)
            clients = clients.stream().filter(client -> client.isPaid() == paid).collect(Collectors.toList());

        List<ClientDocument> finalClients = clients;
        return ResponseEntity.ok(new HashMap<String, List<ClientDocument>>() {{
            put("clients", finalClients);
        }});
    }

    @GetMapping("/{id}/traffic")
    public ResponseEntity<?> getClientTraffic(@PathVariable String id,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date) {

        logger.info("start_date: {}", start_date);
        logger.info("end_date: {}", end_date);

        if (start_date == null && end_date == null) {
            return ResponseEntity.ok(trafficService.getClientTraffic(id));
        } else if (start_date != null && end_date == null) {
            return ResponseEntity.ok(trafficService.getClientTrafficStartGreaterThan(id, start_date));
        } else if (start_date == null && end_date != null) {
            return ResponseEntity.ok(trafficService.getClientTrafficStartLessThan(id, end_date));
        } else {
            return ResponseEntity.ok(trafficService.getClientTraffic(id, start_date, end_date));
        }
    }

    @GetMapping("/{id}/traffic/stat")
    public ResponseEntity<?> getClientTrafficStat(@PathVariable String id, @RequestParam(required = false) int hours) {
        List<TrafficStat> traffic = new ArrayList<>();
        trafficService.getClientTrafficStartLessThan(id, LocalDateTime.of(LocalDate.now(), LocalTime.now()).minusHours(hours));
        for (int i = 0; i < hours; i++) {
            TrafficResponse response = trafficService.getClientTraffic(
                    id,
                    LocalDateTime.of(LocalDate.now(), LocalTime.now()).minusHours(i + 1),
                    LocalDateTime.of(LocalDate.now(), LocalTime.now()).minusHours(i));
            traffic.add(new TrafficStat(Timestamp.valueOf(response.getEndDate()).getTime(), response.getBytesReceived()));
        }
        Collections.reverse(traffic);
        return ResponseEntity.ok(traffic);
    }

    @PostMapping(value = {"/", ""})
    public ResponseEntity<?> createClient(@RequestBody Map<String, String> request) {
        try {
            ClientDocument client = clientService.createClient(request.get("id"));
            return ResponseEntity.status(HttpStatus.CREATED).body(client);
        } catch (FellyRecordAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(1, "Client already exists."));
        }
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<?> blockClient(@PathVariable String id) {
        ClientDocument client = clientService.getClient(id);

        if (client != null) {
            boolean blocked = clientService.blockClient(client);
            if (blocked) {
                return ResponseEntity.ok(new MessageResponse(id, 0, "The client has been blocked."));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(1, "Client already blocked."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Client not found."));
        }
    }

    @PostMapping("/{id}/unblock")
    public ResponseEntity<?> unblockClient(@PathVariable String id) {
        ClientDocument client = clientService.getClient(id);

        if (client != null) {
            boolean unblocked = clientService.unblockClient(client);
            if (unblocked) {
                return ResponseEntity.ok(new MessageResponse(id, 0, "The client has been unblocked."));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(1, "The client is not blocked."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Client not found."));
        }
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<?> getFile(@PathVariable String id) {
        ClientDocument client = clientService.getClient(id);

        if (client != null) {
            FileSystemResource resource = new FileSystemResource("/etc/openvpn/clients/" + client.getName() + "/" + client.getName() + ".ovpn");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            ContentDisposition disposition = ContentDisposition
                    .inline()
                    .filename(resource.getFilename())
                    .build();
            headers.setContentDisposition(disposition);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Client not found."));
        }
    }

}
