package com.herring.felly.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class ClientModel {
    private String id;
    private boolean isBlocked;
    private LocalDateTime createdAt;
    private LocalDateTime lastActive;

    public ClientModel(String id) {
        this.id = id;
        this.isBlocked = false;
        this.createdAt = LocalDateTime.of(LocalDate.now(), LocalTime.now()).minusDays(6);
        this.lastActive = LocalDateTime.of(LocalDate.now(), LocalTime.now());
    }
}
