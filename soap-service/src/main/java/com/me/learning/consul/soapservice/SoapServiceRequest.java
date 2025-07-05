package com.me.learning.consul.soapservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoapServiceRequest {
    private List<String> serviceNames;
    private String correlationId;
    private String userId;
}
