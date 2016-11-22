package com.jking31cs;

import com.jking31cs.state.BattleTree;
import com.jking31cs.state.Edge;
import com.jking31cs.state.State;

import java.io.IOException;
import java.util.*;

/**
 * Created by May on 11/21/2016.
 */
public class PokemonSituationAnalysis {
    private int temp;
    private final String pokemon1;
    private final String pokemon2;

    private Map<String, Integer> p1MoveWinToTimes = new HashMap<>();
    private Map<String, Integer> p1MoveToTimes = new HashMap<>();



    public PokemonSituationAnalysis(String pokemon1,String pokemon2) {
        this.pokemon1 = pokemon1;
        this.pokemon2=pokemon2;
    }

    public Map<String, Double> getMoveConfidence() throws IOException {
        //TODO all your team gathering and stuff here.
        List<String> teamIds = TeamReader.pokemonTeamIndex().get(pokemon1);
        for (String teamId : teamIds) {
            Map<String, BattleTree> battles = ReadInDataObj.battlesForTeam(teamId);
            for (BattleTree bt : battles.values()) {
                analyzeBattle(bt);
            }
        }
        Map <String,Double> confidence=new HashMap<>();
        String bestMove="";
        double bestConfidence=0;
        for (Map.Entry<String, Integer> entry : p1MoveToTimes.entrySet()) {
            double curConfidence=(double)p1MoveWinToTimes.get(entry.getKey())/entry.getValue();
            if (curConfidence>bestConfidence) {
                bestConfidence = curConfidence;
                bestMove = entry.getKey();
            }
        }

        confidence.put(bestMove,bestConfidence);

        return confidence;
    }
    //find the battle tree has both pokemon1 and pokemon2
    private void analyzeBattle(BattleTree tree) {
        for (State state : tree.states.values()) {
            if (state.isInitState()) {
                //System.out.println(pokemon2+state.getT2Status().getP1().getName()+state.getT2Status().getP2().getName()+state.getT2Status().getP3().getName());
                if ((!state.getT2Status().getP1().getName().equals(pokemon2))
                    && (!state.getT2Status().getP2().getName().equals(pokemon2))
                    && (!state.getT2Status().getP3().getName().equals(pokemon2))) {
                    break;
                }
                calculate1(tree);
            }
        }
    }

    //all no-end states
    private void calculate1(BattleTree tree){
        for (State state : tree.states.values()){
            if (!state.getP1Status().getName().equals(pokemon1) || !state.getP2Status().getName().equals(pokemon2)) continue;

            if (!state.isEndState()){
                Set<String> edges=tree.stateToEdgeMap.get(state.getId());
                // check values
                for (String edge : edges) {
                    Edge currentEdge = tree.edges.get(edge);

                    if (currentEdge.getP1Move().getMove().isPresent()) {
                        Move p1Move = currentEdge.getP1Move().getMove().get();
                        int winCount = calculate2(tree, currentEdge);

                        if (p1MoveWinToTimes.containsKey(p1Move.getName())) {
                            int newCount = p1MoveWinToTimes.get(p1Move.getName()) + winCount;
                            p1MoveWinToTimes.remove(p1Move.getName());
                            p1MoveWinToTimes.put(p1Move.getName(), newCount);

                            int newTemp = p1MoveToTimes.get(p1Move.getName()) + temp;
                            p1MoveToTimes.remove(p1Move.getName());
                            p1MoveToTimes.put(p1Move.getName(), newTemp);

                        } else {
                            p1MoveWinToTimes.put(p1Move.getName(), winCount);
                            p1MoveToTimes.put(p1Move.getName(), temp);
                        }
                    }

                }
            }
        }
    }

    private int calculate2(BattleTree tree, Edge edge){
        int count=0;
        temp=0;
        Queue<String> stateQueue=new PriorityQueue<String>();
        stateQueue.add(edge.getNextStateId());
        while(!stateQueue.isEmpty()){
            //System.out.println(stateQueue.size());
            State curState=tree.states.get(stateQueue.remove());
            //System.out.println(stateQueue.size());
            if (curState.isEndState()){
                temp++;
                if (curState.getP1Status().getCurrentHP() > 0) count++;

            }else{
                Set<String> edges=tree.stateToEdgeMap.get(curState.getId());
                Iterator iterator = edges.iterator();
                // check values
                while (iterator.hasNext()){
                    Edge edge_in=tree.edges.get(iterator.next());
                    stateQueue.add(edge_in.getNextStateId());
                }
            }
        }
        //System.out.println(count);
        return count;
    }
}
