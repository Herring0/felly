package com.herring.felly.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.herring.felly.payload.enums.Currency;
import com.herring.felly.payload.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("payments")
@Getter
@Setter
public class PaymentDocument {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    @Field("client")
    private String client;

    @Field("PSP_id")
    @JsonProperty("PSP_id")
    private String PSP_id;

    @Field("currency")
    private Currency currency;

    @Field("amount")
    private double amount;

    @Field("tariff_id")
    @JsonProperty("tariff_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String tariffId;

    @Field("status")
    @JsonIgnore
    private PaymentStatus status = PaymentStatus.CREATED;

    @JsonProperty("status")
    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentDocument(String client, String PSP_id, Currency currency, double amount, String tariffId) {
        this.client = client;
        this.PSP_id = PSP_id;
        this.currency = currency;
        this.amount = amount;
        this.tariffId = tariffId;
    }

    public PaymentDocument() {
    }

    @Override
    public String toString() {
        return "PaymentDocument{" +
                "id='" + id + '\'' +
                ", client='" + client + '\'' +
                ", PSP_id='" + PSP_id + '\'' +
                ", currency=" + currency +
                ", amount=" + amount +
                ", tariffId='" + tariffId + '\'' +
                ", status=" + status +
                '}';
    }

    //    public PaymentDocument(String client) {
//        this.client = client;
//        this.status = PaymentStatus.CREATED;
//    }

//    public PaymentDocument(ObjectId id, String client, String PSP_id, Currency currency, double amount, String tariffId) {
//        this.id = id;
//        this.client = client;
//        this.PSP_id = PSP_id;
//        this.currency = currency;
//        this.amount = amount;
//        this.tariffId = tariffId;
//        this.status = PaymentStatus.CREATED;
//    }
}
