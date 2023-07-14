package com.herring.felly.payload.response;

import com.herring.felly.model.ClientModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClientsList {

    private List<ClientModel> clients;

}
