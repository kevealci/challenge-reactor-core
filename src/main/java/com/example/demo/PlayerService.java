package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository repo;

    private Flux<Player> getAll() {
        return repo.findAll()
                .buffer(100)
                .flatMap(players -> Flux.fromStream(players.parallelStream()))
                ;
    }

    private Flux<Player> getPlayersOver34() {

        return getAll()
                .buffer(100)
                .flatMap(player -> Flux.fromStream(player.parallelStream()))
                .filter(players -> {
                    try {
                        return players.getAge() > 34;
                    } catch (Exception e) {
                        return false;
                    }
                });
    }

    private Flux<Player> getByClub() {
        return getPlayersOver34()
                .buffer(100)
                .flatMap(player -> Flux.fromStream(player.parallelStream()))
                .filter(players -> {
                    try {
                        return players.getClub().equals("FC Barcelona");
                    } catch (Exception e) {
                        return false;
                    }
                })
                .onErrorContinue((e, i) ->
                        System.out.println("error on method GetClub" + i)
                );
    }

    public Flux<Player> getFilterPlayer() {
        return getByClub()
                .buffer(100)
                .flatMap(player -> Flux.fromStream(player.parallelStream()))

                .onErrorContinue((e, i) ->
                        System.out.println("error on method getFilterPlayer" + i));
    }


    public Flux<List<Player>> getRankingPlayer() {

        return getAll()
                .buffer(100)
                .flatMap(player -> Flux.fromStream(player.parallelStream()))
                .distinct()
                .groupBy(Player::getNational)
                .flatMap(Flux::collectList)
                .map(list -> {
                    Collections.sort(list);
                    return list;
                })
                .onErrorContinue((e, i) ->
                        System.out.println("error on method getRankingPlayer" + i));

    }

}
