package com.herring.felly.document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("payments")
@Getter
@Setter
public class PaymentDocument {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("client")
    private String client;

    @Field("expireAt")
    private LocalDateTime expireAt;

    public PaymentDocument(String client, LocalDateTime expireAt) {
        this.client = client;
        this.expireAt = expireAt;
    }
}
