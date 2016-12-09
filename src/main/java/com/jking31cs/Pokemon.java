package com.jking31cs;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Class that contains the meta data for a pokemon.
 */
public class Pokemon {
    private final String name;
    private final int hp, attack, defense, special, speed;

    public Pokemon(String name, int hp, int attack, int defense, int special, int speed) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.special = special;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpecial() {
        return special;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return hp == pokemon.hp &&
            attack == pokemon.attack &&
            defense == pokemon.defense &&
            special == pokemon.special &&
            speed == pokemon.speed &&
            Objects.equal(name, pokemon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, hp, attack, defense, special, speed);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("hp", hp)
            .add("attack", attack)
            .add("defense", defense)
            .add("special", special)
            .add("speed", speed)
            .toString();
    }
}
