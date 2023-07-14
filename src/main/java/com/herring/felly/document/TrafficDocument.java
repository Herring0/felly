package com.herring.felly.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Document("felly")
@Getter
@Setter
@AllArgsConstructor
public class TrafficDocument {

    @Id
    private ObjectId id;

    @Field("User")
    private String user;

    @Field("Ipv4")
    private String ipv4;

    @Field("Bytes_Received")
    private long bytesReceived;

    @Field("Bytes_Sent")
    private long bytesSent;

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(getId().getDate().toInstant(),
                ZoneId.systemDefault());
    }
}
