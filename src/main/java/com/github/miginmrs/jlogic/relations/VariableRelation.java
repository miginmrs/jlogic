package com.github.miginmrs.jlogic.relations;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.values.Value;

public class VariableRelation implements Relation{
    private final int i;

    public VariableRelation(int i) {
        this.i = i;
    }    
    
    @Override
    public boolean unify(Value[] vars, Assertion[] assertions, Assertion assertion) {
        if(assertions[i]!=null)
            return assertions[i].equals(assertion);
        assertions[i] = assertion;
        return true;
    }

    @Override
    public Assertion toAssertion(Value[] vars, Assertion[] assertions) {
        return assertions[i];
    }

    @Override
    public String toString(String[] varNames, String[] assertionNames, int priority) {
        return assertionNames[i];
    }
    
}
