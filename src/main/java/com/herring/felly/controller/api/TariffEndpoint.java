package com.herring.felly.controller.api;

import com.herring.felly.document.TariffDocument;
import com.herring.felly.payload.enums.ProductType;
import com.herring.felly.payload.response.ErrorResponse;
import com.herring.felly.service.TariffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/tariffs", produces = "application/json")
public class TariffEndpoint {

    private final TariffService tariffService;

    public TariffEndpoint(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTariff(@PathVariable String id) {
        TariffDocument tariff = tariffService.getTariffById(id);
        if (tariff != null) {
            return ResponseEntity.ok(tariffService.getTariffById(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(2, "Tariff not found"));
        }

    }

    @GetMapping("")
    public ResponseEntity<?> getTariffs(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) ProductType type) {

        List<TariffDocument> tariffs = tariffService.getAllTariffs();

        if (name != null)
            tariffs = tariffs.stream().filter(tariff -> tariff.getName() == name).collect(Collectors.toList());
        if (type != null)
            tariffs = tariffs.stream().filter(tariff -> tariff.getType() == type).collect(Collectors.toList());

        List<TariffDocument> finalTariffs = tariffs;
        return ResponseEntity.ok(new HashMap<String, List<TariffDocument>>() {{
            put("tariffs", finalTariffs);
        }});
    }

    @PostMapping("")
    public ResponseEntity<?> createTariff(@RequestBody TariffDocument tariff) {
        if (tariffService.getTariffByName(tariff.getName()) == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tariffService.createTariff(tariff));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(1, "Tariff already exists"));
        }
    }
}
