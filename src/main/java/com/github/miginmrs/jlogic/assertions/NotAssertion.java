package com.github.miginmrs.jlogic.assertions;

import java.util.Objects;

public class NotAssertion implements Assertion{
    public final Assertion negated;

    public NotAssertion(Assertion negated) {
        this.negated = negated;
    }
    
    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public String toString(int priority) {
        return "¬"+negated.toString(1);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.negated);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        final NotAssertion notAssertion = (NotAssertion) obj;
        return Objects.equals(this.negated, notAssertion.negated);
    }

}
