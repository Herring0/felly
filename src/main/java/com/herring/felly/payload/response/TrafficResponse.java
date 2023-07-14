package com.herring.felly.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class TrafficResponse {

    @NonNull
    private String user;

    @NonNull
    private long bytesReceived;

    @NonNull
    private long bytesSent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
