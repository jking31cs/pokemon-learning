package com.jking31cs.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jking31 on 11/1/16.
 */
public class BattleTree {

    public String id;

    public Map<String, State> states = new HashMap<>();
    public Map<String, Edge> edges = new HashMap<>();

    //Id of the initial state.
    public String initialStateId;

    //Given a stateId, find all outgoing edges from that state.
    public Map<String, Set<String>> stateToEdgeMap = new HashMap<>();

    public void addState(State state) {
        states.put(state.getId(), state);
        stateToEdgeMap.put(state.getId(), new HashSet<>());
    }

    public void addEdge(Edge edge) {
        edges.put(edge.getId(), edge);
        stateToEdgeMap.get(edge.getPrevState().getId()).add(edge.getId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BattleTree{");
        sb.append("id='").append(id).append('\'');
        sb.append(", states=").append(states);
        sb.append(", edges=").append(edges);
        sb.append(", initialStateId='").append(initialStateId).append('\'');
        sb.append(", stateToEdgeMap=").append(stateToEdgeMap);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BattleTree that = (BattleTree) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (states != null ? !states.equals(that.states) : that.states != null) {
            return false;
        }
        if (edges != null ? !edges.equals(that.edges) : that.edges != null) {
            return false;
        }
        if (initialStateId != null ? !initialStateId.equals(that.initialStateId) : that.initialStateId != null) {
            return false;
        }
        return stateToEdgeMap != null ? stateToEdgeMap.equals(that.stateToEdgeMap) : that.stateToEdgeMap == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (states != null ? states.hashCode() : 0);
        result = 31 * result + (edges != null ? edges.hashCode() : 0);
        result = 31 * result + (initialStateId != null ? initialStateId.hashCode() : 0);
        result = 31 * result + (stateToEdgeMap != null ? stateToEdgeMap.hashCode() : 0);
        return result;
    }
}
