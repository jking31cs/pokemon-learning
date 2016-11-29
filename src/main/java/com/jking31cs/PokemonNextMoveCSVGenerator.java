package com.jking31cs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by jking31 on 11/28/16.
 */
public class PokemonNextMoveCSVGenerator {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, PokemonAnalysis.JsonOutput.class);

        List<PokemonAnalysis.JsonOutput> nextMoves = mapper.readValue(new File("output/pokemon-next-moves.json"), listType);
        System.out.println(nextMoves.size());
        BufferedWriter writer  = new BufferedWriter(new FileWriter(new File("output/pokemon-next-moves.csv")));
        int count = 0;
        for (PokemonAnalysis.JsonOutput o : nextMoves) {
            System.out.println(++count);
            StringBuffer sb = new StringBuffer();
            sb.append(o.attacker + ",");
            sb.append(o.defender + ",");
            String move = o.move;
            if (move.isEmpty()) move = "NONE";
            sb.append(move + ",");
            sb.append(o.confidence + ",");
            writer.write(o.toString() + '\n');

        }

    }
}
