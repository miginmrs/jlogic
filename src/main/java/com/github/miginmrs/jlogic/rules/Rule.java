package com.github.miginmrs.jlogic.rules;

import com.github.miginmrs.jlogic.facts.Fact;
import java.util.List;

public interface Rule {
    String getName();
    int getPremissesNumber();
    Fact deduce(List<Fact> facts);
    FactBuilder getRuleContext();
}
