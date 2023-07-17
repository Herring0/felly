package com.herring.felly.repository;

import com.herring.felly.document.ClientDocument;
import com.herring.felly.document.TrafficDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<ClientDocument, String> {

    @Query("{name: '?0'}")
    List<ClientDocument> findAllByName(String name);

    @Query("{name: '?0'}")
    ClientDocument findByName(String name);

    Optional<ClientDocument> findById(String id);


    long count();

}
