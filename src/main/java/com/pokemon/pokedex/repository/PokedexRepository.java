package com.pokemon.pokedex.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.pokemon.pokedex.model.Pokemon;

public interface PokedexRepository extends ReactiveMongoRepository<Pokemon, String> {

}
