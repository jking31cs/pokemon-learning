package com.jking31cs.state;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.jking31cs.Move;

/**
 * Represents actions made by each team as a to another state.
 */
public class Edge {
    private final long id;
    private final State prevState;
    private final State nextState;

    private final MoveAction p1Move;
    private final MoveAction p2Move;

    public Edge(
        long id,
        State prevState,
        State nextState,
        MoveAction p1Move,
        MoveAction p2Move
    ) {
        this.id = id;
        this.prevState = prevState;
        this.nextState = nextState;
        this.p1Move = p1Move;
        this.p2Move = p2Move;
    }

    public long getId() {
        return id;
    }

    public State getPrevState() {
        return prevState;
    }

    public State getNextState() {
        return nextState;
    }

    public MoveAction getP1Move() {
        return p1Move;
    }

    public MoveAction getP2Move() {
        return p2Move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return id == edge.id &&
            Objects.equal(prevState, edge.prevState) &&
            Objects.equal(nextState, edge.nextState) &&
            Objects.equal(p1Move, edge.p1Move) &&
            Objects.equal(p2Move, edge.p2Move);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, prevState, nextState, p1Move, p2Move);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .add("prevState", prevState)
            .add("nextState", nextState)
            .add("p1Move", p1Move)
            .add("p2Move", p2Move)
            .toString();
    }

    public static class MoveAction {
        private final Optional<Move> move;
        private final Optional<PokemonStatus> pSwitch;

        private MoveAction(
            Optional<Move> move,
            Optional<PokemonStatus> pSwitch
        ) {
            this.move = move;
            this.pSwitch = pSwitch;
        }

        public static MoveAction switchTo(PokemonStatus pokemonStatus) {
            return new MoveAction(Optional.<Move>absent(), Optional.of(pokemonStatus));
        }

        public static MoveAction move(Move move) {
            return new MoveAction(Optional.of(move), Optional.<PokemonStatus>absent());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MoveAction that = (MoveAction) o;
            return Objects.equal(move, that.move) &&
                Objects.equal(pSwitch, that.pSwitch);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(move, pSwitch);
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                .add("move", move)
                .add("pSwitch", pSwitch)
                .toString();
        }
    }
}
