package com.jking31cs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Jessica on 11/17/2016.
 */
public class PokemonPerformanceAnalysis {

    //TODO: figure out exact syntax for getting fields from ObjectMapper

    public static void main(String[] args) throws IOException {
        new PokemonPerformanceAnalysis().getAllConfidence();
    }

    public void getAllConfidence() throws IOException {
        final Map<String, Double> confidences = new HashMap<>();

        final BlockingDeque<String> pokemon = new LinkedBlockingDeque<>(PokemonListingCache.getAll().keySet());

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (!pokemon.isEmpty()) {
                    String pokemonBeingAnalyzed = pokemon.poll();
                    Double confidence;
                    try {
                        confidence = new PokemonWinAnalysis(pokemonBeingAnalyzed).getConfidence();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(pokemonBeingAnalyzed + ", " + confidence);
                }
            }
        };

        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }

}
