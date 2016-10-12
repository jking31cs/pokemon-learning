package com.jking31cs;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;

import static com.jking31cs.TypeEnum.*;

/**
 * Coded mapping of Type Chart from http://bulbapedia.bulbagarden.net/wiki/Type/Type_chart#Generation_I
 */
public class TypeMapping {

    private static final TypeMapping singletonInstance = new TypeMapping();

    private final Map<TypeEnum, Type> mapping;

    private TypeMapping() {
        this.mapping = new HashMap<TypeEnum, Type>(12);

        this.mapping.put(NORMAL, new Type(
            NORMAL,
            Sets.immutableEnumSet(FIGHTING),
            Sets.immutableEnumSet(GHOST),
            Sets.<TypeEnum>newHashSet()
        ));

        this.mapping.put(FIGHTING, new Type(
            FIGHTING,
            Sets.immutableEnumSet(FLYING, PSYCHIC),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(ROCK, BUG)
        ));

        this.mapping.put(FLYING, new Type(
            FLYING,
            Sets.immutableEnumSet(ROCK, ELECTRIC, ICE),
            Sets.immutableEnumSet(GROUND),
            Sets.immutableEnumSet(FIGHTING, BUG, GRASS)
        ));

        this.mapping.put(POISON, new Type(
            POISON,
            Sets.immutableEnumSet(GROUND, BUG, PSYCHIC),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(FIGHTING, POISON, GRASS)
        ));

        this.mapping.put(GROUND, new Type(
            GROUND,
            Sets.immutableEnumSet(WATER, GRASS, ICE),
            Sets.immutableEnumSet(ELECTRIC),
            Sets.immutableEnumSet(POISON, ROCK)
        ));

        this.mapping.put(ROCK, new Type(
            ROCK,
            Sets.immutableEnumSet(WATER, GRASS, FIGHTING, GROUND),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(NORMAL, FLYING, POISON, FIRE)
        ));

        this.mapping.put(BUG, new Type(
            BUG,
            Sets.immutableEnumSet(FLYING, POISON, ROCK, FIRE),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(FIGHTING, GROUND, GRASS)
        ));

        this.mapping.put(GHOST, new Type(
            GHOST,
            Sets.immutableEnumSet(GHOST),
            Sets.immutableEnumSet(NORMAL, FIGHTING),
            Sets.immutableEnumSet(POISON, BUG)
        ));

        this.mapping.put(FIRE, new Type(
            FIRE,
            Sets.immutableEnumSet(GROUND, ROCK, WATER),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(BUG, FIRE, GRASS)
        ));

        this.mapping.put(WATER, new Type(
            WATER,
            Sets.immutableEnumSet(GRASS, ELECTRIC),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(FIRE, WATER, ICE)
        ));

        this.mapping.put(GRASS, new Type(
            GRASS,
            Sets.immutableEnumSet(FLYING, POISON, BUG, FIRE, ICE),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(WATER, GRASS, ELECTRIC)
        ));

        this.mapping.put(ELECTRIC, new Type(
            ELECTRIC,
            Sets.immutableEnumSet(GROUND),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(FLYING, ELECTRIC)
        ));

        this.mapping.put(PSYCHIC, new Type(
            PSYCHIC,
            Sets.immutableEnumSet(BUG),
            Sets.immutableEnumSet(GHOST),
            Sets.immutableEnumSet(FIGHTING, PSYCHIC)
        ));

        this.mapping.put(ICE, new Type(
            ICE,
            Sets.immutableEnumSet(FIGHTING, ROCK, FIRE),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(ICE)
        ));

        this.mapping.put(DRAGON, new Type(
            DRAGON,
            Sets.immutableEnumSet(ICE, DRAGON),
            Sets.<TypeEnum>newHashSet(),
            Sets.immutableEnumSet(FIRE, WATER, ELECTRIC, GRASS)
        ));
    }

    public static Type get(TypeEnum typeEnum) {
        return singletonInstance.mapping.get(typeEnum);
    }
}
