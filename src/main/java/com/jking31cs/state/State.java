package com.jking31cs.state;

import com.google.common.base.Objects;

/**
 * Represents a static moment in a battle.
 */
public class State {
    private final long id;
    private final Team t1;
    private final Team t2;
    private final TeamStatus t1Status;
    private final TeamStatus t2Status;
    private final PokemonStatus p1Status;
    private final PokemonStatus p2Status;
    private final boolean isEndState;
    private final boolean isInitState;

    public State(
        long id,
        Team t1,
        Team t2,
        TeamStatus t1Status,
        TeamStatus t2Status,
        PokemonStatus p1Status,
        PokemonStatus p2Status,
        boolean isEndState,
        boolean isInitState
    ) {
        this.id = id;
        this.t1 = t1;
        this.t2 = t2;
        this.t1Status = t1Status;
        this.t2Status = t2Status;
        this.p1Status = p1Status;
        this.p2Status = p2Status;
        this.isEndState = isEndState;
        this.isInitState = isInitState;
    }

    public long getId() {
        return id;
    }

    public Team getT1() {
        return t1;
    }

    public Team getT2() {
        return t2;
    }

    public TeamStatus getT1Status() {
        return t1Status;
    }

    public TeamStatus getT2Status() {
        return t2Status;
    }

    public PokemonStatus getP1Status() {
        return p1Status;
    }

    public PokemonStatus getP2Status() {
        return p2Status;
    }

    public boolean isEndState() {
        return isEndState;
    }

    public boolean isInitState() {
        return isInitState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return id == state.id &&
            isEndState == state.isEndState &&
            isInitState == state.isInitState &&
            Objects.equal(t1, state.t1) &&
            Objects.equal(t2, state.t2) &&
            Objects.equal(t1Status, state.t1Status) &&
            Objects.equal(t2Status, state.t2Status) &&
            Objects.equal(p1Status, state.p1Status) &&
            Objects.equal(p2Status, state.p2Status);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, t1, t2, t1Status, t2Status, p1Status, p2Status, isEndState, isInitState);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .add("t1", t1)
            .add("t2", t2)
            .add("t1Status", t1Status)
            .add("t2Status", t2Status)
            .add("p1Status", p1Status)
            .add("p2Status", p2Status)
            .add("isEndState", isEndState)
            .add("isInitState", isInitState)
            .toString();
    }
}
