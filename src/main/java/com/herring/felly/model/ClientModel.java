package com.herring.felly.model;

import java.sql.Timestamp;

public class ClientModel {
    private String id;
    private boolean isBlocked;
    private Timestamp createdAt;
    private Timestamp lastActive;

    public ClientModel(String id) {
        this.id = id;
        this.isBlocked = false;
        this.createdAt = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d",
                2023, 07, 01, 17, 03, 13));
        this.lastActive = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d",
                2023, 07, 02, 23, 37, 22));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastActive() {
        return lastActive;
    }

    public void setLastActive(Timestamp lastActive) {
        this.lastActive = lastActive;
    }
}
