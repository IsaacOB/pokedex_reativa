package com.pokemon.pokedex;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import com.pokemon.pokedex.model.Pokemon;
import com.pokemon.pokedex.repository.PokedexRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class PokedexApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokedexApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init (ReactiveMongoOperations operations,
							PokedexRepository pokedexRepository) {
		
		return args -> {
			Flux<Pokemon> pokedexFlux = Flux.just(
											 new Pokemon(null, "Pikachu", "Raio", "Thunder", 6.09),
											 new Pokemon(null, "Charizard", "Fogo", "Blase", 90.05))
										.flatMap(pokedexRepository::save);
			
			pokedexFlux.
					thenMany(pokedexRepository.findAll())
					.subscribe(System.out::println);
		};
	}

}
