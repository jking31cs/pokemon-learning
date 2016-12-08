package com.jking31cs;

import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Jessica on 11/17/2016.
 */
public class PokemonPerformanceAnalysis {

    //TODO: figure out exact syntax for getting fields from ObjectMapper

    public static void main(String[] args) throws IOException {

        new PokemonPerformanceAnalysis().getAllConfidence(GetPokemonToAnalyze.get(), Integer.parseInt(args[0]));
    }

    public void getAllConfidence(Set<String> pokemonSet, int size) throws IOException {
        final Map<String, Double> confidences = new HashMap<>();

        //I'm so lazy right now.
        Set<Set<String>> pokemonSets = new HashSet<>();
        for (String p1 : pokemonSet) {
            if (size == 1) {
                pokemonSets.add(Sets.newHashSet(p1));
            } else {
                for (String p2 : pokemonSet) {
                    if (p1.equals(p2)) continue;
                    if (size == 2) {
                        pokemonSets.add(Sets.newHashSet(p1, p2));
                    } else {
                        for (String p3 : pokemonSet) {
                            if (p1.equals(p3) || p2.equals(p3)) continue;
                            pokemonSets.add(Sets.newHashSet(p1,p2,p3));
                        }
                    }
                }
            }
        }

        final BlockingDeque<Set<String>> pokemon = new LinkedBlockingDeque<>(pokemonSets);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (!pokemon.isEmpty()) {
                    Set<String> pokemonBeingAnalyzed = pokemon.poll();
                    Double confidence;
                    try {
                        confidence = new PokemonWinAnalysis(pokemonBeingAnalyzed).getConfidence();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(setToString(pokemonBeingAnalyzed) + ", " + confidence);
                }
            }
        };

        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }

    private String setToString(Set<String> pokemonBeingAnalyzed) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        boolean isFirst = true;
        for (String s : pokemonBeingAnalyzed) {
            if (!isFirst) builder.append(" ");
            builder.append(s);
            isFirst = false;
        }
        builder.append("]");
        return builder.toString();
    }

}
