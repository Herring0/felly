package com.herring.felly.repository;

import com.herring.felly.document.PaymentDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentDocument, ObjectId> {


    @Query("{client: '?0'}")
    List<PaymentDocument> findAllByClient(String client);

    Optional<PaymentDocument> findById(ObjectId id);

    Optional<PaymentDocument> findFirstByClient(String client);

//    Optional<PaymentDocument> findTopByOrderByExpireAtDesc(String client);

    long count();

}
