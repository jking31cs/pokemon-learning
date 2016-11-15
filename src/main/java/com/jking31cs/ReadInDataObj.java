package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jking31cs.state.BattleTree;

/**
 * Created by jking31 on 11/15/16.
 */
public class ReadInDataObj {

    public static void main(String[] args) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new GuavaModule());
        JavaType mapType = om.getTypeFactory().constructMapType(HashMap.class, String.class, BattleTree.class);
        Map<String, BattleTree> readInValues = om.readValue(new File("output/simulated-battles1.json"), mapType);
        System.out.println(readInValues);
    }
}
