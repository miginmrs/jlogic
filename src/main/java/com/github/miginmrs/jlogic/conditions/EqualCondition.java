package com.github.miginmrs.jlogic.conditions;

import com.github.miginmrs.jlogic.terms.Term;
import com.github.miginmrs.jlogic.values.Value;
import java.util.Objects;

public class EqualCondition implements Condition {
    private final Term a;
    private final Term b;

    public EqualCondition(Term a, Term b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean verify(Value[] vars) {
        if(a.isVariable(vars) && b.isVariable(vars))
            return Objects.equals(a.getValue(vars), b.getValue(vars));
        Term x = a.isVariable() ? this.a : this.b;
        Term y = a.isVariable() ? this.b : this.a;
        return x.setValue(vars, y.getValue(vars));
    }

    @Override
    public String toString() {
        return a + "=" + b;
    }

    @Override
    public String toString(String[] varNames, int priority) {
        return a.toString(varNames, 0) + "=" + b.toString(varNames, 0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.a);
        hash = 47 * hash + Objects.hashCode(this.b);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EqualCondition))
            return false;
        final EqualCondition other = (EqualCondition) obj;
        return Objects.equals(this.a, other.a) && Objects.equals(this.b, other.b);
    }
    
}
