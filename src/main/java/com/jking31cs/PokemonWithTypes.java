package com.jking31cs;

import com.google.common.base.Objects;

import java.util.Set;

/**
 * Created by jking31cs on 10/11/16.
 */
public class PokemonWithTypes {
    private final Pokemon pokemon;
    private final Set<Type> types;

    public PokemonWithTypes(Pokemon pokemon, Set<Type> types) {
        this.pokemon = pokemon;
        this.types = types;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonWithTypes that = (PokemonWithTypes) o;
        return Objects.equal(pokemon, that.pokemon);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pokemon);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("pokemon", pokemon)
            .add("types", types)
            .toString();
    }
}
