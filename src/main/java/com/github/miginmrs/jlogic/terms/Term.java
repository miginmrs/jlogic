package com.github.miginmrs.jlogic.terms;

import com.github.miginmrs.jlogic.values.Value;

public interface Term<T extends Value> {
    T getValue(Value[] vars);
    boolean setValue(Value[] vars, T value);
    boolean isVariable();
    boolean isVariable(Value[] vars);
    String toString(String[] varNames, int priority);    
}
