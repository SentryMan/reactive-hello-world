package com.example.demo.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

  private static final String ENDPOINT = "http://<your-local-ip>:9090";

  private final RestTemplate template = new RestTemplate();

  @GetMapping("helloworld")
  String handle() {
    return template.getForObject(ENDPOINT + "/hello", String.class)
        + template.getForObject(ENDPOINT + "/world", String.class);
  }
}
