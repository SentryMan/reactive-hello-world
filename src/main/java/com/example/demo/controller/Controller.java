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

@Component
public class Controller {

  private static final String ENDPOINT = "http://<your-local-ip>:9090";
  private static final AsyncRestTemplate template = new AsyncRestTemplate();
  
  @Bean
  public RouterFunction<ServerResponse> router() {

    return RouterFunctions.route(GET("helloworld"), this::handle);
  }


  Mono<ServerResponse> handle(ServerRequest request) {

    Mono<String> helloMono =
        Mono.<ResponseEntity<String>>create(
                sink ->
                    template
                        .getForEntity(ENDPOINT + "/hello", String.class)
                        .addCallback(sink::success, sink::error))
            .map(ResponseEntity::getBody);

    Mono<String> worldMono =
        Mono.<ResponseEntity<String>>create(
                sink ->
                    template
                        .getForEntity(ENDPOINT + "/world", String.class)
                        .addCallback(sink::success, sink::error))
            .map(ResponseEntity::getBody);

    return helloMono.zipWith(worldMono, (h, w) -> h + w).flatMap(ServerResponse.ok()::bodyValue);
  }
}

