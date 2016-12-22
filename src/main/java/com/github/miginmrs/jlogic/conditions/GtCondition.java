package com.github.miginmrs.jlogic.conditions;

import com.github.miginmrs.jlogic.terms.Term;
import java.util.function.IntPredicate;

public class GtCondition extends CompareCondition {
    private static final IntPredicate gtPredicate = r->r>0;
    private static final String gtString = ">";
    public GtCondition(Term a, Term b) {
        super(a, b, gtPredicate, gtString);
    }
    
}
