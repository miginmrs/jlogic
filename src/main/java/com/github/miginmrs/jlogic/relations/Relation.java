package com.github.miginmrs.jlogic.relations;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.values.Value;

public interface Relation {
    Assertion toAssertion(Value[] vars, Assertion[] assertions);
    boolean unify(Value[] vars, Assertion[] assertions, Assertion assertion);
    String toString(String[] varNames, String[] assertionNames, int priority);    
}
