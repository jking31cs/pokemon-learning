package com.jking31cs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jessica on 11/17/2016.
 */
public class PokemonPerformanceAnalysis {

    //TODO: figure out exact syntax for getting fields from ObjectMapper

    public static void main(String[] args) throws IOException {
        new PokemonPerformanceAnalysis().getAllConfidence();
    }

    public Map<String, Double> getAllConfidence() throws IOException {
        Map<String, Double> confidences = new HashMap<>();
        for (String pokemon : PokemonListingCache.getAll().keySet()) {
            Double confidence = new PokemonWinAnalysis(pokemon).getConfidence();
            System.out.println(pokemon + ", " + confidence);
        }
        return confidences;
    }

}
