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

        List<PokemonWithTypes> pokemonWithTypes = new ArrayList<>(PokemonListingCache.getAll().values());
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

                    if (score > bestScore
                        || score == bestScore && betterStats(testTeam, bestTeam)) {
                        bestScore = score;
                        bestTeam = new Team();
                        bestTeam.p1 = testTeam.p1;
                        bestTeam.p2 = testTeam.p2;
                        bestTeam.p3 = testTeam.p3;
                    }
                }
            }
        }

        System.out.println("Best Team: " + bestTeam);
        System.out.println("Best Score: " + bestScore);
    }

    private static boolean betterStats(Team testTeam, Team bestTeam) {
        //TODO figure this out.
        return true;
    }

    private static int calculateScore(Team testTeam) {
        int score = 0;
        Set<TypeEnum> doubleWeaknesses = new HashSet<>();
        Set<TypeEnum> weaknesses = new HashSet<>();
        Set<TypeEnum> resistances = new HashSet<>();
        Set<TypeEnum> doubleResistances = new HashSet<>();
        Set<TypeEnum> nullifications = new HashSet<>();


        //The goal is to minimize repeated weaknesses and maximize resistances.
        Set<TypeEnum> p1t1Weaknesses =  testTeam.p1.getType1().getWeaknesses();
        if (testTeam.p1.getType2() != null) {
            for (TypeEnum p1t2Weakness : testTeam.p1.getType2().getWeaknesses()) {
                if (p1t1Weaknesses.contains(p1t2Weakness)) {
                    doubleWeaknesses.add(p1t2Weakness);
                }
            }
        }
        weaknesses.addAll(p1t1Weaknesses);
        if (testTeam.p1.getType2() != null) {
            weaknesses.addAll(testTeam.p1.getType2().getWeaknesses());
        }
        Set<TypeEnum> p2t1Weaknesses =  testTeam.p2.getType1().getWeaknesses();
        if (testTeam.p2.getType2() != null) {
            for (TypeEnum p2t2Weakness : testTeam.p2.getType2().getWeaknesses()) {
                if (p2t1Weaknesses.contains(p2t2Weakness)) {
                    if (doubleWeaknesses.contains(weaknesses)) {
                        score -= 10;
                    }
                    doubleWeaknesses.add(p2t2Weakness);
                }
            }
        }
        weaknesses.addAll(p2t1Weaknesses);
        if (testTeam.p2.getType2() != null) {
            weaknesses.addAll(testTeam.p2.getType2().getWeaknesses());
        }
        Set<TypeEnum> p3t1Weaknesses =  testTeam.p3.getType1().getWeaknesses();
        if (testTeam.p3.getType2() != null) {
            for (TypeEnum p3t2Weakness : testTeam.p3.getType2().getWeaknesses()) {
                if (p3t1Weaknesses.contains(p3t2Weakness)) {
                    if (doubleWeaknesses.contains(weaknesses)) {
                        score -= 10;
                    }
                    doubleWeaknesses.add(p3t2Weakness);
                }
            }
        }
        score -= 12 - weaknesses.size();  //We want a well distributed weakness graph.
        score -= 3 * doubleWeaknesses.size();
        weaknesses.addAll(p3t1Weaknesses);
        if (testTeam.p3.getType2() != null) {
            weaknesses.addAll(testTeam.p3.getType2().getWeaknesses());
        }
        Set<TypeEnum> p1t1Resistances =  testTeam.p1.getType1().getResistances();
        if (testTeam.p1.getType2() != null) {
            for (TypeEnum p1t2Resistance : testTeam.p1.getType2().getResistances()) {
                if (p1t1Resistances.contains(p1t2Resistance)) {
                    doubleResistances.add(p1t2Resistance);
                }
            }
        }
        resistances.addAll(p1t1Resistances);
        nullifications.addAll(testTeam.p1.getType1().getNullifications());
        if (testTeam.p1.getType2() != null) {
            resistances.addAll(testTeam.p1.getType2().getResistances());
            nullifications.addAll(testTeam.p1.getType2().getNullifications());
        }

        Set<TypeEnum> p2t1Resistances =  testTeam.p1.getType1().getResistances();
        if (testTeam.p1.getType2() != null) {
            for (TypeEnum p1t2Resistance : testTeam.p1.getType2().getResistances()) {
                if (p1t1Resistances.contains(p1t2Resistance)) {
                    doubleResistances.add(p1t2Resistance);
                }
            }
        }
        resistances.addAll(p2t1Resistances);
        nullifications.addAll(testTeam.p2.getType1().getNullifications());
        if (testTeam.p2.getType2() != null) {
            resistances.addAll(testTeam.p2.getType2().getResistances());
            nullifications.addAll(testTeam.p2.getType2().getNullifications());
        }

        Set<TypeEnum> p3t1Resistances =  testTeam.p1.getType1().getResistances();
        if (testTeam.p1.getType2() != null) {
            for (TypeEnum p1t2Resistance : testTeam.p1.getType2().getResistances()) {
                if (p1t1Resistances.contains(p1t2Resistance)) {
                    doubleResistances.add(p1t2Resistance);
                }
            }
        }

        resistances.addAll(p3t1Resistances);
        nullifications.addAll(testTeam.p3.getType1().getNullifications());
        if (testTeam.p3.getType2() != null) {
            resistances.addAll(testTeam.p3.getType2().getResistances());
            nullifications.addAll(testTeam.p3.getType2().getNullifications());
        }

        score += resistances.size();
        score += 2 * resistances.size();
        score += 2 * nullifications.size();

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
