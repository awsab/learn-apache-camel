/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class RestServiceCaller {

    private final MessageChannel restRequestChannel;

    public RestServiceCaller (MessageChannel restRequestChannel) {
        this.restRequestChannel = restRequestChannel;
    }

    public void callRestService () {
        restRequestChannel.send (MessageBuilder.withPayload ("").build (), 10);
    }
}
