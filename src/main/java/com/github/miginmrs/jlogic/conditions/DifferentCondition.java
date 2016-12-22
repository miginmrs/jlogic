package com.github.miginmrs.jlogic.conditions;

import com.github.miginmrs.jlogic.terms.Term;
import com.github.miginmrs.jlogic.values.Value;
import java.util.Objects;

public class DifferentCondition implements Condition {
    private final Term a;
    private final Term b;

    public DifferentCondition(Term a, Term b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean verify(Value[] vars) {
        if(a.isVariable(vars) || b.isVariable(vars))
            return false;
        return !Objects.equals(a.getValue(vars), b.getValue(vars));
    }

    @Override
    public String toString() {
        return a + "≠" + b;
    }

    @Override
    public String toString(String[] varNames, int priority) {
        return a.toString(varNames, 0) + "≠" + b.toString(varNames, 0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.a);
        hash = 71 * hash + Objects.hashCode(this.b);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DifferentCondition)) {
            return false;
        }
        final DifferentCondition other = (DifferentCondition) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return true;
    }
    
}
