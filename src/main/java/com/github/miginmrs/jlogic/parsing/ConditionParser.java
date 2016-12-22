package com.github.miginmrs.jlogic.parsing;

import com.github.miginmrs.jlogic.conditions.Condition;
import com.github.miginmrs.jlogic.conditions.DifferentCondition;
import com.github.miginmrs.jlogic.conditions.EqualCondition;
import com.github.miginmrs.jlogic.conditions.GeCondition;
import com.github.miginmrs.jlogic.conditions.GtCondition;
import com.github.miginmrs.jlogic.conditions.LeCondition;
import com.github.miginmrs.jlogic.conditions.LtCondition;
import com.github.miginmrs.jlogic.terms.Term;
import java.util.List;
import java.util.function.BiFunction;

public class ConditionParser implements BiFunction<Parser, List<String>, Condition>{
    @Override
    public Condition apply(Parser t, List<String> varNames) {
        int point = t.mark();
        TermParser termParser = new TermParser();
        try {
            Term term = termParser.apply(t, varNames);
            char c = t.getChar();
            t.seek(1);
            if(c=='≠') return new DifferentCondition(term, termParser.apply(t, varNames));
            else if(c=='=') return new EqualCondition(term, termParser.apply(t, varNames));
            else if(c=='<') return new LtCondition(term, termParser.apply(t, varNames));
            else if(c=='>') return new GtCondition(term, termParser.apply(t, varNames));
            else if(c=='≤') return new LeCondition(term, termParser.apply(t, varNames));
            else if(c=='≥') return new GeCondition(term, termParser.apply(t, varNames));
            else throw new RuntimeException();
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }
}
