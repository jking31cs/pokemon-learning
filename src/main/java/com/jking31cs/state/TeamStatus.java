package com.jking31cs.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Represents a list of pokemon statuses for each pokemon on the team.
 */
public class TeamStatus {
    private final String id;
    private final PokemonStatus p1;
    private final PokemonStatus p2;
    private final PokemonStatus p3;

    @JsonCreator
    public TeamStatus(
            @JsonProperty("id") String id,
            @JsonProperty("p1") PokemonStatus p1,
            @JsonProperty("p2") PokemonStatus p2,
            @JsonProperty("p3") PokemonStatus p3
    ) {
        this.id = id;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public String getId() {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeamStatus status = (TeamStatus) o;

        if (id != null ? !id.equals(status.id) : status.id != null) {
            return false;
        }
        if (p1 != null ? !p1.equals(status.p1) : status.p1 != null) {
            return false;
        }
        if (p2 != null ? !p2.equals(status.p2) : status.p2 != null) {
            return false;
        }
        return p3 != null ? p3.equals(status.p3) : status.p3 == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (p1 != null ? p1.hashCode() : 0);
        result = 31 * result + (p2 != null ? p2.hashCode() : 0);
        result = 31 * result + (p3 != null ? p3.hashCode() : 0);
        return result;
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
