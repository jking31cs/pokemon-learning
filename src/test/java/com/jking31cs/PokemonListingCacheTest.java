package com.jking31cs;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by jking31cs on 10/11/16.
 */
public class PokemonListingCacheTest {
    @Test
    public void testGetAll() throws IOException {
        System.out.println(PokemonListingCache.getAll());
    }
}
