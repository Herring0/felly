package com.herring.felly.service;

import com.herring.felly.document.TrafficDocument;
import com.herring.felly.payload.response.TrafficResponse;
import com.herring.felly.repository.TrafficRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TrafficService {

    @Autowired
    private TrafficRepository trafficRepository;

    public TrafficResponse getClientTraffic(String id) {
        List<TrafficDocument> documents = trafficRepository.findAllByName(id);
        TrafficResponse trafficResponse = getTrafficFromList(documents);
        trafficResponse.setStartDate(documents.isEmpty() ? LocalDateTime.of(1970, 1, 1, 0, 0, 0) :
                documents.get(0).getCreatedAt());
        trafficResponse.setEndDate(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        trafficResponse.setUser(id);
        return trafficResponse;
    }

    public TrafficResponse getClientTrafficStartGreaterThan(String id, LocalDateTime start) {
        List<TrafficDocument> documents = trafficRepository.findAllByNameAndDateGreaterThan(id, start);
        TrafficResponse trafficResponse = getTrafficFromList(documents);
        trafficResponse.setStartDate(start);
        trafficResponse.setEndDate(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        trafficResponse.setUser(id);
        return trafficResponse;
    }

    public TrafficResponse getClientTrafficStartLessThan(String id, LocalDateTime end) {
        List<TrafficDocument> documents = trafficRepository.findAllByNameAndDateLessThan(id, end);
        TrafficResponse trafficResponse = getTrafficFromList(documents);
        trafficResponse.setStartDate(documents.isEmpty() ? LocalDateTime.of(1970, 1, 1, 0, 0, 0) :
                documents.get(0).getCreatedAt());
        trafficResponse.setEndDate(end);
        trafficResponse.setUser(id);
        return trafficResponse;
    }

    public TrafficResponse getClientTraffic(String id, LocalDateTime start, LocalDateTime end) {
        List<TrafficDocument> documents = trafficRepository.findAllByNameAndPeriod(id, start, end);
        TrafficResponse trafficResponse = getTrafficFromList(documents);
        trafficResponse.setStartDate(start);
        trafficResponse.setEndDate(end);
        trafficResponse.setUser(id);
        return trafficResponse;
    }

    private TrafficResponse getTrafficFromList(List<TrafficDocument> trafficDocuments) {
        TrafficResponse trafficResponse = new TrafficResponse();
        trafficResponse.setBytesSent(0);
        trafficResponse.setBytesReceived(0);

        long previousSent = 0;
        long previousReceived = 0;

        for (TrafficDocument document : trafficDocuments) {
            if (previousSent < document.getBytesSent()) {
//                if (documents.indexOf(document) == documents.size() - 1) {
//                    trafficResponse.setBytesSent(trafficResponse.getBytesSent() + document.getBytesSent());
//                    trafficResponse.setBytesReceived(trafficResponse.getBytesReceived() + document.getBytesReceived());
//                }
                trafficResponse.setBytesSent(trafficResponse.getBytesSent() + (document.getBytesSent() - previousSent));
                trafficResponse.setBytesReceived(trafficResponse.getBytesReceived() + (document.getBytesReceived() - previousReceived));
            } else {
                trafficResponse.setBytesSent(trafficResponse.getBytesSent() + document.getBytesSent());
                trafficResponse.setBytesReceived(trafficResponse.getBytesReceived() + document.getBytesReceived());
            }
            previousSent = document.getBytesSent();
            previousReceived = document.getBytesReceived();
        }
        return trafficResponse;
    }
}
