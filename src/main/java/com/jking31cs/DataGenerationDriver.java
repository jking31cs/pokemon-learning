package com.jking31cs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.Sets;
import com.jking31cs.state.BattleTree;
import com.jking31cs.state.Team;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.jking31cs.IdGenerator.randomId;

/**
 * Created by jking31 on 11/8/16.
 */
public class DataGenerationDriver {
    public GeneratedData generatedData = new GeneratedData();
    private File outputDirectory = new File("output");
    private final Set<String> pokemonSet;
    private final int setSize;
    private final int numberOfBattlesPerTeam;
    private final boolean skipExistingFiles;

    private static class Arguments {
        /**
         * 1 means we only care about 1 pokemon to look at when picking teams
         * 2 means we care about the team contain 2/3 pokemon
         * 3 is the whole team.
         */
        private int setSize = 1;

        private int numberOfBattlesPerTeam = 5;

        /**
         * true means we skip over team battle files that already exist.
         * false means we add onto that file.
         */
        private boolean skipExistingFiles = true;
    }

    public DataGenerationDriver(Set<String> pokemonToAnalyze, Arguments args) throws IOException {
        this.pokemonSet = pokemonToAnalyze;
        this.setSize = args.setSize;
        this.numberOfBattlesPerTeam = args.numberOfBattlesPerTeam;
        this.skipExistingFiles = args.skipExistingFiles;
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

    private Set<Set<String>> pokemonSets() {
        Set<Set<String>> pokemonSets = new HashSet<>();
        for (String p1 : this.pokemonSet) {
            if (setSize == 1) {
                pokemonSets.add(Sets.newHashSet(p1));
            } else {
                for (String p2 : this.pokemonSet) {
                    if (p1.equals(p2)) continue;
                    if (setSize == 2) {
                        pokemonSets.add(Sets.newHashSet(p1, p2));
                    } else {
                        for (String p3 : this.pokemonSet) {
                            if (p1.equals(p3) || p2.equals(p3)) continue;
                            pokemonSets.add(Sets.newHashSet(p1,p2,p3));
                        }
                    }
                }
            }
        }
        return pokemonSets;
    }

    public static void main(String[] args) throws IOException {
        Arguments arguments = new Arguments();
        for (int i = 0; i < args.length-1; i+=2) {
            String field = args[i];
            switch (field) {
                case "-setSize":
                    arguments.setSize = Integer.parseInt(args[i+1]);
                    break;
                case "-numberOfBattles":
                    arguments.numberOfBattlesPerTeam = Integer.parseInt(args[i+1]);
                    break;
                case "-skipExistingFiles":
                    arguments.skipExistingFiles = Boolean.parseBoolean(args[i+1]);
            }
        }
        new DataGenerationDriver(GetPokemonToAnalyze.get(), arguments);
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
        Map<String, Set<String>> allTeamIndexes = TeamReader.pokemonTeamIndex();
        Set<Set<String>> setsToAnalyze = pokemonSets();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        JavaType battleTreeType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, BattleTree.class);
        Set<String> analyzedTeams = new HashSet<>();
        for (Set<String> setOfPokemon : setsToAnalyze) {

            //Only look at teams that contain the pokemon sets we care about.
            Set<Team> teams = new HashSet<>(generatedData.teams.values());
            for (String pokemon : setOfPokemon) {
                teams = Sets.intersection(teams, new HashSet<>(allTeamIndexes.get(pokemon).stream().map(
                    s -> generatedData.teams.get(s)).collect(Collectors.toList())));
            }

            for (Team t1 : teams) {

                //First, detect if we already did this team.
                String teamId = t1.getId();
                if (analyzedTeams.contains(teamId)) continue;
                File currentBattleFile = new File(outputDirectory,
                    "simulated-battles/" + teamId.substring(0, 2) + "/battle-" + teamId + ".json");
                if (currentBattleFile.exists()) {
                    if (skipExistingFiles) {
                        analyzedTeams.add(teamId);
                        continue;
                    }
                    generatedData.addAllBattleTrees(objectMapper.readValue(currentBattleFile, battleTreeType));
                }

                //Now simulate some battles.
                int battleCount = 0;
                while (battleCount < numberOfBattlesPerTeam) {
                    Team t2 = generatedData.teams.values().stream().collect(Collectors.toList()).get(
                        new Random().nextInt(generatedData.teams.size())
                    );
                    if (t1.equals(t2)) {
                        continue; //Not doing mirror matches, blah
                    }
                    generatedData.addBattleTree(BattleTreeBuilder.createBattleTree(t1, t2));
                    ++battleCount;
                }
                File outputDir = new File("output/simulated-battles");
                if (!outputDir.exists()) outputDir.mkdir();
                String subDir = t1.getId().substring(0,2);
                File targetDir = new File(outputDir, subDir);
                if (!targetDir.exists()) targetDir.mkdir();
                File resultFile = new File(targetDir, "battle-" + teamId + ".json");

                objectMapper.writeValue(resultFile, generatedData.battleTrees);
                analyzedTeams.add(teamId);
                generatedData.battleTrees = new HashMap<>();
            }
            ++count;
            System.out.println(String.format("Percent Complete: %.5f", ((100.0d * (count)) /  setsToAnalyze.size())));
        }
    }

}
