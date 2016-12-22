package com.github.miginmrs.jlogic.terms;

import com.github.miginmrs.jlogic.values.IntegerValue;
import com.github.miginmrs.jlogic.values.Value;
import java.util.Objects;

public class IntegerVariable implements IntegerTerm{
    private final Variable<IntegerValue> var;

    public IntegerVariable(Variable<IntegerValue> var) {
        this.var = var;
    }

    @Override
    public IntegerValue getValue(Value[] vars) {
        return var.getValue(vars);
    }

    @Override
    public boolean setValue(Value[] vars, IntegerValue value) {
        return var.setValue(vars, value);
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    @Override
    public boolean isVariable(Value[] vars) {
        return var.isVariable(vars);
    }

    @Override
    public String toString(String[] varNames, int priority) {
        return var.toString(varNames, priority);
    }

    @Override
    public String toString() {
        return var.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.var);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            return var.equals(obj);
        }
        if(!(obj instanceof IntegerVariable))
            return false;
        return var.equals(((IntegerVariable)obj).var);
    }
    
}
