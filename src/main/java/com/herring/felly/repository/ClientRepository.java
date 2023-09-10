package com.herring.felly.repository;

import com.herring.felly.document.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<ClientDocument, String> {

    @Query("{name: '?0'}")
    List<ClientDocument> findAllByName(String name);

    @Query("{name: '?0'}")
    ClientDocument findByName(String name);

    Optional<ClientDocument> findById(String id);

    List<ClientDocument> findByIsActive(boolean isActive);
    List<ClientDocument> findByIsBlocked(boolean isBlocked);

    List<ClientDocument> findByIsPaid(boolean isPaid);

    List<ClientDocument> findByIsActiveAndIsBlocked(boolean isBlocked, boolean isActive);

    List<ClientDocument> findByNameContainingIgnoreCase(String keyword);

    long count();

}
