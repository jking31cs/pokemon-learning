package com.jking31cs.state;

import com.google.common.base.Objects;

/**
 * Represents a change in status for a pokemon, for now only keeps track of the hp.
 */
public class PokemonStatus {
    private final long id;
    private final String name;
    private final int currentHP;

    public PokemonStatus(long id, String name, int currentHP) {
        this.id = id;
        this.name = name;
        this.currentHP = currentHP;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonStatus that = (PokemonStatus) o;
        return id == that.id &&
            currentHP == that.currentHP &&
            Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, currentHP);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .add("name", name)
            .add("currentHP", currentHP)
            .toString();
    }

}
