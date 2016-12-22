package com.github.miginmrs.jlogic.terms;

import com.github.miginmrs.jlogic.values.IntegerValue;
import com.github.miginmrs.jlogic.values.Value;
import java.util.Objects;

public class ConstInteger implements IntegerTerm {
    private final IntegerValue a;

    public ConstInteger(int a) {
        this.a = new IntegerValue(a);
    }

    @Override
    public IntegerValue getValue(Value[] vars) {
        return a;
    }

    @Override
    public boolean setValue(Value[] vars, IntegerValue value) {
        return Objects.equals(value, a);
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    @Override
    public boolean isVariable(Value[] vars) {
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.a);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ConstInteger)) {
            return false;
        }
        final ConstInteger other = (ConstInteger) obj;
        return Objects.equals(this.a, other.a);
    }

    @Override
    public String toString() {
        return a.toString();
    }

    @Override
    public String toString(String[] varNames, int priority) {
        return a.toString();
    }
    
}
