package com.jking31cs;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.jking31cs.state.BattleTree;
import com.jking31cs.state.State;

/**
 * Created by jking31 on 11/20/16.
 */
public class PokemonWinAnalysis {

    private static Map<String, Boolean> battleT1WinCache = new HashMap<>();

    private double wins = 0;
    private double battles = 0;
    private final List<String> pokemon;

    public PokemonWinAnalysis(String singlePokemon) {
        this.pokemon = Collections.singletonList(singlePokemon);
    }

    public PokemonWinAnalysis(List<String> pokemon) {
        this.pokemon = pokemon;
    }

    public Double getConfidence() throws IOException {
        //TODO all your team gathering and stuff here.
        Set<String> teamIds = TeamReader.pokemonTeamIndex().get(pokemon.get(0));
        for (int i = 1; i < pokemon.size(); i++) {
            Set<String> teamIdsForPkmn = TeamReader.pokemonTeamIndex().get(pokemon.get(i));
            teamIds = Sets.intersection(teamIds, teamIdsForPkmn);
        }
        for (String teamId : teamIds) {
            Map<String, BattleTree> battles = ReadInDataObj.battlesForTeam(teamId);
            battles.values().forEach(this::analyzeBattle);
        }
        return wins/battles;
    }

    private void analyzeBattle(BattleTree tree) {
        if (battleT1WinCache.get(tree.id) != null) {
            if (battleT1WinCache.get(tree.id)) wins++;
            battles++;
        } else {
            for (State state : tree.states.values()) {
                if (state.isEndState()) {
                    if (state.getP1Status().getCurrentHP() > 0) {
                        battleT1WinCache.put(tree.id, true);
                        wins++;
                    } else {
                        battleT1WinCache.put(tree.id, false);
                    }
                    battles++;
                }
            }
        }
    }

}
