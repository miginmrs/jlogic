package com.github.miginmrs.jlogic.terms;

import com.github.miginmrs.jlogic.values.Value;

public class Variable<T extends Value> implements Term<T> {
    private final int i;

    public Variable(int i) {
        this.i = i;
    }

    @Override
    public T getValue(Value[] vars) {
        return (T)vars[i];
    }

    @Override
    public boolean setValue(Value[] vars, T value) {
        if (vars[i] != null) {
            return vars[i].equals(value);
        }
        vars[i] = value;
        return true;
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    @Override
    public boolean isVariable(Value[] vars) {
        return vars[i] == null;
    }

    @Override
    public String toString() {
        return new String(new byte[]{(byte) (120 + i)});
    }

    @Override
    public String toString(String[] varNames, int priority) {
        return varNames[i];
    }

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.i;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Variable)) {
            return false;
        }
        final Variable other = (Variable) obj;
        return this.i == other.i;
    }
    
}
