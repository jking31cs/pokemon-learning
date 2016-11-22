package com.jking31cs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by May on 11/21/2016.
 */
public class PokemonAnalysis {
    public static void main(String[] args) throws IOException {
        for (String pokemon1 : PokemonListingCache.getAll().keySet()) {
            for (String pokemon2 : PokemonListingCache.getAll().keySet()) {
                if (pokemon1.equals(pokemon2)) continue;
                Map<String, Double> moveStatistics = new PokemonSituationAnalysis(pokemon1, pokemon2).getMoveConfidence();

                System.out.println("home: " + pokemon1 + " Guest: " + pokemon2 + " Best move: " + moveStatistics.keySet() + " Confidence: " + moveStatistics.values());

            }
        }
    }

}
