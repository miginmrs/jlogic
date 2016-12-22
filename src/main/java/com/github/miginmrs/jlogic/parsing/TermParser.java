package com.github.miginmrs.jlogic.parsing;

import com.github.miginmrs.jlogic.terms.ConstInteger;
import com.github.miginmrs.jlogic.terms.FunctionTerm;
import com.github.miginmrs.jlogic.terms.IntegerTerm;
import com.github.miginmrs.jlogic.terms.IntegerVariable;
import com.github.miginmrs.jlogic.terms.Sum;
import com.github.miginmrs.jlogic.terms.Term;
import com.github.miginmrs.jlogic.terms.Variable;
import com.github.miginmrs.jlogic.values.IntegerValue;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TermParser implements BiFunction<Parser, List<String>, Term>{
    private static final String IDEN_ID = "ident";
    private static final Pattern IDEN_PATTERN = Pattern.compile("\\s*(?<"+IDEN_ID+">[0-9a-zA-Z_]*)\\s*");
    @Override
    public Term apply(Parser t, List<String> varNames) {
        int point = t.mark();
        try {
            Term term = parseOne(t, varNames);
            Character c = t.getChar();
            if(c!=null) 
                if(c=='+' || c=='-'){
                    t.seek(1);
                    Term aTerm = apply(t, varNames);
                    if(term instanceof Variable)
                        term = new IntegerVariable((Variable<IntegerValue>) term);
                    else if(!(term instanceof IntegerTerm))
                        throw new Exception(term+" is not evaluable to Integer");
                    if(aTerm instanceof Variable)
                        aTerm = new IntegerVariable((Variable<IntegerValue>) aTerm);
                    else if(!(aTerm instanceof IntegerTerm))
                        throw new Exception(aTerm+" is not evaluable to Integer");
                    if(c=='+') term = new Sum((IntegerTerm)term, (IntegerTerm)aTerm);
                    else if(c=='-') term = new Sum((IntegerTerm)term, (IntegerTerm)aTerm);
                }
            return term;
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }
    private Term parseOne(Parser t, List<String> varNames) {
        int point = t.mark();
        try {
            Matcher matcher = t.parseWithPattern(IDEN_PATTERN);
            String name = matcher.group(IDEN_ID);
            if(t.getChar() == '(') {
                t.seek(1);
                List<Term> args = new LinkedList<>();
                boolean closed = false;
                while(true) {
                    char c = t.getChar();
                    if(c==')') {
                        t.seek(1);
                        closed = true;
                        break;
                    }
                    Term arg = apply(t, varNames);
                    c = t.getChar();
                    if(c==',')
                        t.seek(1);
                    else if(c!=')')
                        throw new RuntimeException();
                    args.add(arg);
                }
                if(!closed)
                    throw new RuntimeException();
                if(name.isEmpty() && args.size()==1)
                    return args.get(0);
                return new FunctionTerm(name, args);
            } else if(Character.isUpperCase(name.charAt(0)))
                return new FunctionTerm(name, null);
            else {
                try {
                    int n = Integer.parseInt(name);
                    return new ConstInteger(n);
                } catch (Exception ex) {
                    int index = varNames.indexOf(name);
                    if(index==-1) {
                        index = varNames.size();
                        varNames.add(name);
                    }
                    return new Variable(index);
                }
            }
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }

}
