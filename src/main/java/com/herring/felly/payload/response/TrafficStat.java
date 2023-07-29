package com.herring.felly.payload.response;

import com.herring.felly.document.TrafficDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TrafficStat {
    private long timestamp;
    private long value;
//    private List<TrafficResponse> traffic;
}
