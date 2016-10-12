package com.jking31cs;

import java.util.Set;

/**
 * A type that includes a listing of weaknesses, nullifications, and resistances.
 */
public class Type {
    private final TypeEnum name;
    private final Set<TypeEnum> weaknesses;
    private final Set<TypeEnum> nullifications;
    private final Set<TypeEnum> resistances;


    public Type(
        TypeEnum name,
        Set<TypeEnum> weaknesses,
        Set<TypeEnum> nullifications,
        Set<TypeEnum> resistances
    ) {
        this.name = name;
        this.weaknesses = weaknesses;
        this.nullifications = nullifications;
        this.resistances = resistances;
    }

    public TypeEnum getName() {
        return name;
    }

    public Set<TypeEnum> getWeaknesses() {
        return weaknesses;
    }

    public Set<TypeEnum> getNullifications() {
        return nullifications;
    }

    public Set<TypeEnum> getResistances() {
        return resistances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        return name == type.name;

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Type{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
