package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Reads in the flat file into memory.
 */
public class MoveListingCache {

    final Map<String, List<Move>> pokemonMoveSetsMapping;

    MoveListingCache() throws IOException {
        this.pokemonMoveSetsMapping = new HashMap<>();
        populate();
    }

    private void populate() throws IOException {
        //First, read in the JSON file such that we can analyze it.
        File jsonFile = new File("data/moves.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonResults results = objectMapper.readValue(jsonFile, JsonResults.class);

        //Now we want to get out the movesets for each pokemon.
        for (int pokedexId=1; pokedexId <= 151; pokedexId++) {
            List<List<Integer>> moveIdInfo = results.movesets.get(pokedexId);
            Integer moveId = moveIdInfo.get(0).get(0);
            if (results.moves.containsKey(moveId)) {
                Map<String, Object> moveInfo = results.moves.get(moveIdInfo.get(0).get(0));
                TypeEnum moveType = TypeEnum.valueOf(results.types.get(moveInfo.get("typeid")).get("name").toString().toUpperCase());
                Move move = new Move(
                        (String) moveInfo.get("name"),
                        moveType,
                        moveInfo.get("category").equals("s"),
                        (Integer) moveInfo.get("power"),
                        (Integer) moveInfo.get("accuracy"),
                        (Integer) moveInfo.get("pp")
                );
                String pokemonName = (String) results.pokedex.get(pokedexId).get("name");
                pokemonMoveSetsMapping.putIfAbsent(pokemonName, new ArrayList<>());
                pokemonMoveSetsMapping.get(pokemonName).add(move);
            }
        }

    }

    private static class JsonResults {
        public Map<Integer, Map<String, Object>> moves;
        public Map<Integer, List<List<Integer>>> movesets;
        public Map<Integer, Map<String, Object>> pokedex;
        public Map<Integer, Map<String, Object>> types;
    }
}
