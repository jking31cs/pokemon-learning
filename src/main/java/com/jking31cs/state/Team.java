package com.jking31cs.state;

import com.google.common.base.Objects;

/**
 * Represents a team.  We only need the name to look up their stats  from our cache.
 */
public class Team {
    private final String id;
    private final String p1;
    private final String p2;
    private final String p3;

    public Team(String id, String p1, String p2, String p3) {
        this.id = id;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public String getId() {
        return id;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getP3() {
        return p3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return java.util.Objects.equals(id, team.id) &&
            Objects.equal(p1, team.p1) &&
            Objects.equal(p2, team.p2) &&
            Objects.equal(p3, team.p3);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, p1, p2, p3);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .add("p1", p1)
            .add("p2", p2)
            .add("p3", p3)
            .toString();
    }
}
