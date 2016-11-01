package com.jking31cs.state;

import com.google.common.base.Objects;

/**
 * Represents a list of pokemon statuses for each pokemon on the team.
 */
public class TeamStatus {
    private final long id;
    private final PokemonStatus p1;
    private final PokemonStatus p2;
    private final PokemonStatus p3;

    public TeamStatus(long id, PokemonStatus p1, PokemonStatus p2, PokemonStatus p3) {
        this.id = id;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public long getId() {
        return id;
    }

    public PokemonStatus getP1() {
        return p1;
    }

    public PokemonStatus getP2() {
        return p2;
    }

    public PokemonStatus getP3() {
        return p3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamStatus that = (TeamStatus) o;
        return id == that.id &&
            Objects.equal(p1, that.p1) &&
            Objects.equal(p2, that.p2) &&
            Objects.equal(p3, that.p3);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, p1, p2, p3);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .add("p1", p1)
            .add("p2", p2)
            .add("p3", p3)
            .toString();
    }
}
