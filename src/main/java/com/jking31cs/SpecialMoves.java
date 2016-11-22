package com.jking31cs;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Moves with weird situations.
 */
public class SpecialMoves {
    //Moves that charge the first move and hit the second move.
    public static final Set<String> secondTurnMoves = Sets.newHashSet(
            "Dig",
            "Fly",
            "Sky Attack",
            "Solar Beam",
            "Skull Bash"
    );

    //Moves that deal damage one turn, then recharge the second.
    public static final Set<String> firstTurnMoves = Sets.newHashSet(
            "Hyper Beam"
    );

    //Moves that repeat 2 times.
    public static final Set<String> repeatMoves = Sets.newHashSet(
            "Petal Dance",
            "Thrash"
    );

    //These moves have way too specific of rules to consider.
    public static final Set<String> ignoreMoves = Sets.newHashSet(
            "Dream Eater",
        "Explosion",
        "Self-Destruct"
    );
}
