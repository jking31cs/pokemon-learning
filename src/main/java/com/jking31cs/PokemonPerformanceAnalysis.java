package com.jking31cs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jking31cs.state.BattleTree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jessica on 11/17/2016.
 */
public class PokemonPerformanceAnalysis {

    //TODO: figure out exact syntax for getting fields from ObjectMapper

    final Double CONFIDENCE = 0.9;

    Map<Pokemon, ArrayList<String>> teamsContainingMember;
    Map<String, Double> winsPerTeam;
    Map<String, Double> battlesPerTeam;
    Map<Pokemon, Double> pokemonWinRatios;
    //need to initialize these somewhere

    public Map<String, BattleTree> parseSingleTeamInput (File singleTeam) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        JavaType mapType = om.getTypeFactory().constructMapType(HashMap.class, String.class, BattleTree.class);
        Map<String, BattleTree> readInValues = om.readValue(singleTeam, mapType);
        System.out.println(readInValues);

        return readInValues;
    }

    public void sortTeamMembers (Map<String, BattleTree> allTeamBattles) {

        //get teams.json
        //TeamReader creates team objects

        //get three team members from Team 1 in allTeamBattles
        List<Pokemon> team = new ArrayList<Pokemon>();
        team.add(allTeamBattles.team1.p1);
        team.add(allTeamBattles.team1.p2);
        team.add(allTeamBattles.team1.p3);

        //for every Pokemon seen so far
        for (Map.Entry<Pokemon, ArrayList<String>> pokemon : teamsContainingMember.entrySet()) {
            for (Pokemon member : team) {
                //if Pokemon is on this team
                if (pokemon.getKey() == member) {
                    //Add this team to the list of teams containing the Pokemon
                    ArrayList<String> teams = teamsContainingMember.get(pokemon.getKey());
                    teams.add(allTeamBattles.team1.id);
                    teamsContainingMember.put(pokemon.getKey(), teams);
                    team.remove(member);
                }
            }
            //if every team member has been accounted for
            if (team.isEmpty()) {
                break;
            }
        }
        //if this is the first time some Pokemon are seen
        if (!team.isEmpty()) {
            for (Pokemon member : team) {
                //add new Pokemon to map, along with this team
                ArrayList<String> teams = new ArrayList<>();
                teams.add(allTeamBattles.team1.id);
                teamsContainingMember.put(member, teams);
            }
        }
    }

    public void calcTeamWinsAndBattles(Map<String, BattleTree> allTeamBattles) {
        Double wins;
        Double battles;
        String teamId = allTeamBattles.team1.id;
        for (each endstate == true in battle tree) {
            if(allTeamBattles.team1.p1.health > 0 || allTeamBattles.team1.p2.health > 0 || allTeamBattles.team1.p3.health > 0) {
               wins++;
            }
            battles++;
        }
        winsPerTeam.put(teamId, wins);
        battlesPerTeam.put(teamId, battles);
    }

    public void calculatePokemonWinRatio(Map.Entry<Pokemon, ArrayList<String>> pokemonWithTeams) {
        Double wins = 0.0;
        Double battles = 0.0;
        for(String teamId : pokemonWithTeams.getValue()) {
            wins =+ winsPerTeam.get(teamId);
            battles =+ battlesPerTeam.get(teamId);
        }
        pokemonWinRatios.put(pokemonWithTeams.getKey(), (wins/battles));
    }



    //main driver for this class
    public Map<Pokemon, Double> pokeRankCalculator () throws IOException {

        Map<Pokemon, Double> topPokemon = new HashMap<>();



        //calculate stats for each team

        //calculate stats for each Pokemon
        for(Map.Entry<Pokemon, ArrayList<String>> pokemonWithTeams : teamsContainingMember.entrySet()) {
            calculatePokemonWinRatio(pokemonWithTeams);
        }

        for(Map.Entry<Pokemon, Double> pokemonRatio : pokemonWinRatios.entrySet()) {
            if(pokemonRatio.getValue() >= CONFIDENCE) {
                topPokemon.put(pokemonRatio.getKey(), pokemonRatio.getValue());
            }
        }

        return topPokemon;
    }

}
