package com.herring.felly.payload.response;

import com.herring.felly.model.ClientModel;

import java.util.ArrayList;
import java.util.List;

public class ClientsList {

    private List<ClientModel> clients = new ArrayList<>();

    public ClientsList(List<ClientModel> clients) {
        this.clients = clients;
    }

    public List<ClientModel> getClients() {
        return clients;
    }

    public void setClients(List<ClientModel> clients) {
        this.clients = clients;
    }
}
