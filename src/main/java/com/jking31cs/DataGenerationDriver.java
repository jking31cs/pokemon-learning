package com.jking31cs;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jking31cs.state.Team;

import static com.jking31cs.IdGenerator.randomId;

/**
 * Created by jking31 on 11/8/16.
 */
public class DataGenerationDriver {
    public GeneratedData generatedData = new GeneratedData();

    public DataGenerationDriver() throws IOException {
        buildTeams();
        simulateBattleTrees();
    }

    public static void main(String[] args) throws IOException {
        DataGenerationDriver driver = new DataGenerationDriver();
        //new ObjectMapper().writeValue(new File("allData.json"), driver.generatedData);
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
    }

    private void simulateBattleTrees() throws IOException {
        int count = 1;
        for (Team t1 : generatedData.teams.values()) {
            for (Team t2 : generatedData.teams.values()) {
                if (t1.equals(t2)) {
                    continue; //Not doing mirror matches, blah
                }
                generatedData.addBattleTree(BattleTreeBuilder.createBattleTree(t1, t2));
                if (generatedData.battleTrees.size() > count) {
                    System.out.println("Simulated " + ((++count)) + " battles since last output");
                }
            }
        }
    }

}
