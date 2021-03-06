package com.jking31cs;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Creates a listing of pokemon and stores it in memory.
 */
public class PokemonListingCache {

    private static PokemonListingCache instance;

    public static Map<String, PokemonWithTypes> getAll() throws IOException {
        if (instance == null) {
            instance = new PokemonListingCache();
        }
        return instance.pokemonCache;
    }

    private final Map<String, PokemonWithTypes> pokemonCache;

    private PokemonListingCache() throws IOException {
        this.pokemonCache = buildCache();
    }

    private Map<String, PokemonWithTypes> buildCache() throws IOException {
        Scanner scanner = new Scanner(new File("data/pokemon-listing.txt"));
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
        scanner = new Scanner(new File("data/pokemon-type-listing.txt"));
        Map<String, PokemonWithTypes> pokemonWithTypes = new HashMap<>(151);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] args = line.split("\\t");
            Pokemon pokemon = nameMapping.get(args[3]);
            Set<Type> types = new HashSet<Type>();
            Type t1 = TypeMapping.get(TypeEnum.valueOf(args[4].toUpperCase()));
            Type t2 = null;
            //this means there's a second type
            if (args.length == 6) {
                t2  =TypeMapping.get(TypeEnum.valueOf(args[5].toUpperCase()));
            }
            pokemonWithTypes.put(pokemon.getName(), new PokemonWithTypes(pokemon, t1, t2));
        }
        Set<String> namesICareAbout = Sets.newHashSet(
            "Mewtwo",
            "Dragonite",
            "Mew",
            "Moltres",
            "Zapdos",
            "Articuno",
            "Cloyster",
            "Gyarados",
            "Arcanine",
            "Exeggutor",
            "Tauros",
            "Lapras",
            "Rhydon",
            "Aerodactyl",
            "Tentacruel",
            "Starmie",
            "Pinsir",
            "Vaporeon",
            "Jolteon",
            "Flareon",
            "Kabutops",
            "Snorlax",
            "Venusaur",
            "Charizard",
            "Blastoise",
            "Gengar",
            "Kingler",
            "Omastar",
            "Ninetales",
            "Machamp",
            "Victreebel",
            "Golem",
            "Rapidash",
            "Weezing",
            "Scyther",
            "Chansey",
            "Nidoqueen",
            "Nidoking",
            "Poliwrath",
            "Hypno",
            "Kangaskhan",
            "Sandslash",
            "Golduck",
            "Alakazam",
            "Dewgong",
            "Dodrio",
            "Muk",
            "Electrode",
            "Pidgeot",
            "Raichu"
        );
        return Maps.asMap(namesICareAbout, pokemonWithTypes::get);
    }
}
