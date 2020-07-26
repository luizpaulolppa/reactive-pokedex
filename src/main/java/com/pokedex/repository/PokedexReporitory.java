package com.pokedex.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.pokedex.model.Pokemon;

@Repository
public interface PokedexReporitory extends ReactiveMongoRepository<Pokemon, String> {

}
