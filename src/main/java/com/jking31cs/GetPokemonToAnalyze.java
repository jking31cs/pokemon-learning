package com.jking31cs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reads in the "PokemonToAnalyze" file and outputs a set of pokemon names.
 */
public class GetPokemonToAnalyze {

    public static Set<String> get() throws IOException {
        File pokemonToAnalyze = new File("data/PokemonToAnalyze");
        BufferedReader reader = new BufferedReader(new FileReader(pokemonToAnalyze));
        Set<String> pokemon = new HashSet<>();
        String line;
        while ((line = reader.readLine()) != null) {
            pokemon.add(line);
        }
        return pokemon;
    }
}
