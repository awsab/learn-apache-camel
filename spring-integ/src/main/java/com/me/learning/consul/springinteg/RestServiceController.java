/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 06/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package com.me.learning.consul.springinteg;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/rest-integration")
public class RestServiceController {

    private final RestServiceCaller restServiceCaller;

    public RestServiceController (RestServiceCaller restServiceCaller) {
        this.restServiceCaller = restServiceCaller;
    }

    @GetMapping ("/call")
    public ResponseEntity<String> callRestService () {
        restServiceCaller.callRestService();
        return ResponseEntity.ok("REST service called successfully");
    }
}
