package com.me.learning.consul.springinteg.gateway;

import com.me.learning.consul.springinteg.entity.PersonRequest;
import com.me.learning.consul.springinteg.entity.PersonResponse;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(
        name = "personGateway1",
        defaultRequestChannel = "personRequestChannel",
        defaultRequestTimeout = "5000",
        defaultReplyTimeout = "5000")
public interface IPersonGateway extends BaseServiceGateway<PersonResponse, Long, PersonRequest> {

    PersonResponse getPersonById(Long personId);

    @Gateway(replyTimeout = 5000)
    PersonResponse createPerson(PersonRequest personRequest);
}
