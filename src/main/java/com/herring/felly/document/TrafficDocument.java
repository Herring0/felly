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

@Document("Traffic")
@Getter
@Setter
public class TrafficDocument {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("client")
    private String client;

    @Field("ipv4")
    private String ipv4;

    @Field("bytesReceived")
    private long bytesReceived;

    @Field("bytesSent")
    private long bytesSent;

    public TrafficDocument(String client, String ipv4, long bytesReceived, long bytesSent) {
        this.client = client;
        this.ipv4 = ipv4;
        this.bytesReceived = bytesReceived;
        this.bytesSent = bytesSent;
    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(getId().getDate().toInstant(),
                ZoneId.systemDefault());
    }
}
