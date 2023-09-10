package com.herring.felly.scheduler;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BlockScheduler {
    private static final Logger log = LoggerFactory.getLogger(BlockScheduler.class);

    private final ClientService clientService;

    public BlockScheduler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Scheduled(fixedRate = 10000)
    @Async
    public void blockExpiredClients() {
        List<ClientDocument> clients = clientService.getPaidClients();

        for (ClientDocument client : clients) {
            if (LocalDateTime.now().isAfter(client.getExpireAt())) {
                client.setPaid(false);
                clientService.blockClient(client);
                log.info("The client {} is blocked for non-payment.", client.getName());
            }
        }
    }
}
