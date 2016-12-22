package com.github.miginmrs.jlogic.relations;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.assertions.ImplyAssertion;
import com.github.miginmrs.jlogic.values.Value;
import com.github.miginmrs.jlogic.view.Strings;

public class ImplyRelation implements Relation{
    private final Relation a, b;

    public ImplyRelation(Relation a, Relation b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean unify(Value[] vars, Assertion[] assertions, Assertion assertion) {
        if(!(assertion instanceof ImplyAssertion))
            return false;
        ImplyAssertion implyAssertion = (ImplyAssertion) assertion;
        return a.unify(vars, assertions, implyAssertion.a) && b.unify(vars, assertions, implyAssertion.b);
    }

    @Override
    public Assertion toAssertion(Value[] vars, Assertion[] assertions) {
        return new ImplyAssertion(a.toAssertion(vars, assertions), b.toAssertion(vars, assertions));
    }

    @Override
    public String toString(String[] varNames, String[] assertionNames, int priority) {
        String string = a.toString(varNames, assertionNames, 1) + Strings.imply + b.toString(varNames, assertionNames, 0);
        if(priority == 0)
            return string;
        return "("+string+")";
    }
    
}
