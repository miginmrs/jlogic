package com.github.miginmrs.jlogic.parsing;

import com.github.miginmrs.jlogic.conditions.Condition;
import com.github.miginmrs.jlogic.relations.Relation;
import com.github.miginmrs.jlogic.rules.Rule;
import com.github.miginmrs.jlogic.rules.ZPRule;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class RuleParser implements Function<Parser, Rule>{
    @Override
    public Rule apply(Parser t) {
        int point = t.mark();
        RelationParser relationParser = new RelationParser();
        ConditionParser conditionParser = new ConditionParser();
        List<String> varNames = new LinkedList<>();
        List<String> assertionNames = new LinkedList<>();
        List<Relation> premisses = new LinkedList<>();
        List<Condition> conditions = new LinkedList<>();
        try {
            char c;
            while(Character.isSpaceChar(c = t.getChar()))
                t.seek(1);
            if(c=='⊢')
                t.seek(1);
            while(c!='⊢') {
                int permpoint = t.mark();
                try {
                    Relation relation = relationParser.apply(t, varNames, assertionNames);
                    while(Character.isSpaceChar(c = t.getChar()))
                        t.seek(1);
                    if(c != ',' && c!='⊢')
                        throw new RuntimeException();
                    premisses.add(relation);
                    t.seek(1);
                } catch(Exception e) {
                    t.rewind(permpoint);
                    conditions.add(conditionParser.apply(t, varNames));
                    while(Character.isSpaceChar(c = t.getChar()))
                        t.seek(1);
                    if(c != ',' && c!='⊢')
                        throw new RuntimeException();
                    t.seek(1);
                }
            }
            return new ZPRule(
                premisses.toArray(new Relation[0]), 
                conditions.toArray(new Condition[0]),
                relationParser.apply(t, varNames, assertionNames),
                varNames.toArray(new String[0]), 
                assertionNames.toArray(new String[0])
            );
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }
}
