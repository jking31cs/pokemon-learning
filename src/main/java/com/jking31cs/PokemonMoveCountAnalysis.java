package com.jking31cs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jking31cs.state.BattleTree;
import com.jking31cs.state.Edge;
import com.jking31cs.state.State;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jking31cs on 11/27/16.
 */
public class PokemonMoveCountAnalysis {

    static final Map<String, Map<String, Long>> moveCount = new HashMap<>();

    static final Lock lock = new ReentrantLock();

    static void update(String pokemon, String move) {
        lock.lock();
        if (!moveCount.containsKey(pokemon)) moveCount.put(pokemon, new HashMap<>());
        if (!moveCount.get(pokemon).containsKey(move)) moveCount.get(pokemon).put(move, 0l);
        long updatedCount = 1+moveCount.get(pokemon).get(move);
        moveCount.get(pokemon).put(move, updatedCount);
        lock.unlock();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final BlockingDeque<File> battleQueue =
            new LinkedBlockingDeque<>(Arrays.asList(new File("output/simulated-battles").listFiles()));

        final ObjectMapper om = new ObjectMapper();
        final JavaType jt = om.getTypeFactory().constructMapType(HashMap.class, String.class, BattleTree.class);
        om.registerModule(new GuavaModule());

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while(!battleQueue.isEmpty()) {
                    File battleFolder = battleQueue.poll();
                    for (File battleFile : battleFolder.listFiles()) {
                        try {
                            Map<String, BattleTree> battles = om.readValue(battleFile, jt);
                            for (BattleTree bt : battles.values()) {
                                State curState = bt.states.get(bt.initialStateId);

                                while (!curState.isEndState()) {
                                    String pkmn = curState.getP1Status().getName();
                                    Optional<String> firstEdge = bt.stateToEdgeMap.get(curState.getId()).stream().findFirst();
                                    if (!firstEdge.isPresent()) {
                                        break;
                                    }
                                    String edgeId = firstEdge.get();
                                    Edge edge = bt.edges.get(edgeId);
                                    if (edge.getP1Move().getMove().isPresent()) {
                                        String move = edge.getP1Move().getMove().get().getName();
                                        update(pkmn, move);
                                    }
                                    curState = bt.states.get(edge.getNextStateId());
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };
        Thread thread1 = new Thread(task);
        thread1.start();
        Thread thread2 = new Thread(task);
        thread2.start();
        Thread thread3 = new Thread(task);
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        om.writeValue(new File("output/move-analysis.json"), moveCount);
    }

}
