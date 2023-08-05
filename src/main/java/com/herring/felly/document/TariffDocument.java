package com.herring.felly.document;

import com.herring.felly.enums.TariffType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Document("tariffs")
@Getter
@Setter
public class TariffDocument {

    @Id
    private ObjectId id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("type")
    private TariffType type;

    @Field("duration_in_days")
    private int durationInDays;

    @Field("prices")
    private Map prices;
}
