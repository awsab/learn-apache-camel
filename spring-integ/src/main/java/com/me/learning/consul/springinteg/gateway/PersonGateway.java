/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg.gateway;

import com.me.learning.consul.springinteg.entity.PersonResponse;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway (
        name = "personGateway",
        defaultRequestChannel = "personRequestChannel"
)
public interface PersonGateway {
    PersonResponse getPerson (Long personId);
}
