# pokemon-learning
Practicing reinforcement learning techniques using Pokemon battle rules.

##Goals
Our goal is to analyze Pokemon stats and movesets to form the ultimate team, then teach that team to win using reinforcement learning.

##Prerequisites
The data folder must contain the following files: `moves.json`, `pokemon-listing.txt`, and `pokemon-type-listing.txt`.
There also should be a `PokemonToAnalyze` file with a newline-separated list of Pokemon to generate data for.

##Building and Running
Use `./gradlew build` to build the app.
This compiles the code and runs all the tests.

####Data Generator 
To run the data generation, use the following command: `./gradlew runDataGenerator`. 
To run it with arguments, add `-Dexec.args="<arguments here>"` to the end of that command.
Note that it will look at the `data/PokemonToAnalyze` for which Pokemon to run the data on.
The output will be a folder named `output` with a `teams.json` file and a folder named `simulated-battles` containing an indexed collection of folders of all the team battles generated.
Each file will contain the collection of battles for a given team as T1.

######Arguments
EXAMPLE: `-Dexec.args="-setSize 2 -numberOfBattles 1- -skipExistingFalse false"`
* `-setSize` defaults to 3.
    * Size of sets of Pokemon to analyze.
    * For example, if the setSize is 2, we generate battles for all teams that contain at least 2 team members from the `PokemonToAnalyze` set.
* `-numberOfBattles`defaults to 5.
    * Number of battles to simulate per team having battles generated.
    * Note that the higher this is, the longer this takes to run.
* `-skipExistingFiles` defaults to true
    * True means that we do not generate battles if a file for the team already exists
    * Used if the process was killed prematurely upon initial generation of data and needs to be restarted.
    * False means that it reads in the battles generated and adds onto the existing list with more battles
    
####Pokemon Performance Analysis
To run the Pokemon Analysis, use the following command: `./gradlew runPokemonAnalysis 1`. 
Note that the number at the end represents the number of Pokemon to look at in combination.
For example, if it's 2, we look at each pair of Pokemon in the `PokemonToAnalyze` file and calculate the win percentages for teams containing that pair.
If it's 1, we look at win percentages for teams that contain one of the singular Pokemon in the file.
If it's 3, we look at the winning percentage of for just that team of 3.
All output goes to the console.

####Pokemon Move Analysis
To run the Pokemon Move Analysis, use the following command: `./gradlew runMoveAnalysis`.
This takes in no arguments. It creates a file in the `output` folder called `move-analysis.json` that contains a list of JSON objects with a mapping of Pokemon name to a mapping of moves and counts of times the move was used among all battles with that Pokemon.
Note that if you quit the process before it is finished, it writes to file everything it had currently calculated.

####Pokemon Next Move Analysis
To run the Pokemon Next Move Analysis, use the following command: `./gradlew runNextMoveAnalysis`. 
This looks at each Pokemon in the `PokemonToAnalyze` file and determines which move it should use against every possible defender and the confidence that it will lead to a team win.
The output goes to the `output` folder called `pokemon-next-moves.json`, which contains a list of objects with an attacker, defender, move, and confidence.
Like the move analysis, this writes to file everything it had calculated when quit prematurely.

##Notes
The initial `PokemonToAnalyze` file before generating data should contain the following pokemon for best results:

    Mewtwo
    Dragonite
    Mew
    Moltres
    Zapdos
    Articuno
    Cloyster
    Gyarados
    Arcanine
    Exeggutor
    Tauros
    Lapras
    Rhydon
    Aerodactyl
    Tentacruel
    Starmie
    Pinsir
    Vaporeon
    Jolteon
    Flareon
    Kabutops
    Snorlax
    Venusaur
    Charizard
    Blastoise
    Gengar
    Kingler
    Omastar
    Ninetales
    Machamp
    Victreebel
    Golem
    Rapidash
    Weezing
    Scyther
    Chansey
    Nidoqueen
    Nidoking
    Poliwrath
    Hypno
    Kangaskhan
    Sandslash
    Golduck
    Alakazam
    Dewgong
    Dodrio
    Muk
    Electrode
    Pidgeot
    Raichu
    
All data of Pokemon's moves and types came from pokemondb.net.
The tasks will take in the numberhood of hours to complete, so be careful with your inputs.
