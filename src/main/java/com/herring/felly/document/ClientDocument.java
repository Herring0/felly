package com.herring.felly.document;

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
    private ObjectId id;

    @Field("name")
    private String name;

    @Field("isActive")
    private boolean isActive;

    @Field("isBlocked")
    private boolean isBlocked;


    public ClientDocument(String name, boolean isBlocked) {
        this.name = name;
        this.isActive = false;
        this.isBlocked = isBlocked;
    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(getId().getDate().toInstant(),
                ZoneId.systemDefault());
    }
}
