/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 02/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package org.me.practise.personaggregator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class SoapAccount {
    private List<String> serviceNames;
    private String correlationId;
    private String userId;
}
