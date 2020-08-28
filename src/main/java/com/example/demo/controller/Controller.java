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

   
  private HttpClient client;
  
  public Controller() {
   super();
   this.client = HttpClient.create();
   }
  
  @Bean
  public RouterFunction<ServerResponse> router() {

    return RouterFunctions.route(GET("helloworld"), this::handle);
  }

  Mono<ServerResponse> handle(ServerRequest request) {

    Flux<String> helloMono =
        client.get().uri(ENDPOINT + "/hello").responseContent().asByteArray().map(String::new);

    Flux<String> worldMono =
        client.get().uri(ENDPOINT + "/world").responseContent().asByteArray().map(String::new);

    return ServerResponse.ok().body(helloMono.zipWith(worldMono, (h, w) -> h + w), String.class);
  }
}
