package com.herring.felly.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmptyResponse {

    private int code;

    private String error;

}
