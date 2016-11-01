package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jking31cs.state.BattleTree;
import com.jking31cs.state.Edge;
import com.jking31cs.state.PokemonStatus;
import com.jking31cs.state.State;
import com.jking31cs.state.Team;
import com.jking31cs.state.TeamStatus;

/**
 * Testing BattleTree Idea.
 *
 * TODO DON'T RUN THIS, IT'LL CREATE A BIG FILE.
 */
public class MainDriver {

    static Map<String, PokemonWithTypes> allPokemon;
    static Map<String, List<Move>> moveSets;
    public static void main(String[] args) throws IOException {
        allPokemon = PokemonListingCache.getAll();
        moveSets = MoveListingCache.getMoveSets();

        Team t1 = new Team(
                randomId(),
                "Blastoise",
                "Charizard",
                "Venusaur"
        );

        Team t2 = new Team(
                randomId(),
                "Poliwrath",
                "Jynx",
                "Gengar"
        );

        TeamStatus t1Status = getInitStatus(t1);
        TeamStatus t2Status = getInitStatus(t2);

        PokemonStatus p1 = t1Status.getP1();
        PokemonStatus p2 = t2Status.getP1();

        State initState = new State(
                randomId(),
                t1,
                t2,
                t1Status,
                t2Status,
                p1,
                p2,
                false,
                true
        );

        BattleTree battleTree = new BattleTree();
        battleTree.states.put(initState.getId(), initState);

        Queue<State> stateQueue = new ArrayDeque<>();
        stateQueue.add(initState);

        while (!stateQueue.isEmpty()) {
            State currentState = stateQueue.poll();
            if (currentState.isEndState()) {
                continue;
            }
            PokemonStatus curp1 = currentState.getP1Status();
            PokemonStatus curp2 = currentState.getP2Status();
            PokemonStatus newP1 = null;
            PokemonStatus newP2 = null;
            TeamStatus curT1Status = currentState.getT1Status();
            TeamStatus curT2Status = currentState.getT2Status();
            TeamStatus newT1Status = null;
            TeamStatus newT2Status = null;
            boolean isEndState = false;

            for (Move m1 : moveSets.get(currentState.getP1Status().getName())) {
                for (Move m2 : moveSets.get(currentState.getP2Status().getName())) {

                    //First, compare speeds to see who goes first.
                    if (allPokemon.get(curp1.getName()).getPokemon().getSpeed()
                            > allPokemon.get(curp2.getName()).getPokemon().getSpeed()) {
                        //Do the attacks from each side if possible.
                        newP2 = updateStatus(curp1, curp2, m1);
                        if (newP2.getCurrentHP() > 0) {
                            newP1 = updateStatus(curp2, curp1, m2);
                        } else {
                            newP1 = new PokemonStatus(
                                    randomId(),
                                    curp1.getName(),
                                    curp1.getCurrentHP()
                            );
                        }
                    } else {
                        //Same as above, but in opposite order
                        newP1 = updateStatus(curp2, curp1, m2);
                        if (newP1.getCurrentHP() > 0) {
                            newP2 = updateStatus(curp1, curp2, m1);
                        } else {
                            newP2 = new PokemonStatus(
                                    randomId(),
                                    curp2.getName(),
                                    curp2.getCurrentHP()
                            );
                        }
                    }

                    //If no damage is done by either pokemon, we will hit a deadlock.  This should prevent it.
                    if (newP1.getCurrentHP() == curp1.getCurrentHP() && newP2.getCurrentHP() == curp2.getCurrentHP()) {
                        continue;
                    }

                    //Update Team Status
                    if (curT1Status.getP1().getId() == curp1.getId()) {
                        newT1Status = new TeamStatus(
                                randomId(),
                                newP1,
                                curT1Status.getP2(),
                                curT1Status.getP3()
                        );
                    } else if (curT1Status.getP2().getId() == curp1.getId()) {
                        newT1Status = new TeamStatus(
                                randomId(),
                                curT1Status.getP1(),
                                newP1,
                                curT1Status.getP3()
                        );
                    } else if (curT1Status.getP3().getId() == curp1.getId()) {
                        newT1Status = new TeamStatus(
                                randomId(),
                                curT1Status.getP1(),
                                curT1Status.getP2(),
                                newP1
                        );
                    }

                    if (curT2Status.getP1().getId() == curp2.getId()) {
                        newT2Status = new TeamStatus(
                                randomId(),
                                newP2,
                                curT2Status.getP2(),
                                curT2Status.getP3()
                        );
                    } else if (curT2Status.getP2().getId() == curp2.getId()) {
                        newT2Status = new TeamStatus(
                                randomId(),
                                curT2Status.getP1(),
                                newP2,
                                curT2Status.getP3()
                        );
                    } else if (curT2Status.getP3().getId() == curp2.getId()) {
                        newT2Status = new TeamStatus(
                                randomId(),
                                curT2Status.getP1(),
                                curT2Status.getP2(),
                                newP2
                        );
                    }

                    //Switch out dead pokemon
                    if (newP1.getCurrentHP() <= 0) {
                        if (t1Status.getP1().getId() != curp1.getId() && t1Status.getP1().getCurrentHP() > 0) {
                            newP1 = t1Status.getP1();
                        } else if (t1Status.getP2().getId() != curp2.getId() && t1Status.getP2().getCurrentHP() > 0) {
                            newP1 = t1Status.getP2();
                        } else if (t1Status.getP3().getId() != curp2.getId() && t1Status.getP3().getCurrentHP() > 0) {
                            newP1 = t1Status.getP3();
                        } else {
                            isEndState = true;
                        }
                    }

                    if (newP2.getCurrentHP() <= 0) {
                        if (t2Status.getP1().getId() != curp2.getId() && t2Status.getP1().getCurrentHP() > 0) {
                            newP2 = t2Status.getP1();
                        } else if (t2Status.getP2().getId() != curp2.getId() && t2Status.getP2().getCurrentHP() > 0) {
                            newP2 = t2Status.getP2();
                        } else if (t2Status.getP3().getId() != curp2.getId() && t2Status.getP3().getCurrentHP() > 0) {
                            newP2 = t2Status.getP3();
                        } else {
                            isEndState = true;
                        }
                    }

                    //Now create a new State.
                    State newState = new State(
                            randomId(),
                            currentState.getT1(),
                            currentState.getT2(),
                            newT1Status,
                            newT2Status,
                            newP1,
                            newP2,
                            isEndState,
                            false
                    );
                    battleTree.states.put(newState.getId(), newState);
                    stateQueue.add(newState);
                    Edge newEdge = new Edge(
                            randomId(),
                            currentState,
                            newState,
                            Edge.MoveAction.move(m1),
                            Edge.MoveAction.move(m2)
                    );
                    battleTree.edges.put(newEdge.getId(), newEdge);
                }
            }
            if (stateQueue.size() > 100000) {
                System.out.println("That's too big.");
                break;
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File("output.json"), battleTree);
    }

    private static TeamStatus getInitStatus(Team t1) {
        return new TeamStatus(
                randomId(),
                getPokemonStatus(t1.getP1()),
                getPokemonStatus(t1.getP2()),
                getPokemonStatus(t1.getP3())
        );
    }

    private static PokemonStatus getPokemonStatus(String pokemon) {
        return new PokemonStatus(
                randomId(),
                pokemon,
                allPokemon.get(pokemon).getPokemon().getHp()
        );
    }

    private static long randomId() {
        return new SecureRandom().nextLong();
    }

    //Assuming min damage for ease of analysis
    private static PokemonStatus updateStatus (PokemonStatus attacker, PokemonStatus defender, Move move) {
        //((200/7)*Attack\Special*MoveAttack)/Defense\Special)/50)+2)*STAB)*TypeModifier/10)*217)/255

        PokemonWithTypes attackerPokemon = allPokemon.get(attacker.getName());
        PokemonWithTypes defenderPokemon = allPokemon.get(defender.getName());
        boolean isSpecial = move.isSpecial();
        double attackPortion = (200d/7d) * (isSpecial ? attackerPokemon.getPokemon().getSpecial() : attackerPokemon.getPokemon().getAttack())
                * move.getPower();
        double defensePortion = 1d * (isSpecial ? defenderPokemon.getPokemon().getSpecial() : defenderPokemon.getPokemon().getDefense());
        boolean isStab = attackerPokemon.getType1().getName() == move.getType();
        if (attackerPokemon.getType2() != null) {
            isStab = isStab || attackerPokemon.getType2().getName() == move.getType();
        }

        double damage = ((((attackPortion / defensePortion) + 2) * (isStab ? 1.5 : 1) * (calculateTypeModifier(defenderPokemon, move) / 10)) * 217) / 255;

        return new PokemonStatus(
                randomId(),
                defender.getName(),
                defender.getCurrentHP() - ((int) (damage + .5d))
        );
    }

    private static double calculateTypeModifier(PokemonWithTypes defender, Move move) {
        double modifier = 1;
        boolean has2Type = defender.getType2() != null;
        if (defender.getType1().getWeaknesses().contains(move.getType())) {
            modifier = modifier * 2;
        }
        if (has2Type && defender.getType1().getWeaknesses().contains(move.getType())) {
            modifier = modifier * 2;
        }
        if (defender.getType1().getResistances().contains(move.getType())) {
            modifier = modifier * .5d;
        }
        if (has2Type && defender.getType1().getResistances().contains(move.getType())) {
            modifier = modifier * .5d;
        }
        if (defender.getType1().getNullifications().contains(move.getType())) {
            modifier = modifier * 0;
        }
        if (has2Type && defender.getType1().getNullifications().contains(move.getType())) {
            modifier = modifier * 0;
        }
        return modifier;
    }
}
