/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.personservicerequest.entiry;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonRequest implements Serializable {

    private Long personId;
}
