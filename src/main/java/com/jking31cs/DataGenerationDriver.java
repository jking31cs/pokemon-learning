package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jking31cs.state.Team;

import static com.jking31cs.IdGenerator.randomId;

/**
 * Created by jking31 on 11/8/16.
 */
public class DataGenerationDriver {
    public GeneratedData generatedData = new GeneratedData();
    private File outputDirectory = new File("output");

    public DataGenerationDriver() throws IOException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
        File teamFile = new File(outputDirectory, "teams.json");
        if (teamFile.exists()) {
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new GuavaModule());
            JavaType teamType = om.getTypeFactory().constructMapType(HashMap.class, String.class, Team.class);
            generatedData.addAllTeams(om.readValue(teamFile, teamType));
        } else {
            buildTeams();
        }
        simulateBattleTrees();
    }

    public static void main(String[] args) throws IOException {
        DataGenerationDriver driver = new DataGenerationDriver();
    }

    private void buildTeams() throws IOException {
        Map<String, PokemonWithTypes> allPokemon = PokemonListingCache.getAll();
        List<String> names = allPokemon.keySet().stream().collect(Collectors.toList());
        for (int i = 0; i < names.size(); i++) {
            for (int j = 0; j < names.size(); j++) {
                if (j == i) continue;
                for (int k = 0; k < names.size(); k++) {
                    if (k == i || j == k) continue;
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

            //First, detect if we already did this team.
            String teamId = t1.getId();
            if (new File(outputDirectory, "simulated-battles/" + teamId.substring(0,2) + "/battle-" + teamId +".json").exists()) {
                ++count;
                System.out.println(String.format("Percent Complete: %.5f", ((100.0d * (count)) /  generatedData.teams.size())));
                continue;
            }

            //Now simulate some battles.
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
            System.out.println(String.format("Percent Complete: %.5f", ((100.0d * (count)) /  generatedData.teams.size())));
            File outputDir = new File("output/simulated-battles");
            if (!outputDir.exists()) outputDir.mkdir();
            String subDir = t1.getId().substring(0,2);
            File targetDir = new File(outputDir, subDir);
            if (!targetDir.exists()) targetDir.mkdir();
            File resultFile = new File(targetDir, "battle-" + t1.getId() + ".json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new GuavaModule());
            objectMapper.writeValue(resultFile, generatedData.battleTrees);
            generatedData.battleTrees = new HashMap<>();
        }
    }

}
