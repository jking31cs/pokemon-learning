package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Reads in the flat file containing moves in json format into memory.
 */
public class MoveListingCache {

    private final Map<String, List<Move>> pokemonMoveSetsMapping;
    private static MoveListingCache moveListingCache;
    public static Map<String, List<Move>> get() throws IOException {
        if (moveListingCache == null) {
            moveListingCache = new MoveListingCache();
        }
        return moveListingCache.pokemonMoveSetsMapping;
    }

    public static Map<String, List<Move>> getMoveSets() throws IOException {
        return new MoveListingCache().pokemonMoveSetsMapping;
    }

    private MoveListingCache() throws IOException {
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
            List<List<Integer>> moveset = results.movesets.get(pokedexId);
            for (List<Integer> moveIdInfo : moveset) {
                Integer moveId = moveIdInfo.get(0);
                if (results.moves.containsKey(moveId)) {
                    Map<String, Object> moveInfo = results.moves.get(moveId);
                    TypeEnum moveType = TypeEnum.valueOf(
                            results.types.get(moveInfo.get("typeid")).get("name").toString().toUpperCase());
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
                    List<Move> moves = pokemonMoveSetsMapping.get(pokemonName);
                    moves.add(move);
                    pokemonMoveSetsMapping.put(pokemonName, moves);
                }
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
