package com.pokemon.pokedex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pokemon.pokedex.model.Pokemon;
import com.pokemon.pokedex.repository.PokedexRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {

	private PokedexRepository repository;

	public PokemonController(PokedexRepository pokedexRepository) {
		this.repository = pokedexRepository;
	}

	@GetMapping
	public Flux<Pokemon> getAllPokemons() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Pokemon>> getPokemon(@PathVariable String id) {
		return repository.findById(id).map(pokemon -> ResponseEntity.ok(pokemon))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Pokemon> savePokemon(@RequestBody Pokemon pokemon) {
		return repository.save(pokemon);
	}

	@PutMapping("{id}")
	public Mono<ResponseEntity<Pokemon>> updatePokemon(@PathVariable(value = "id") String id,
													   @RequestBody Pokemon pokemon) {

		return repository.findById(id).flatMap(existingPokemon -> {
			existingPokemon.setNome(pokemon.getNome());
			existingPokemon.setCategoria(pokemon.getCategoria());
			existingPokemon.setHabilidade(pokemon.getHabilidade());
			existingPokemon.setPeso(pokemon.getPeso());
			return repository.save(existingPokemon);
		}).map(updatePokemon -> ResponseEntity.ok(updatePokemon)).defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("{id")
	public Mono<ResponseEntity<Void>> deletePokemon(@PathVariable (value = "id") String id){
		return repository.findById(id)
					     .flatMap(existingPokemon -> repository.delete(existingPokemon)
					    		 .then(Mono.just(
					    				 ResponseEntity.ok().<Void>build())))
					     .defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping
	public Mono<Void> deleteAllPokemons (){
		return repository.deleteAll();
	}

}
