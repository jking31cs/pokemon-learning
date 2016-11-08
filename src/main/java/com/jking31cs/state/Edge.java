package com.jking31cs.state;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.jking31cs.Move;

/**
 * Represents actions made by each team as a to another state.
 */
public class Edge {
    private final String id;
    private final State prevState;
    private final State nextState;

    private final MoveAction p1Move;
    private final MoveAction p2Move;

    public Edge(
        String id,
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

    public String getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Edge edge = (Edge) o;

        if (id != null ? !id.equals(edge.id) : edge.id != null) {
            return false;
        }
        if (prevState != null ? !prevState.equals(edge.prevState) : edge.prevState != null) {
            return false;
        }
        if (nextState != null ? !nextState.equals(edge.nextState) : edge.nextState != null) {
            return false;
        }
        if (p1Move != null ? !p1Move.equals(edge.p1Move) : edge.p1Move != null) {
            return false;
        }
        return p2Move != null ? p2Move.equals(edge.p2Move) : edge.p2Move == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (prevState != null ? prevState.hashCode() : 0);
        result = 31 * result + (nextState != null ? nextState.hashCode() : 0);
        result = 31 * result + (p1Move != null ? p1Move.hashCode() : 0);
        result = 31 * result + (p2Move != null ? p2Move.hashCode() : 0);
        return result;
    }

    public MoveAction getP2Move() {
        return p2Move;
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

        public static MoveAction waitTurn() {
            return new MoveAction(Optional.absent(), Optional.absent());
        }

        public Optional<Move> getMove() {
            return move;
        }

        public Optional<PokemonStatus> getpSwitch() {
            return pSwitch;
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
