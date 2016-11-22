package com.jking31cs;

import com.jking31cs.state.PokemonStatus;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;

/**
 * Created by jking31cs on 11/21/16.
 */
public class BattleTreeBuilderTest {
    @Test
    public void damageDealt() throws Exception {
        BattleTreeBuilder.allPokemon = PokemonListingCache.getAll();
        BattleTreeBuilder.moveSets = MoveListingCache.getMoveSets();
        double damage = BattleTreeBuilder.damageDealt(
            new PokemonStatus("Weezing", "Weezing", 500),
            new PokemonStatus("Golem", "Golem", 500),
            MoveListingCache.getMoveSets().get("Weezing").stream().filter(move -> move.getName().equals("Thunder")).findFirst().get()
        );

        System.out.println(damage);
    }

}