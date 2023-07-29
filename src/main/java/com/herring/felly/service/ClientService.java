package com.herring.felly.service;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.repository.ClientRepository;
import com.herring.felly.security.jwt.AuthEntryPointJwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private ProcessBuilder processBuilder;
    private Process process;

    private final ClientRepository clientRepository;

    private final int UNKNOWN_CODE = 3;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDocument getClient(String id) {
        return clientRepository.findByName(id);
    }

    public List<ClientDocument> getAllClients() {
        return clientRepository.findAll();
    }

    public List<ClientDocument> getClients(boolean isActive, boolean isBlocked) {
        return clientRepository.findByIsActiveAndIsBlocked(isActive, isBlocked);
    }

    public List<ClientDocument> getClientsByActive(boolean active) {
        return clientRepository.findByIsActive(active);
    }

    public List<ClientDocument> getClientsByBlock(boolean blocked) {
        return clientRepository.findByIsBlocked(true);
    }

    public int createClient(String id) {
        processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "/etc/openvpn/easy-rsa/mc.sh", id);

        try {
            process = processBuilder.start();
            process.waitFor();
//            int code = getCode(process);
            if (clientRepository.findByName(id) == null) {
                clientRepository.save(new ClientDocument(id));
            }
//            if (code == 0) {
//                clientRepository.save(new ClientDocument(id));
//            }
            return 0;
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

        try {
            if ((code = reader.readLine()) != null) {
                logger.info("Script execute result: {}", code);
                return Integer.parseInt(code);
            } else return UNKNOWN_CODE;
        } catch (NumberFormatException e) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("Script execute result: {}", line);
            }
            return -1;
        }

    }

    private List<ClientDocument> getClientsList(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<ClientDocument> clients = new ArrayList<>();
        String clientId;

        while ((clientId = reader.readLine()) != null) {
            clients.add(clientRepository.findByName(clientId));
        }
        return clients;
    }

    private List<ClientDocument> parseStatusLog(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        List<ClientModel> clients = new ArrayList<>();
        List<ClientDocument> clients = new ArrayList<>();
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
//                clients.add(new ClientModel(line.split(",")[0]));
                clients.add(clientRepository.findByName(line.split(",")[0]));
            }
        }
        return clients;
    }


}
