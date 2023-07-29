package com.herring.felly.controller.api.search;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/search/clients", produces = "application/json")
public class ClientSearchEndpoint {

    @GetMapping("")
    public ResponseEntity<?> searchClient(@RequestParam String query) {
        return ResponseEntity.ok(query);
    }
}
