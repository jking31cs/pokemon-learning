package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jking31cs.state.BattleTree;
import com.jking31cs.state.Team;

/**
 * Created by jking31 on 11/15/16.
 */
public class ReadInDataObj {

    public static void main(String[] args) throws IOException {

        Map<String, Team> teams = TeamReader.readTeamsFromFile();
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        JavaType mapType = om.getTypeFactory().constructMapType(HashMap.class, String.class, BattleTree.class);
        for (String id : teams.keySet()) {
            String dirKey = id.substring(0,2);
            File battleFile = new File("output/simulated-battles/" + dirKey + "/battle-" + id + ".json");
            if (!battleFile.exists()) continue;
            Map<String, BattleTree> readInValues = om.readValue(battleFile, mapType);
            System.out.println(readInValues);
        }
    }

    public static Map<String, BattleTree> battlesForTeam(String id) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        JavaType mapType = om.getTypeFactory().constructMapType(HashMap.class, String.class, BattleTree.class);
        String dirKey = id.substring(0,2);
        File battleFile = new File("output/simulated-battles/" + dirKey + "/battle-" + id + ".json");
        if (!battleFile.exists()) throw new IllegalArgumentException("No battle trees exist for this team id.");
        Map<String, BattleTree> readInValues = om.readValue(battleFile, mapType);
        System.out.println(readInValues);
    }
}
