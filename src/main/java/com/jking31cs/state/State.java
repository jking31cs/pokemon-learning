package com.jking31cs.state;

import com.google.common.base.Objects;

/**
 * Represents a static moment in a battle.
 */
public class State {
    private final String id;
    private final String t1Id;
    private final String t2Id;
    private final TeamStatus t1Status;
    private final TeamStatus t2Status;
    private final PokemonStatus p1Status;
    private final PokemonStatus p2Status;
    private final boolean isEndState;
    private final boolean isInitState;

    public State(
        String id,
        Team t1Id,
        Team t2Id,
        TeamStatus t1Status,
        TeamStatus t2Status,
        PokemonStatus p1Status,
        PokemonStatus p2Status,
        boolean isEndState,
        boolean isInitState
    ) {
        this.id = id;
        this.t1Id = t1Id.getId();
        this.t2Id = t2Id.getId();
        this.t1Status = t1Status;
        this.t2Status = t2Status;
        this.p1Status = p1Status;
        this.p2Status = p2Status;
        this.isEndState = isEndState;
        this.isInitState = isInitState;
    }

    public String getId() {
        return id;
    }

    public String getT1Id() {
        return t1Id;
    }

    public String getT2Id() {
        return t2Id;
    }

    public TeamStatus getT1Status() {
        return t1Status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        State state = (State) o;

        if (isEndState != state.isEndState) {
            return false;
        }
        if (isInitState != state.isInitState) {
            return false;
        }
        if (id != null ? !id.equals(state.id) : state.id != null) {
            return false;
        }
        if (t1Id != null ? !t1Id.equals(state.t1Id) : state.t1Id != null) {
            return false;
        }
        if (t2Id != null ? !t2Id.equals(state.t2Id) : state.t2Id != null) {
            return false;
        }
        if (t1Status != null ? !t1Status.equals(state.t1Status) : state.t1Status != null) {
            return false;
        }
        if (t2Status != null ? !t2Status.equals(state.t2Status) : state.t2Status != null) {
            return false;
        }
        if (p1Status != null ? !p1Status.equals(state.p1Status) : state.p1Status != null) {
            return false;
        }
        return p2Status != null ? p2Status.equals(state.p2Status) : state.p2Status == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (t1Id != null ? t1Id.hashCode() : 0);
        result = 31 * result + (t2Id != null ? t2Id.hashCode() : 0);
        result = 31 * result + (t1Status != null ? t1Status.hashCode() : 0);
        result = 31 * result + (t2Status != null ? t2Status.hashCode() : 0);
        result = 31 * result + (p1Status != null ? p1Status.hashCode() : 0);
        result = 31 * result + (p2Status != null ? p2Status.hashCode() : 0);
        result = 31 * result + (isEndState ? 1 : 0);
        result = 31 * result + (isInitState ? 1 : 0);
        return result;
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
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .add("t1Id", t1Id)
            .add("t2Id", t2Id)
            .add("t1Status", t1Status)
            .add("t2Status", t2Status)
            .add("p1Status", p1Status)
            .add("p2Status", p2Status)
            .add("isEndState", isEndState)
            .add("isInitState", isInitState)
            .toString();
    }
}
