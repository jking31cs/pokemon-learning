package com.jking31cs;

import com.jking31cs.state.Team;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jking31cs on 10/31/16.
 */
public class MainDriver {

    public static void main(String[] args) throws IOException {
        Map<String, PokemonWithTypes> allPokemon = PokemonListingCache.getAll();
        Map<String, List<Move>> moveSets = MoveListingCache.getMoveSets();

    }

    private static long randomId() {
        return Instant.now().toEpochMilli();
    }


}
