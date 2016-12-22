package com.github.miginmrs.jlogic.facts;

import com.github.miginmrs.jlogic.rules.Rule;
import com.github.miginmrs.jlogic.assertions.Assertion;
import java.util.Objects;

public class FactImpl implements Fact {
    public final Assertion assertion;
    public final Rule rule;
    private final Fact[] facts;

    public FactImpl(Assertion assertion, Rule rule, Fact[] facts) {
        this.assertion = assertion;
        this.rule = rule;
        this.facts = facts;
    }

    public Fact getFact(int i) {
        return facts[i];
    }

    public int getFactsNumber() {
        return facts.length;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public Fact[] getChildren() {
        return facts;
    }

    @Override
    public String toString() {
        String string = "";
        if(facts!=null)
            for(Fact fact:facts)
                string+=fact+"\n";
        return string+"\t"+assertion+" deduced by "+rule.getName();
    }

    @Override
    public Assertion getAssertion() {
        return assertion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.assertion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FactImpl)) {
            return false;
        }
        final FactImpl other = (FactImpl) obj;
        return Objects.equals(this.assertion, other.assertion);
    }
}
