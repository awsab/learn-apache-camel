package com.me.learning.consul.springinteg.gateway;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

public interface BaseServiceGateway<T, ID, R> {

    T getById(@Payload ID id);

    T getByIdWithTimeout(@Payload ID id, @Header("timeout") long timeout);

    T getByIdWithHeaders(@Payload ID id, @Header("serviceName") String serviceName);

    T create(@Payload R request);

    T update(@Payload ID id, @Payload R request);

    void delete(@Payload ID id);

    List<T> getAll();

    List<T> createBatch(@Payload List<R> requests);
}
