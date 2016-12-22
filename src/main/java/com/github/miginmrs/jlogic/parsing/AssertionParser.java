package com.github.miginmrs.jlogic.parsing;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.assertions.AtomicAssertion;
import com.github.miginmrs.jlogic.assertions.ComposedAssertion;
import com.github.miginmrs.jlogic.assertions.ImplyAssertion;
import com.github.miginmrs.jlogic.assertions.NotAssertion;
import com.github.miginmrs.jlogic.values.Value;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssertionParser {
    private static final String IDEN_ID = "ident";
    private static final Pattern IDEN_PATTERN = Pattern.compile("\\s*(?<"+IDEN_ID+">¬*[0-9a-zA-Z_]*)\\s*");

    public Assertion apply(Parser t) {
        int point = t.mark();
        try {
            Assertion assertion = parseOne(t);
            Character c = t.getChar();
            if(c!=null)
                if(c=='→') {
                    t.seek(1);
                    assertion = new ImplyAssertion(assertion, apply(t));
                }
            return assertion;
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }
    private Assertion parseOne(Parser t) {
        int point = t.mark();
        try {
            Matcher matcher = t.parseWithPattern(IDEN_PATTERN);
            String name = matcher.group(IDEN_ID);
            if(name.startsWith("¬")){
                t.rewind(matcher.start(IDEN_ID)+1);
                return new NotAssertion(parseOne(t));
            }
            Character c = t.getChar();
            if(c!=null && c == '(') {
                ValueParser valueParser = new ValueParser();
                t.seek(1);
                List<Value> args = new LinkedList<>();
                boolean closed = false;
                c = t.getChar();
                while(true) {
                    if(c==')') {
                        t.seek(1);
                        closed = true;
                        break;
                    }
                    Value arg = valueParser.apply(t);
                    c = t.getChar();
                    if(c==',')
                        t.seek(1);
                    else if(c!=')')
                        throw new RuntimeException();
                    args.add(arg);
                }
                if(!closed)
                    throw new RuntimeException();
                return new AtomicAssertion(name, args);
            } else if(c!=null && c == '[') {
                t.seek(1);
                List<Assertion> args = new LinkedList<>();
                boolean closed = false;
                c = t.getChar();
                while(true) {
                    if(c==']') {
                        t.seek(1);
                        closed = true;
                        break;
                    }
                    Assertion arg = apply(t);
                    c = t.getChar();
                    if(c==',')
                        t.seek(1);
                    else if(c!=']')
                        throw new RuntimeException();
                    args.add(arg);
                }
                if(!closed)
                    throw new RuntimeException();
                if(name.isEmpty() && args.size()==1)
                    return args.get(0);
                return new ComposedAssertion(name, args);
            } else if(Character.isLowerCase(name.charAt(0))) 
                return new ComposedAssertion(name, null);
            else throw new Exception(name+" of predicates must start with lower case");
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }

}
