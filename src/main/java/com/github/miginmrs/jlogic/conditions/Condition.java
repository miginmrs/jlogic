package com.github.miginmrs.jlogic.conditions;

import com.github.miginmrs.jlogic.values.Value;

public interface Condition {
    boolean verify(Value[] vars);
    String toString(String[] varNames, int priority);    
}
