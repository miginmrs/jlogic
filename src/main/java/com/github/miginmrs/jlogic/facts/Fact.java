package com.github.miginmrs.jlogic.facts;

import com.github.miginmrs.jlogic.rules.Rule;
import com.github.miginmrs.jlogic.assertions.Assertion;

public interface Fact {
    Assertion getAssertion();
    Rule getRule();
    Fact[] getChildren();    
}
