package com.jking31cs;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.jking31cs.state.BattleTree;
import com.jking31cs.state.Edge;
import com.jking31cs.state.PokemonStatus;
import com.jking31cs.state.State;
import com.jking31cs.state.Team;
import com.jking31cs.state.TeamStatus;

import static com.jking31cs.IdGenerator.randomId;

/**
 * Testing BattleTree Idea.
 */
public class BattleTreeBuilder {

    static Map<String, PokemonWithTypes> allPokemon;
    static Map<String, List<Move>> moveSets;

    static double thresholdDamagePercent = .2d;
    public static BattleTree createBattleTree(Team t1, Team t2) throws IOException {
        allPokemon = PokemonListingCache.getAll();
        moveSets = MoveListingCache.getMoveSets();

        TeamStatus t1Status = getInitStatus(t1);
        TeamStatus t2Status = getInitStatus(t2);

        PokemonStatus p1 = t1Status.getP1();
        PokemonStatus p2 = t2Status.getP1();

        State initState = new State(
                randomId(),
                t1.getId(),
                t2.getId(),
                t1Status,
                t2Status,
                p1,
                p2,
                false,
                true
        );

        BattleTree battleTree = new BattleTree();
        battleTree.addState(initState);
        battleTree.initialStateId = initState.getId();
        Queue<State> stateQueue = new ArrayDeque<>();
        stateQueue.add(initState);

        boolean speedTie = true;
        Edge prevEdge = null;
        boolean hasRepeated1 = true;
        boolean hasRepeated2 = true;
        boolean mustWait1 = false;
        boolean mustWait2 = false;
        boolean isSecondTurn1 = false;
        boolean isSecondTurn2 = false;
        while (!stateQueue.isEmpty()) {
            boolean notUsingRepeatedMove1 = true;
            boolean notUsingRepeatedMove2 = true;
            State currentState = stateQueue.poll();
            if (currentState.isEndState()) continue;
            //The goal here is to determine the move for each pokemon that does the most damage to the opponent.
            PokemonStatus p1Status = currentState.getP1Status();
            PokemonStatus p2Status = currentState.getP2Status();
            TeamStatus curT1Status = currentState.getT1Status();
            TeamStatus curT2Status = currentState.getT2Status();
            Pokemon pokemon1 = allPokemon.get(p1Status.getName()).getPokemon();
            Pokemon pokemon2 = allPokemon.get(p2Status.getName()).getPokemon();
            double damage1 = 0;
            double minDamageNeeded1 =
                Math.min(pokemon2.getHp() * thresholdDamagePercent,
                    p2Status.getCurrentHP());
            Move move1 = null;
            double damage2 = 0;
            Move move2 = null;
            double minDamageNeeded2 =
                Math.min(pokemon1.getHp() * thresholdDamagePercent,
                    p1Status.getCurrentHP());
            for (Move m1 : moveSets.get(p1Status.getName())) {
                double damageDealt = damageDealt(p1Status, p2Status, m1);
                if (damage1 < damageDealt) {
                    damage1 = damageDealt;
                    move1 = m1;
                }
            }
            for (Move m2 : moveSets.get(p2Status.getName())) {
                double damageDealt = damageDealt(p2Status, p1Status, m2);
                if (damage2 < damageDealt) {
                    damage2 = damageDealt;
                    move2 = m2;
                }
            }
            boolean switch1 = false;
            boolean switch2 = false;
            Edge.MoveAction moveAction1 = Edge.MoveAction.move(move1);
            Edge.MoveAction moveAction2 = Edge.MoveAction.move(move2);
            boolean canSwitch1 = true;
            if (prevEdge != null && prevEdge.getP1Move().getMove().isPresent() &&
                    SpecialMoves.repeatMoves.contains(prevEdge.getP1Move().getMove().get().getName())
                    && !hasRepeated1) {
                moveAction1 = Edge.MoveAction.move(prevEdge.getP1Move().getMove().get());
                hasRepeated1 = true;
                damage1 = damageDealt(p1Status, p2Status, prevEdge.getP1Move().getMove().get());
                canSwitch1 = false;
                notUsingRepeatedMove1 = false;
            }
            if (mustWait1) {
                damage1 = 0;
                canSwitch1 = false;
                moveAction1 = Edge.MoveAction.waitTurn();
                mustWait1 = false;
            }
            if (SpecialMoves.secondTurnMoves.contains(move1.getName())) {
                damage1 = 0;
                canSwitch1 = false;
                moveAction1 = Edge.MoveAction.waitTurn();
                isSecondTurn1 = true;
            }
            boolean canSwitch2 = true;
            if (prevEdge != null && prevEdge.getP2Move().getMove().isPresent() &&
                    SpecialMoves.repeatMoves.contains(prevEdge.getP2Move().getMove().get().getName())
                    && !hasRepeated2) {
                moveAction2 = Edge.MoveAction.move(prevEdge.getP2Move().getMove().get());
                hasRepeated2 = true;
                damage2 = damageDealt(p2Status, p1Status, prevEdge.getP2Move().getMove().get());
                canSwitch2 = false;
                notUsingRepeatedMove2 = false;
            }
            if (mustWait2) {
                damage2 = 0;
                canSwitch2 = false;
                moveAction2 = Edge.MoveAction.waitTurn();
                mustWait2 = false;
            }
            if (SpecialMoves.secondTurnMoves.contains(move2.getName())) {
                damage2 = 0;
                canSwitch2 = false;
                moveAction2 = Edge.MoveAction.waitTurn();
                isSecondTurn2 = true;
            }

            PokemonStatus temp1 = new PokemonStatus(
                p1Status.getId(),
                p1Status.getName(),
                p1Status.getCurrentHP()
            );
            PokemonStatus temp2 = new PokemonStatus(
                p2Status.getId(),
                p2Status.getName(),
                p2Status.getCurrentHP()
            );
            if (canSwitch1 && damage1 < minDamageNeeded1) {
                int minDamageDealt = Integer.MAX_VALUE;
                if (curT1Status.getP1().getId() != temp1.getId()
                    && damageDealt(p2, curT1Status.getP1(), move2) < minDamageDealt
                    && curT1Status.getP1().getCurrentHP() > 0) {
                    damage1 = 0;
                    switch1 = true;
                    moveAction1 = Edge.MoveAction.switchTo(curT1Status.getP1());
                    p1Status = curT1Status.getP1();
                }
                if (curT1Status.getP2().getId() != temp1.getId()
                    && damageDealt(p2, curT1Status.getP2(), move2) < minDamageDealt
                    && curT1Status.getP2().getCurrentHP() > 0) {
                    damage1 = 0;
                    switch1 = true;
                    moveAction1 = Edge.MoveAction.switchTo(curT1Status.getP2());
                    p1Status = curT1Status.getP2();
                }
                if (curT1Status.getP3().getId() != temp1.getId()
                    && damageDealt(p2, curT1Status.getP3(), move2) < minDamageDealt
                    && curT1Status.getP3().getCurrentHP() > 0) {
                    damage1 = 0;
                    switch1 = true;
                    moveAction1 = Edge.MoveAction.switchTo(curT1Status.getP3());
                    p1Status = curT1Status.getP3();
                }
            }
            if (canSwitch2 && damage2 < minDamageNeeded2) {
                int minDamageDealt = Integer.MAX_VALUE;
                if (curT2Status.getP1().getId() != temp2.getId()
                    && damageDealt(p1, curT2Status.getP1(), move1) < minDamageDealt
                    && curT2Status.getP1().getCurrentHP() > 0) {
                    damage2 = 0;
                    switch2 = true;
                    moveAction2 = Edge.MoveAction.switchTo(curT2Status.getP1());
                    p2Status = curT2Status.getP1();
                }
                if (curT2Status.getP2().getId() != temp2.getId()
                    && damageDealt(p1, curT2Status.getP2(), move1) < minDamageDealt
                    && curT2Status.getP2().getCurrentHP() > 0) {
                    damage2 = 0;
                    switch2 = true;
                    moveAction1 = Edge.MoveAction.switchTo(curT2Status.getP2());
                    p2Status = curT2Status.getP2();
                }
                if (curT2Status.getP3().getId() != temp2.getId()
                    && damageDealt(p1, curT2Status.getP3(), move1) < minDamageDealt
                    && curT2Status.getP3().getCurrentHP() > 0) {
                    damage2 = 0;
                    switch2 = true;
                    moveAction1 = Edge.MoveAction.switchTo(curT2Status.getP3());
                    p2Status = curT2Status.getP3();
                }
            }

            //Now that we know the damage rolls, let's create a new state.
            PokemonStatus newP1Status = null;
            PokemonStatus newP2Status = null;
            TeamStatus newT1Status = null;
            TeamStatus newT2Status = null;

            //First apply damage per speed rules.
            if (pokemon1.getSpeed() > pokemon2.getSpeed() || (pokemon1.getSpeed() == pokemon2.getSpeed() && speedTie)) {
                speedTie = !speedTie;
                newP2Status = new PokemonStatus(
                    randomId(),
                    p2Status.getName(),
                    (int) (p2Status.getCurrentHP() - damage1)
                );
                if (newP2Status.getCurrentHP() > 0) {
                    newP1Status = new PokemonStatus(
                        randomId(),
                        p1Status.getName(),
                        (int) (p1Status.getCurrentHP() - damage2)
                    );
                } else {
                    newP1Status = new PokemonStatus(
                        randomId(),
                        p1Status.getName(),
                        p1Status.getCurrentHP()
                    );
                }
            } else {
                speedTie = !speedTie;
                newP1Status = new PokemonStatus(
                    randomId(),
                    p1Status.getName(),
                    (int) (p1Status.getCurrentHP() - damage2)
                );
                if (newP1Status.getCurrentHP() > 0) {
                    newP2Status = new PokemonStatus(
                        randomId(),
                        p2Status.getName(),
                        (int) (p2Status.getCurrentHP() - damage1)
                    );
                } else {
                    newP2Status = new PokemonStatus(
                        randomId(),
                        p2Status.getName(),
                        p2Status.getCurrentHP()
                    );
                }
            }

            //Update Team Status
            if (curT1Status.getP1().getId() == p1Status.getId()) {
                newT1Status = new TeamStatus(
                    randomId(),
                    newP1Status,
                    curT1Status.getP2(),
                    curT1Status.getP3()
                );
            } else if (curT1Status.getP2().getId() == p1Status.getId()) {
                newT1Status = new TeamStatus(
                    randomId(),
                    curT1Status.getP1(),
                    newP1Status,
                    curT1Status.getP3()
                );
            } else if (curT1Status.getP3().getId() == p1Status.getId()) {
                newT1Status = new TeamStatus(
                    randomId(),
                    curT1Status.getP1(),
                    curT1Status.getP2(),
                    newP1Status
                );
            }

            if (curT2Status.getP1().getId() == p2Status.getId()) {
                newT2Status = new TeamStatus(
                    randomId(),
                    newP2Status,
                    curT2Status.getP2(),
                    curT2Status.getP3()
                );
            } else if (curT2Status.getP2().getId() == p2Status.getId()) {
                newT2Status = new TeamStatus(
                    randomId(),
                    curT2Status.getP1(),
                    newP2Status,
                    curT2Status.getP3()
                );
            } else if (curT2Status.getP3().getId() == p2Status.getId()) {
                newT2Status = new TeamStatus(
                    randomId(),
                    curT2Status.getP1(),
                    curT2Status.getP2(),
                    newP2Status
                );
            }

            //Switch out dead pokemon
            boolean isEndState = false;
            if (newP1Status.getCurrentHP() <= 0) {
                if (t1Status.getP1().getId() != p1Status.getId() && newT1Status.getP1().getCurrentHP() > 0) {
                    newP1Status = newT1Status.getP1();
                } else if (t1Status.getP2().getId() != p1Status.getId() && newT1Status.getP2().getCurrentHP() > 0) {
                    newP1Status = newT1Status.getP2();
                } else if (t1Status.getP3().getId() != p1Status.getId() && newT1Status.getP3().getCurrentHP() > 0) {
                    newP1Status = newT1Status.getP3();
                } else {
                    isEndState = true;
                }
            }

            if (newP2Status.getCurrentHP() <= 0) {
                if (t2Status.getP1().getId() != p2Status.getId() && newT2Status.getP1().getCurrentHP() > 0) {
                    newP2Status = newT2Status.getP1();
                } else if (t2Status.getP2().getId() != p2Status.getId() && newT2Status.getP2().getCurrentHP() > 0) {
                    newP2Status = newT2Status.getP2();
                } else if (t2Status.getP3().getId() != p2Status.getId() && newT2Status.getP3().getCurrentHP() > 0) {
                    newP2Status = newT2Status.getP3();
                } else {
                    isEndState = true;
                }
            }


            if (SpecialMoves.repeatMoves.contains(move1.getName()) && notUsingRepeatedMove1) {
                hasRepeated1 = false;
            }
            if (SpecialMoves.firstTurnMoves.contains(move1.getName())) {
                mustWait1 = true;
            }
            if (SpecialMoves.repeatMoves.contains(move2.getName()) && notUsingRepeatedMove2) {
                hasRepeated2 = false;
            }
            if (SpecialMoves.firstTurnMoves.contains(move2.getName())) {
                mustWait2 = true;
            }

            //Now create a new State.
            State newState = new State(
                randomId(),
                t1.getId(),
                t2.getId(),
                newT1Status,
                newT2Status,
                newP1Status,
                newP2Status,
                isEndState,
                false
            );
            battleTree.addState(newState);
            stateQueue.add(newState);
            Edge newEdge = new Edge(
                randomId(),
                currentState.getId(),
                newState.getId(),
                moveAction1,
                moveAction2
            );
            battleTree.addEdge(newEdge);
            prevEdge = newEdge;
            if (battleTree.states.size() > 100) {
                break;
            }
        }
        battleTree.id = randomId();

        return battleTree;
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



    //Assuming min damage for ease of analysis
    private static double damageDealt (PokemonStatus attacker, PokemonStatus defender, Move move) {
        //((200/7)*Attack\Special*MoveAttack)/Defense\Special)/50)+2)*STAB)*TypeModifier/10)*217)/255

        PokemonWithTypes attackerPokemon = allPokemon.get(attacker.getName());
        PokemonWithTypes defenderPokemon = allPokemon.get(defender.getName());
        boolean isSpecial = move.isSpecial();
        double attackDefensePortion = isSpecial
            ? (1.0d * attackerPokemon.getPokemon().getSpecial() / defenderPokemon.getPokemon().getSpecial())
            : (1.0d * attackerPokemon.getPokemon().getAttack() / defenderPokemon.getPokemon().getDefense());
        boolean isStab = attackerPokemon.getType1().getName() == move.getType();
        if (attackerPokemon.getType2() != null) {
            isStab = isStab || attackerPokemon.getType2().getName() == move.getType();
        }

        return ((175d/250d) * (attackDefensePortion) * move.getPower() + 2) * (isStab ? 1.5 : 1) * calculateTypeModifier(defenderPokemon, move);
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
        return modifier * .85;
    }
}
