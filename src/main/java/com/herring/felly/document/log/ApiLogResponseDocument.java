package com.herring.felly.document.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document("api_logs_responses")
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ApiLogResponseDocument {
    private String request_uuid;
    private int response_status;
    private Map<String, Object> payload;
    private Date response_timestamp;
}
