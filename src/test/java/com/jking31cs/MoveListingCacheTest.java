package com.jking31cs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Created by jking31 on 10/18/16.
 */
public class MoveListingCacheTest {

    @Test
    public void testCreateMoveListingCache() throws IOException {
        Map<String, List<Move>> mapping = MoveListingCache.get();
        List<Move> pikachu = mapping.get("Bulbasaur");
        for(Move m : pikachu) {
            System.out.println(m);
        }
    }
}