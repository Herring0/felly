package com.herring.felly.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrafficResponse {

    private String user;

    private long bytesReceived;

    private long bytesSent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
