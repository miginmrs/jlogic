package com.github.miginmrs.jlogic.rules;

import com.github.miginmrs.jlogic.facts.Fact;

public interface FactBuilder {
    FactBuilder setPremiss(Fact fact, int index);
    Fact getResult();
}
