package com.example.demo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

//MongoDB Reactive Repository

@Repository
public interface PlayerRepository extends ReactiveMongoRepository<Player, String> {

    Mono<Player> findByCode(String code);
    Flux<Player> findByAge(int code);


}
