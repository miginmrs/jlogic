package com.github.miginmrs.jlogic.conditions;

import com.github.miginmrs.jlogic.terms.Term;
import java.util.function.IntPredicate;

public class LeCondition extends CompareCondition {
    private static final IntPredicate lePredicate = r->r<=0;
    private static final String leString = "≤";
    public LeCondition(Term a, Term b) {
        super(a, b, lePredicate, leString);
    }
    
}
