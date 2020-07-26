package com.pokedex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pokedex.model.Pokemon;
import com.pokedex.repository.PokedexReporitory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/podemons")
public class PokemonController {
	
	private PokedexReporitory pokedexReporitory;
	
	private PokemonController(PokedexReporitory pokedexReporitory) {
		this.pokedexReporitory = pokedexReporitory;
	}
	
	@GetMapping
	public Flux<Pokemon> getAllPokemons() {
		return pokedexReporitory.findAll();
	}
	
	@GetMapping("/{id}")
	private Mono<ResponseEntity<Pokemon>> getPokemon(@PathVariable String id) {
		return pokedexReporitory.findById(id)
				.map(pokemon -> ResponseEntity.ok(pokemon))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	private Mono<Pokemon> savePokemon(@RequestBody Pokemon pokemon) {
		return pokedexReporitory.save(pokemon);
	}

}
