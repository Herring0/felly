package com.herring.felly.repository.log;

import com.herring.felly.document.log.ApiLogRequestDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRequestRepository extends MongoRepository<ApiLogRequestDocument, String> {
}
