package com.jking31cs;

import java.io.IOException;

import org.junit.Test;

/**
 * Created by jking31 on 10/18/16.
 */
public class MoveListingCacheTest {

    @Test
    public void testCreateMoveListingCache() throws IOException {
        System.out.println(new MoveListingCache().pokemonMoveSetsMapping);
    }
}