package com.jking31cs;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Set;

/**
 * Created by jking31cs on 10/11/16.
 */
public class PokemonWithTypes {
    private final Pokemon pokemon;
    private final Type type1;
    private final Type type2;

    public PokemonWithTypes(
        Pokemon pokemon,
        Type type1,
        Type type2

    ) {
        this.pokemon = pokemon;
        this.type1 = type1;
        this.type2 = type2;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Type getType1() {
        return type1;
    }

    public Type getType2() {
        return type2;
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
        return MoreObjects.toStringHelper(this)
            .add("pokemon", pokemon)
            .add("type1", type1)
            .add("type2", type2)
            .toString();
    }

}
