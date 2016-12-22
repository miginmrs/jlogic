package com.github.miginmrs.jlogic.conditions;

import com.github.miginmrs.jlogic.terms.Term;
import com.github.miginmrs.jlogic.values.IntegerValue;
import com.github.miginmrs.jlogic.values.Value;
import java.util.Objects;
import java.util.function.IntPredicate;

abstract class CompareCondition implements Condition {
    private final Term<IntegerValue> a;
    private final Term<IntegerValue> b;
    private final IntPredicate p;
    private final String s;

    public CompareCondition(Term a, Term b, IntPredicate p, String symbole) {
        this.a = a;
        this.b = b;
        this.p = p;
        this.s = symbole;
    }

    @Override
    public boolean verify(Value[] vars) {
        return p.test(a.getValue(vars).asInt - b.getValue(vars).asInt);
    }

    @Override
    public String toString() {
        return a + s + b;
    }

    @Override
    public String toString(String[] varNames, int priority) {
        return a.toString(varNames, 0) + s + b.toString(varNames, 0);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.a);
        hash = 61 * hash + Objects.hashCode(this.b);
        hash = 61 * hash + Objects.hashCode(this.p);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CompareCondition))
            return false;
        final CompareCondition other = (CompareCondition) obj;
        return Objects.equals(this.a, other.a) && Objects.equals(this.b, other.b)&&Objects.equals(this.p, other.p);
    }
    
}
