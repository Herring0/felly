package com.herring.felly.service;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.exceptions.FellyRecordAlreadyExistsException;
import com.herring.felly.repository.ClientRepository;
import com.herring.felly.security.jwt.AuthEntryPointJwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public ClientDocument createClient(String id) {
        processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "/etc/openvpn/easy-rsa/mc.sh", id);

        try {
            process = processBuilder.start();
            process.waitFor();

            if (clientRepository.findByName(id) == null) {
                return clientRepository.save(new ClientDocument(id));
            } else {
                throw new FellyRecordAlreadyExistsException("Client already exists");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * 0 - success
    * 1 - client already blocked
    * 3 - unknown state
    * */
    @Transactional
    public boolean blockClient(ClientDocument client) {
        processBuilder = new ProcessBuilder();


        if (!client.isBlocked()) {
            try {
                processBuilder.command("bash", "/etc/openvpn/scripts/block_user.sh", client.getName());
                process = processBuilder.start();
                process.waitFor();
                client.setBlocked(true);
                clientRepository.save(client);
                getCode(process);
                return true;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }

    }

    /**
     * 0 - success
     * 1 - the client is not blocked
     * 3 - unknown state
     * */
    public boolean unblockClient(ClientDocument client) {
        processBuilder = new ProcessBuilder();

        if (client.isBlocked()) {
            try {
                processBuilder.command("bash", "/etc/openvpn/scripts/unblock_user.sh", client.getName());
                process = processBuilder.start();
                process.waitFor();
                client.setBlocked(false);
                clientRepository.save(client);
                getCode(process);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
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

}
