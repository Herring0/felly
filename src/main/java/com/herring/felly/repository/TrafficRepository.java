package com.herring.felly.repository;

import com.herring.felly.document.TrafficDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrafficRepository extends MongoRepository<TrafficDocument, String> {

    @Query("{User: '?0'}")
    List<TrafficDocument> findAllByName(String name);

    Optional<TrafficDocument> findById(String id);

    @Query("{" +
            "'User': ?0, " +
            "'$expr': {" +
                "$and: [" +
                    "{'$gte': [" +
                        "{'$toDate': '$_id'}, " +
                        "{'$date': ?1}]}," +
                    "{'$lte': [" +
                        "{'$toDate': '$_id'}, " +
                        "{'$date': ?2}]}" +
                "]" +
            "}" +
            "}")
    List<TrafficDocument> findAllByNameAndPeriod(String name, LocalDateTime start, LocalDateTime end);

    @Query("{" +
            "'User': ?0, " +
            "'$expr': {" +
                "'$gte': [" +
                    "{'$toDate': '$_id'}, " +
                    "{'$date': ?1}]" +
            "}" +
            "}")
    List<TrafficDocument> findAllByNameAndDateGreaterThan(String name, LocalDateTime start);

    @Query("{" +
            "'User': ?0, " +
            "'$expr': {" +
                "'$lte': [" +
                    "{'$toDate': '$_id'}, " +
                    "{'$date': ?1}]" +
            "}" +
            "}")
    List<TrafficDocument> findAllByNameAndDateLessThan(String name, LocalDateTime end);

    TrafficDocument findFirstByOrderByUserDesc(String name);

    long count();

}
