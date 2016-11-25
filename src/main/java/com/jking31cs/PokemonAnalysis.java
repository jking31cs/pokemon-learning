package com.jking31cs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by May on 11/21/2016.
 */
public class PokemonAnalysis {

    private static class JsonOutput {
        public String attacker;
        public String defender;
        public String move;
        public Double confidence;
    }

    public static void main(String[] args) throws IOException {
        File outputFile = new File("output/pokemon-next-moves.json");
        if (outputFile.exists()) outputFile.delete();
        outputFile.createNewFile();

        final BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write('[');
        writer.flush();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                try {
                    writer.write(']');
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    //ignore since I'm screwed if this fails.
                }
            }
        });
        boolean isFirst = true;
        try {
            for (String pokemon1 : PokemonListingCache.getAll().keySet()) {
                for (String pokemon2 : PokemonListingCache.getAll().keySet()) {
                    Map<String, Double> moveStatistics = new PokemonSituationAnalysis(pokemon1, pokemon2).getMoveConfidence();
                    if (pokemon1.equals(pokemon2)) continue;
                    if (!isFirst) {
                        writer.write(',');
                    }
                    isFirst = false;
                    JsonOutput output = new JsonOutput();
                    output.attacker = pokemon1;
                    output.defender = pokemon2;
                    output.move = moveStatistics.keySet().stream().findFirst().orElse("");
                    output.confidence = moveStatistics.values().stream().findFirst().orElse(0d);
                    writer.write(new ObjectMapper().writeValueAsString(output));
                    writer.newLine();
                    writer.flush();
                }
            }
        } finally {
            writer.write(']');
            writer.flush();
            writer.close();
        }
    }

}
