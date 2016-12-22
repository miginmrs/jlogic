package com.github.miginmrs.jlogic.terms;

import com.github.miginmrs.jlogic.values.Value;
import com.github.miginmrs.jlogic.values.IntegerValue;
import java.util.Objects;

public class Dif implements IntegerTerm {
    private final IntegerTerm a;
    private final IntegerTerm b;

    public Dif(IntegerTerm a, IntegerTerm b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public IntegerValue getValue(Value[] vars) {
        return new IntegerValue(a.getValue(vars).asInt - b.getValue(vars).asInt);
    }

    @Override
    public boolean setValue(Value[] vars, IntegerValue value) {
        if (a.isVariable()) {
            if (b.isVariable()) {
                return false;
            } else {
                return a.setValue(vars, new IntegerValue(value.asInt + b.getValue(vars).asInt));
            }
        }
        if (b.isVariable()) {
            return b.setValue(vars, new IntegerValue(a.getValue(vars).asInt - value.asInt));
        }
        return false;
    }

    @Override
    public boolean isVariable() {
        return a.isVariable() || b.isVariable();
    }

    @Override
    public boolean isVariable(Value[] vars) {
        return a.isVariable(vars) || b.isVariable(vars);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.a);
        hash = 53 * hash + Objects.hashCode(this.b);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Dif)) {
            return false;
        }
        final Dif other = (Dif) obj;
        return Objects.equals(this.b, other.b) && Objects.equals(this.a, other.a);
    }
    

    @Override
    public String toString() {
        return a + "-" + b;
    }

    @Override
    public String toString(String[] varNames, int priority) {
        String string = a.toString(varNames, 1) + "-" + b.toString(varNames, 2);
        if(1 < priority)
            string = "("+string+")";
        return string;
    }
    
}
