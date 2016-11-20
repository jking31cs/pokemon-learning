package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jking31cs.state.Team;

/**
 * Created by jking31 on 11/20/16.
 */
public class TeamReader {

    public static Map<String, Team> readTeamsFromFile() throws IOException {
        File teamFile = new File("output/teams.json");
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        JavaType teamType = om.getTypeFactory().constructMapType(HashMap.class, String.class, Team.class);
        return om.readValue(teamFile, teamType);
    }
}
