package com.herring.felly.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.herring.felly.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Document("tariffs")
@Getter
@Setter
public class TariffDocument {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("type")
    private ProductType type;

    @Field("duration_in_days")
    private int duration_in_days;

    @Field("prices")
    private Prices prices;

    public TariffDocument(String name, String description, ProductType type, int duration_in_days, Prices prices) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.duration_in_days = duration_in_days;
        this.prices = prices;
    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(getId().getDate().toInstant(),
                ZoneId.systemDefault());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Prices {
        @Field("USD") @JsonProperty("USD") private double USD;
        @Field("EUR") @JsonProperty("EUR") private double EUR;
        @Field("RUB") @JsonProperty("RUB") private double RUB;
        @Field("TRY") @JsonProperty("TRY") private double TRY;
        @Field("BTC") @JsonProperty("BTC") private double BTC;
    }
}
