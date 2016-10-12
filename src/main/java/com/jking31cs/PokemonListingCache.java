package com.jking31cs;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Creates a listing of pokemon and stores it in memory.
 */
public class PokemonListingCache {

    private static PokemonListingCache instance;

    public static List<PokemonWithTypes> getAll() throws IOException {
        if (instance == null) {
            instance = new PokemonListingCache();
        }
        return instance.pokemonCache;
    }

    private final List<PokemonWithTypes> pokemonCache;

    private PokemonListingCache() throws IOException {
        this.pokemonCache = buildCache();
    }

    private List<PokemonWithTypes> buildCache() throws IOException {
        Scanner scanner = new Scanner(new File("pokemon-listing.txt"));
        Map<String, Pokemon> nameMapping = new HashMap<String, Pokemon>(151);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] args = line.split("\\t");
            Pokemon pokemon = new Pokemon(
                args[1], //Name
                Integer.parseInt(args[3]), //HP
                Integer.parseInt(args[4]), //ATTACK
                Integer.parseInt(args[5]), //DEFENSE
                Integer.parseInt(args[6]), //SPECIAL
                Integer.parseInt(args[7])  //SPEED
            );
            nameMapping.put(pokemon.getName(), pokemon);
        }
        scanner = new Scanner(new File("pokemon-type-listing.txt"));
        List<PokemonWithTypes> pokemonWithTypes = new ArrayList<PokemonWithTypes>(151);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] args = line.split("\\t");
            Pokemon pokemon = nameMapping.get(args[3]);
            Set<Type> types = new HashSet<Type>();
            types.add(TypeMapping.get(TypeEnum.valueOf(args[4].toUpperCase())));
            //this means there's a second type
            if (args.length == 6) {
                types.add(TypeMapping.get(TypeEnum.valueOf(args[5].toUpperCase())));
            }
            pokemonWithTypes.add(new PokemonWithTypes(pokemon, types));
        }
        return pokemonWithTypes;
    }
}
