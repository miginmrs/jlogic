package com.github.miginmrs.jlogic.conditions;

import com.github.miginmrs.jlogic.terms.Term;
import java.util.function.IntPredicate;

public class LtCondition extends CompareCondition {
    private static final IntPredicate ltPredicate = r->r<0;
    private static final String ltString = "<";
    public LtCondition(Term a, Term b) {
        super(a, b, ltPredicate, ltString);
    }
    
}
