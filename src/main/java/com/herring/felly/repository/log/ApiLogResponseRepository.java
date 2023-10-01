package com.herring.felly.repository.log;

import com.herring.felly.document.log.ApiLogResponseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogResponseRepository extends MongoRepository<ApiLogResponseDocument, String> {
}
