package com.herring.felly.service;

import com.herring.felly.model.ClientModel;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private ProcessBuilder processBuilder;
    private Process process;

    private final int UNKNOWN_CODE = 3;

    public ClientModel getClient(String id) {
        processBuilder = new ProcessBuilder();
        processBuilder.command("ls", "/etc/openvpn/clients");

        try {
            process = processBuilder.start();
            process.waitFor();
            List<ClientModel> clients = getClientsList(process);
            return clients.stream()
                    .filter(client -> id.equals(client.getId()))
                    .findAny().orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ClientModel> getAllClients() {
        processBuilder = new ProcessBuilder();
        processBuilder.command("ls", "/etc/openvpn/clients");

        try {
            process = processBuilder.start();
            process.waitFor();
            return getClientsList(process);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ClientModel> getActiveClients() {
        processBuilder = new ProcessBuilder();
        processBuilder.command("cat", "/var/log/openvpn/openvpn-status.log");

        try {
            process = processBuilder.start();
            process.waitFor();
            return parseStatusLog(process);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ClientModel> getClientsBlocklist() {
        processBuilder = new ProcessBuilder();
        processBuilder.command("ls", "/etc/openvpn/ccd");

        try {
            process = processBuilder.start();
            process.waitFor();
            return getClientsList(process);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    public ClientModel createClient(String id) {
        processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "/etc/openvpn/easy-rsa/mc.sh", id);

        try {
            process = processBuilder.start();
            process.waitFor();
            return new ClientModel(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * 0 - success
    * 1 - client already blocked
    * 3 - unknown state
    * */
    public int blockClient(String id) {
        processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "/etc/openvpn/scripts/block_user.sh", id);

        try {
            process = processBuilder.start();
            process.waitFor();
            return getCode(process);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 0 - success
     * 1 - the client is not blocked
     * 3 - unknown state
     * */
    public int unblockClient(String id) {
        processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "/etc/openvpn/scripts/unblock_user.sh", id);

        try {
            process = processBuilder.start();
            process.waitFor();
            return getCode(process);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int getCode(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String code;

        if ((code = reader.readLine()) != null) {
            return Integer.parseInt(code);
        } else return UNKNOWN_CODE;
    }

    private List<ClientModel> getClientsList(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<ClientModel> clients = new ArrayList<>();
        String clientId;

        while ((clientId = reader.readLine()) != null) {
            clients.add(new ClientModel(clientId));
        }
        return clients;
    }

    private List<ClientModel> parseStatusLog(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<ClientModel> clients = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        String clientId;

        while ((clientId = reader.readLine()) != null) {
            lines.add(clientId);
        }

        for (int i = 3; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.equals("ROUTING TABLE")) {
                break;
            } else {
                clients.add(new ClientModel(line.split(",")[0]));
            }
        }
        return clients;
    }


}
