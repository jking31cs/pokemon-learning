package com.jking31cs.state;

import com.google.common.base.Objects;

/**
 * Represents a change in status for a pokemon, for now only keeps track of the hp.
 */
public class PokemonStatus {
    private final String id;
    private final String name;
    private final int currentHP;

    public PokemonStatus(String id, String name, int currentHP) {
        this.id = id;
        this.name = name;
        this.currentHP = currentHP;
    }

    public String getId() {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PokemonStatus that = (PokemonStatus) o;

        if (currentHP != that.currentHP) {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + currentHP;
        return result;
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
