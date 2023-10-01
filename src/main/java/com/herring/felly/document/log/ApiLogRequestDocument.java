package com.herring.felly.document.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Document("api_logs_requests")
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ApiLogRequestDocument {
    private String request_uuid;
    private String ip;
    private String method;
    private Map<String, Object> payload;
    private String url;
    private Date request_timestamp;
    private String user_agent;
    private String token;
}
