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
import java.time.ZoneId;

@Document("clients")
@Getter
@Setter
public class ClientDocument {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("name")
    private String name;

    @Field("isActive")
    private boolean isActive;

    @Field("isBlocked")
    private boolean isBlocked;

    @Field("isPaid")
    private boolean isPaid;

    public ClientDocument(String name) {
        this.name = name;
        this.isActive = false;
        this.isBlocked = false;
        this.isPaid = true;
    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(getId().getDate().toInstant(),
                ZoneId.systemDefault());
    }
}
