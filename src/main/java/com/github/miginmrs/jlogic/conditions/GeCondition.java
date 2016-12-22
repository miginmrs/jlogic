package com.github.miginmrs.jlogic.conditions;

import com.github.miginmrs.jlogic.terms.Term;
import java.util.function.IntPredicate;

public class GeCondition extends CompareCondition {
    private static final IntPredicate gePredicate = r->r>=0;
    private static final String geString = "≥";
    public GeCondition(Term a, Term b) {
        super(a, b, gePredicate, geString);
    }
    
}
