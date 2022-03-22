package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @GetMapping
    public Flux<Player> getFilteredPlayers() {
        playerService.getRankingPlayer();
        return playerService.getFilterPlayer()
                .buffer(100)
                .flatMap(player ->Flux.fromStream(player.parallelStream()));

    }

    @GetMapping("/lists")
    public Flux<List<Player>> getListasRankingPlayers() {
        return playerService.getRankingPlayer();
    }


}
