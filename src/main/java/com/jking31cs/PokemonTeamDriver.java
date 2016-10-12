package com.jking31cs;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jking31cs on 10/11/16.
 */
public class PokemonTeamDriver {

    public static void main(String[] args) throws IOException {
        int bestScore = Integer.MIN_VALUE;

        List<PokemonWithTypes> pokemonWithTypes = PokemonListingCache.getAll();
        Team bestTeam = null;
        for (int i = 0; i < pokemonWithTypes.size() - 2; i++) {
            Team testTeam = new Team();
            testTeam.p1 = pokemonWithTypes.get(i);
            for (int j = i+1; j < pokemonWithTypes.size() - 1; j++) {
                if (j == i) {
                    continue;
                }
                testTeam.p2 = pokemonWithTypes.get(j);
                for (int k = j+1; k < pokemonWithTypes.size(); k++) {
                    if (j == k || i == k) {
                        continue;
                    }
                    testTeam.p3 = pokemonWithTypes.get(k);

                    int score = calculateScore(testTeam);

                    if (score > bestScore) {
                        bestScore = score;
                        bestTeam = testTeam;
                    }
                }
            }
        }

        System.out.println("Best Team: " + bestTeam);
        System.out.println("Best Score: " + bestScore);
    }

    private static int calculateScore(Team testTeam) {
        int score = 0;
        Set<TypeEnum> doubleWeaknesses = new HashSet<>();
        Set<TypeEnum> weaknesses = new HashSet<>();
        Set<TypeEnum> resistances = new HashSet<>();
        Set<TypeEnum> doubleResistances = new HashSet<>();
        Set<TypeEnum> nullifications = new HashSet<>();

        List<Type> typesP1 = Lists.newArrayList(testTeam.p1.getTypes());
        List<Type> typesP2 = Lists.newArrayList(testTeam.p2.getTypes());
        List<Type> typesP3 = Lists.newArrayList(testTeam.p3.getTypes());

        Type p1t1 = typesP1.get(0);
        Type p1t2 = typesP1.size() == 2 ? typesP1.get(1) : null;
        weaknesses.addAll(p1t1.getWeaknesses());
        resistances.addAll(p1t1.getResistances());
        nullifications.addAll(p1t1.getNullifications());
        if (p1t2 != null) {
            for (TypeEnum weakness : p1t1.getWeaknesses()) {
                if (p1t2.getWeaknesses().contains(weakness)) {
                    //If for some god-forsaken reason we have 2 pokemon with the same double weakness, minus 100 points
                    if (doubleWeaknesses.contains(weakness)) {
                        score -= 100;
                    }
                    doubleWeaknesses.add(weakness);


                }
            }
            for (TypeEnum weakness : p1t1.getResistances()) {
                if (p1t2.getResistances().contains(weakness)) {
                    doubleResistances.add(weakness);
                }
            }
            weaknesses.addAll(p1t2.getWeaknesses());
            resistances.addAll(p1t2.getResistances());
            nullifications.addAll(p1t2.getNullifications());
        }

        Type p2t1 = typesP2.get(0);
        Type p2t2 = typesP2.size() == 2 ? typesP2.get(1) : null;
        for (TypeEnum weakness : p2t1.getWeaknesses()) {
            if (weaknesses.contains(weakness)) {
                score -= 100; //Don't want repeating weaknesses
            }
        }
        weaknesses.addAll(p2t1.getWeaknesses());
        resistances.addAll(p2t1.getResistances());
        nullifications.addAll(p2t1.getNullifications());
        if (p2t2 != null) {
            for (TypeEnum weakness : p2t1.getWeaknesses()) {
                if (p2t2.getWeaknesses().contains(weakness)) {
                    //If for some god-forsaken reason we have 2 pokemon with the same double weakness, minus 100 points
                    if (doubleWeaknesses.contains(weakness)) {
                        score -= 100;
                    }
                    doubleWeaknesses.add(weakness);
                }
            }
            for (TypeEnum weakness : p2t1.getResistances()) {
                if (p2t2.getResistances().contains(weakness)) {
                    doubleResistances.add(weakness);
                }
            }
            for (TypeEnum weakness : p2t2.getWeaknesses()) {
                if (weaknesses.contains(weakness)) {
                    score -= 100; //Don't want repeating weaknesses
                }
            }
            weaknesses.addAll(p2t2.getWeaknesses());
            resistances.addAll(p2t2.getResistances());
            nullifications.addAll(p2t2.getNullifications());
        }

        Type p3t1 = typesP3.get(0);
        Type p3t2 = typesP3.size() == 2 ? typesP3.get(1) : null;
        for (TypeEnum weakness : p3t1.getWeaknesses()) {
            if (weaknesses.contains(weakness)) {
                score -= 100; //Don't want repeating weaknesses
            }
        }
        weaknesses.addAll(p3t1.getWeaknesses());
        resistances.addAll(p3t1.getResistances());
        nullifications.addAll(p3t1.getNullifications());
        if (p3t2 != null) {
            for (TypeEnum weakness : p3t1.getWeaknesses()) {
                if (p3t2.getWeaknesses().contains(weakness)) {
                    //If for some god-forsaken reason we have 2 pokemon with the same double weakness, minus 100 points
                    if (doubleWeaknesses.contains(weakness)) {
                        score -= 100;
                    }
                    doubleWeaknesses.add(weakness);
                }
            }
            for (TypeEnum weakness : p3t1.getResistances()) {
                if (p3t2.getResistances().contains(weakness)) {
                    doubleResistances.add(weakness);
                }
            }
            weaknesses.addAll(p3t2.getWeaknesses());
            resistances.addAll(p3t2.getResistances());
            nullifications.addAll(p3t2.getNullifications());
        }

        //Now to score.
        score -= 5 * doubleWeaknesses.size();
        score -= weaknesses.size();
        score += resistances.size();
        score += 2 * doubleResistances.size();
        score += 3 * nullifications.size();

        return score;
    }

    /**
     * Scoring system:
     * -5 points for each double weakness (really want to avoid that)
     * -1 point per weakness (bad)
     * +1 point per resistance (good, will cancel out weakness)
     * +2 points per double resistance (very good)
     * +3 points per nullification (best possible)
     */
    private static int incrementScore(PokemonWithTypes p1) {
        Set<TypeEnum> doubleWeakness = new HashSet<>();
        Set<TypeEnum> doubleResistance = new HashSet<>();
        ArrayList<Type> types = Lists.newArrayList(p1.getTypes());
        Type t1 = types.get(0);
        int score = 0;
        if (types.size() == 1) {
            //update score assuming only one type.
            score += t1.getResistances().size();
            score += 3 * t1.getNullifications().size();
            score -= t1.getWeaknesses().size();
        } else {
            Type t2 = types.get(1);
            for (TypeEnum weakness : t1.getWeaknesses()) {
                if (t2.getWeaknesses().contains(weakness)) {
                    doubleWeakness.add(weakness);
                }
            }
            for (TypeEnum weakness : t1.getResistances()) {
                if (t2.getResistances().contains(weakness)) {
                    doubleResistance.add(weakness);
                }
            }
            score -= 5 * doubleWeakness.size();
            score -= (t1.getWeaknesses().size() + t2.getWeaknesses().size() - (2 * doubleWeakness.size()));
            score += t1.getResistances().size() + t2.getResistances().size() - (2 * doubleResistance.size());
            score += 2 * doubleResistance.size();
            score += 3 * Sets.union(t1.getNullifications(), t2.getNullifications()).size();
        }

        return score;

    }

    private static class Team {
        private PokemonWithTypes p1;
        private PokemonWithTypes p2;
        private PokemonWithTypes p3;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Team team = (Team) o;
            return Objects.equal(p1, team.p1) &&
                Objects.equal(p2, team.p2) &&
                Objects.equal(p3, team.p3);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(p1, p2, p3);
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                .add("p1", p1)
                .add("p2", p2)
                .add("p3", p3)
                .toString();
        }
    }
}
