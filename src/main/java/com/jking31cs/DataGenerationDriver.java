package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jking31cs.state.Team;

import static com.jking31cs.IdGenerator.randomId;

/**
 * Created by jking31 on 11/8/16.
 */
public class DataGenerationDriver {
    public GeneratedData generatedData = new GeneratedData();

    public DataGenerationDriver() throws IOException {
        File outputDirectory = new File("output");
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
        buildTeams();
        simulateBattleTrees();
    }

    public static void main(String[] args) throws IOException {
        DataGenerationDriver driver = new DataGenerationDriver();
    }

    private void buildTeams() throws IOException {
        Map<String, PokemonWithTypes> allPokemon = PokemonListingCache.getAll();
        List<String> names = allPokemon.keySet().stream().collect(Collectors.toList());
        for (int i = 0; i < names.size() - 2; i++) {
            for (int j = i + 1; j < names.size() - 1; j++) {
                for (int k = j + 1; k < names.size(); k++) {
                    Team team = new Team(
                            randomId(),
                            names.get(i),
                            names.get(j),
                            names.get(k)

                    );
                    this.generatedData.addTeam(team);
                }
            }
        }
        System.out.println("Created all teams, size:" + generatedData.teams.size());
        new ObjectMapper().writeValue(new File("output/teams.json"), generatedData.teams);
    }

    private void simulateBattleTrees() throws IOException {
        int count = 0;
        for (Team t1 : generatedData.teams.values()) {
            int battleCount = 0;
            while (battleCount < 5) {
                Team t2 = generatedData.teams.values().stream().collect(Collectors.toList()).get(
                    new Random().nextInt(generatedData.teams.size())
                );
                if (t1.equals(t2)) {
                    continue; //Not doing mirror matches, blah
                }
                generatedData.addBattleTree(BattleTreeBuilder.createBattleTree(t1, t2));
                ++battleCount;
            }
            ++count;
            System.out.println(String.format("Percent Complete: %.5f", ((1.0d * (count)) /  generatedData.teams.size())));
            File resultFile = new File("output/simulated-battles" + count + ".json");
            new ObjectMapper().writeValue(resultFile, generatedData.battleTrees);
            generatedData.battleTrees = new HashMap<>();
        }
    }

}
