package com.herring.felly.document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Field("expireAt")
    private LocalDateTime expireAt;

//    @Field("products")
//    private List<Product> products;

    public ClientDocument(String name) {
        this.name = name;
        this.isActive = false;
        this.isBlocked = false;
        this.isPaid = false;
        this.expireAt = LocalDateTime.of(LocalDate.now(), LocalTime.now());
    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(getId().getDate().toInstant(),
                ZoneId.systemDefault());
    }
}

//@Getter
//@Setter
//@AllArgsConstructor
//class Product {
//    private ProductType type;
//    private LocalDateTime expiredAt;
//}
