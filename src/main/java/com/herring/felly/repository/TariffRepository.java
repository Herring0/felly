package com.herring.felly.repository;

import com.herring.felly.document.TariffDocument;
import com.herring.felly.payload.enums.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TariffRepository extends MongoRepository<TariffDocument, String> {

    @Query("{type: '?0'}")
    List<TariffDocument> findAllByType(ProductType type);

    Optional<TariffDocument> findById(String id);

    Optional<TariffDocument> findFirstByName(String name);

    long count();

}
