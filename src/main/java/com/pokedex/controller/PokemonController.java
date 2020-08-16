package com.pokedex.controller;

import com.pokedex.model.PokemonEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.pokedex.model.Pokemon;
import com.pokedex.repository.PokedexReporitory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/pokemons")
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
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Pokemon>> updatePokemon(@PathVariable String id, @RequestBody Pokemon pokemon) {
		return pokedexReporitory.findById(id)
				.flatMap(existingPokemon -> {
					existingPokemon.setNome(pokemon.getNome());
					existingPokemon.setCategoria(pokemon.getCategoria());
					existingPokemon.setHabilidade(pokemon.getHabilidade());
					existingPokemon.setPeso(pokemon.getPeso());
					return pokedexReporitory.save(existingPokemon);
				})
				.map(updatePokemon -> ResponseEntity.ok(updatePokemon))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deletePokemon(@PathVariable String id) {
		return pokedexReporitory.findById(id)
				.flatMap(existingPokemon -> {
					return pokedexReporitory.delete(existingPokemon).then(Mono.just(ResponseEntity.ok().<Void>build()));
				})
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping
	public Mono<Void> deleteAllPokemons() {
		return pokedexReporitory.deleteAll();
	}

	@GetMapping(value = "/event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<PokemonEvent> getPokemonEvent() {
		return Flux.interval(Duration.ofSeconds(5))
				.map(val -> new PokemonEvent(val, "Evento de Pokemon"));
	}

}
