package com.github.miginmrs.jlogic.relations;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.assertions.NotAssertion;
import com.github.miginmrs.jlogic.values.Value;
import com.github.miginmrs.jlogic.view.Strings;

public class NotRelation implements Relation{
    private final Relation negated;

    public NotRelation(Relation negated) {
        this.negated = negated;
    }
    
    @Override
    public boolean unify(Value[] vars, Assertion[] assertions, Assertion assertion) {
        if(!(assertion instanceof NotAssertion))
            return false;
        NotAssertion notAssertion = (NotAssertion) assertion;
        return negated.unify(vars, assertions, notAssertion.negated);
    }

    @Override
    public Assertion toAssertion(Value[] vars, Assertion[] assertions) {
        return new NotAssertion(negated.toAssertion(vars, assertions));
    }

    @Override
    public String toString(String[] varNames, String[] assertionNames, int priority) {
        return Strings.not+negated.toString(varNames, assertionNames, 1);
    }

}
