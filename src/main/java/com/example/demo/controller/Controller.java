package com.example.demo.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class Controller {

  private static final String ENDPOINT = "http://<your-local-ip>:9090";

   
  private final ExchangeFunction exchangeFunction;
  
  public Controller() {
    super();
    this.exchangeFunction = ExchangeFunctions.create(new ReactorClientHttpConnector());
  }

  Mono<ServerResponse> handle(ServerRequest request) {

    ClientRequest request1 =
        ClientRequest.create(HttpMethod.GET, URI.create(ENDPOINT + "/hello")).build();

    ClientRequest request2 =
        ClientRequest.create(HttpMethod.GET, URI.create(ENDPOINT + "/world")).build();

    Mono<String> helloMono =
        exchangeFunction.exchange(request1).flatMap(r -> r.bodyToMono(String.class));

    Mono<String> worldMono =
        exchangeFunction.exchange(request2).flatMap(r -> r.bodyToMono(String.class));

    return helloMono.zipWith(worldMono, (h, w) -> h + w).flatMap(ServerResponse.ok()::bodyValue);
  }
}
