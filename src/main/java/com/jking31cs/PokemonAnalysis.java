package com.jking31cs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by May on 11/21/2016.
 */
public class PokemonAnalysis {

    public static class JsonOutput {
        public String attacker;
        public String defender;
        public String move;
        public Double confidence;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            JsonOutput that = (JsonOutput) o;

            if (attacker != null ? !attacker.equals(that.attacker) : that.attacker != null) {
                return false;
            }
            if (defender != null ? !defender.equals(that.defender) : that.defender != null) {
                return false;
            }
            if (move != null ? !move.equals(that.move) : that.move != null) {
                return false;
            }
            return confidence != null ? confidence.equals(that.confidence) : that.confidence == null;

        }

        @Override
        public int hashCode() {
            int result = attacker != null ? attacker.hashCode() : 0;
            result = 31 * result + (defender != null ? defender.hashCode() : 0);
            result = 31 * result + (move != null ? move.hashCode() : 0);
            result = 31 * result + (confidence != null ? confidence.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("JsonOutput{");
            sb.append("attacker='").append(attacker).append('\'');
            sb.append(", defender='").append(defender).append('\'');
            sb.append(", move='").append(move).append('\'');
            sb.append(", confidence=").append(confidence);
            sb.append('}');
            return sb.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        File outputFile = new File("output/pokemon-next-moves.json");
        if (outputFile.exists()) outputFile.delete();
        outputFile.createNewFile();

        final BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write('[');
        writer.flush();
        final BooleanHolder stop = new BooleanHolder();
        stop.bool = false;

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                stop.bool = true;
                try {
                    writer.write(']');
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    //ignore since I'm screwed if this fails.
                }
            }
        });
        BooleanHolder isFirst = new BooleanHolder();
        Lock lockWrite = new ReentrantLock();
        final BlockingDeque<String> queue = new LinkedBlockingDeque<>(Arrays.asList(
                "Starmie",
                "Lapras",
                "Mewtwo",
                "Kingler",
                "Blastoise",
                "Jolteon",
                "Raichu",
                "Poliwrath",
                "Cloyster",
                "Venusaur"
        ));
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!queue.isEmpty()) {
                        String pokemon1 = queue.poll();
                        for (String pokemon2 : PokemonListingCache.getAll().keySet()) {
                            if (stop.bool) {
                                return;
                            }
                            Map<String, Double> moveStatistics =
                                    new PokemonSituationAnalysis(pokemon1, pokemon2).getMoveConfidence();
                            if (pokemon1.equals(pokemon2)) continue;

                            JsonOutput output = new JsonOutput();
                            output.attacker = pokemon1;
                            output.defender = pokemon2;
                            output.move = moveStatistics.keySet().stream().findFirst().orElse("");
                            output.confidence = moveStatistics.values().stream().findFirst().orElse(0d);
                            synchronizedWrite(output, writer, isFirst, lockWrite);
                            isFirst.bool = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();


    }

    private static class BooleanHolder {
        boolean bool = true;
    }

    private static void synchronizedWrite(JsonOutput output, BufferedWriter writer, BooleanHolder isFirst, Lock lock) {
        lock.lock();
        try {
            if (!isFirst.bool) {
                writer.write(',');
            }
            writer.write(new ObjectMapper().writeValueAsString(output));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

}
