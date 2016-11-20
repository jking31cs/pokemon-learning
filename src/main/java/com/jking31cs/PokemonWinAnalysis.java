package com.jking31cs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jking31cs.state.BattleTree;
import com.jking31cs.state.State;

/**
 * Created by jking31 on 11/20/16.
 */
public class PokemonWinAnalysis {

    private double wins = 0;
    private double battles = 0;
    private final String pokemon;

    public PokemonWinAnalysis(String pokemon) {
        this.pokemon = pokemon;
    }

    public Double getConfidence() throws IOException {
        //TODO all your team gathering and stuff here.
        List<String> teamIds = TeamReader.pokemonTeamIndex().get(pokemon);
        for (String teamId : teamIds) {
            Map<String, BattleTree> battles = ReadInDataObj.battlesForTeam(teamId);
            for (BattleTree bt : battles.values()) {
                analyzeBattle(bt);
            }
        }
        return wins/battles;
    }

    private void analyzeBattle(BattleTree tree) {
        for (State state : tree.states.values()) {
            if (state.isEndState()) {
                if (state.getP1Status().getCurrentHP() > 0) {
                    wins++;
                }
                battles++;
            }
        }
    }

}
