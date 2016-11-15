package com.jking31cs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Represents a move that a pokemon can use.
 */
public class Move {
    private final String name;
    private final TypeEnum type;
    private final boolean special;
    private final int power, accuracy, pp;

    @JsonCreator
    public Move(
            @JsonProperty("name") String name,
            @JsonProperty("type") TypeEnum type,
            @JsonProperty("special") boolean special,
            @JsonProperty("power") int power,
            @JsonProperty("accuracy") int accuracy,
            @JsonProperty("pp") int pp
    ) {
        this.name = name;
        this.type = type;
        this.special = special;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
    }

    public String getName() {
        return name;
    }

    public TypeEnum getType() {
        return type;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getPp() {
        return pp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move) o;
        return special == move.special &&
                power == move.power &&
                accuracy == move.accuracy &&
                pp == move.pp &&
                Objects.equal(name, move.name) &&
                type == move.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, type, special, power, accuracy, pp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("type", type)
                .add("special", special)
                .add("power", power)
                .add("accuracy", accuracy)
                .add("pp", pp)
                .toString();
    }
}
