package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jking31cs.state.Team;

/**
 * This class creates a pokemonTeamIndex so we can quickly determine which pokemon are on which teams for analytical
 * purposes, writes it to the "output/indexes" folder.
 */
public class TeamReader {

    private static Map<String, Team> teamsMap;
    private static Map<String, Set<String>> pokemonIndex;

    public static void main(String[] args) throws IOException {
        Map<String, Set<String>> x = pokemonTeamIndex();
        System.out.println(x);
    }

    public static Map<String, Team> readTeamsFromFile() throws IOException {
        if (teamsMap != null) return teamsMap;
        File teamFile = new File("output/teams.json");
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        JavaType teamType = om.getTypeFactory().constructMapType(HashMap.class, String.class, Team.class);
        teamsMap = om.readValue(teamFile, teamType);
        return teamsMap;
    }

    public static Map<String, Set<String>> pokemonTeamIndex() throws IOException {
        if (pokemonIndex != null) return pokemonIndex;
        File teamFile = new File("output/indexes/pk-team-index.json");
        if (!teamFile.exists()) {
            createPokemonIndex();
            return pokemonTeamIndex();
        }
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        JavaType indexType = om.getTypeFactory().constructMapType(HashMap.class, String.class, Set.class);
        pokemonIndex = om.readValue(teamFile, indexType);
        return pokemonIndex;
    }

    public static void createPokemonIndex() throws IOException {
        Map<String, Team>  teams = readTeamsFromFile();

        Map<String, List<String>> index = new HashMap<>();
        for (String pokemon : PokemonListingCache.getAll().keySet()) {
            index.put(pokemon, new ArrayList<>());
        }

        for (Team t : teams.values()) {
            index.get(t.getP1()).add(t.getId());
            index.get(t.getP2()).add(t.getId());
            index.get(t.getP3()).add(t.getId());
        }

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        File indexDir = new File("output/indexes");
        if (!indexDir.exists()) indexDir.mkdir();
        om.writeValue(new File(indexDir, "pk-team-index.json"), index);

    }
}
