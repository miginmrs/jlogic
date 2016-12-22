package com.github.miginmrs.jlogic.rules;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.conditions.Condition;
import com.github.miginmrs.jlogic.relations.Relation;
import com.github.miginmrs.jlogic.facts.Fact;
import com.github.miginmrs.jlogic.facts.FactImpl;
import com.github.miginmrs.jlogic.values.Value;
import com.github.miginmrs.jlogic.view.Strings;

import java.util.Iterator;
import java.util.List;

public class ZPRule implements Rule {
    private String name;
    private final Relation[] premises;
    private final Relation conclusion;
    private final Condition[] conditions;
    private final int nVars, nAssertions;
    private final String[] varNames, assertionNames;

    @Override
    public FactBuilder getRuleContext() {
        return new ZPFactBuilder();
    }
    
    class ZPFactBuilder implements FactBuilder {
        private final Value[] vars;
        private final Assertion[] assertions;
        private final Fact[] facts;

        public ZPFactBuilder() {
            vars = new Value[nVars];
            assertions = new Assertion[nAssertions];
            facts = new Fact[premises.length];
        }

        public ZPFactBuilder(Value[] vars, Assertion[] assertions, Fact[] facts) {
            this.vars = vars;
            this.assertions = assertions;
            this.facts = facts;
        }

        
        @Override
        public ZPFactBuilder setPremiss(Fact fact, int index) {
            try {
                if (facts[index] != null)
                    return facts[index].equals(fact)? this: null;
                Assertion[] copyAssertions = assertions.clone();
                Value[] copyVars = vars.clone();
                if(!premises[index].unify(copyVars, copyAssertions, fact.getAssertion()))
                    return null;
                Fact[] copyFacts = facts.clone();
                copyFacts[index] = fact;
                return new ZPFactBuilder(copyVars, copyAssertions, copyFacts);
            }catch (Exception e) {
                return null;
            }
        }

        @Override
        public Fact getResult() {
            for (Condition cond : conditions)
                if (!cond.verify(vars))
                    return null;
            return new FactImpl(conclusion.toAssertion(vars, assertions), ZPRule.this, facts);
        }        
    }

    public ZPRule(Relation[] premises, Condition[] conditions, Relation conclusion, String[] varNames, String[] assertionNames) {
        this.premises = premises;
        this.conditions = conditions;
        this.conclusion = conclusion;
        this.nVars = varNames.length;
        this.nAssertions = assertionNames.length;
        this.varNames = varNames;
        this.assertionNames = assertionNames;
    }

    @Override
    public Fact deduce(List<Fact> facts) {
        if (facts.size() < premises.length) {
            return null;
        }
        Value[] vars = new Value[nVars];
        Assertion[] assertions = new Assertion[nAssertions];
        Iterator<Fact> it = facts.iterator();
        for (Relation premise : premises) {
            try {
                if (!premise.unify(vars, assertions, it.next().getAssertion()))
                    return null;
            }catch (Exception e) {
                return null;
            }
        }
        for (Condition cond : conditions)
            if (!cond.verify(vars))
                return null;
        return new FactImpl(conclusion.toAssertion(vars, assertions), this, facts.subList(0, premises.length).toArray(new Fact[0]));
    }

    @Override
    public String toString() {
        String s;
        if (0 < premises.length) {
            s = premises[0].toString(varNames, assertionNames, 0);
            for (int i = 1; i < premises.length; i++) {
                s += ", " + premises[i].toString(varNames, assertionNames, 0);
            }
            if (0 < conditions.length)
                s += ", " + conditions[0].toString(varNames, 0);
        } else if (0 < conditions.length) {
            s = conditions[0].toString(varNames, 0);
        } else {
            return conclusion.toString(varNames, assertionNames, 0);
        }
        for (int i = 1; i < conditions.length; i++) {
            s += ", " + conditions[i].toString(varNames, 0);
        }
        return s + " " + Strings.deduce + " " + conclusion.toString(varNames, assertionNames, 0);
    }

    @Override
    public int getPremissesNumber() {
        return premises.length;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
