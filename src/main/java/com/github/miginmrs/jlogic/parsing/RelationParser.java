package com.github.miginmrs.jlogic.parsing;

import com.github.miginmrs.jlogic.relations.AtomicRelation;
import com.github.miginmrs.jlogic.relations.ComposedRelation;
import com.github.miginmrs.jlogic.relations.ImplyRelation;
import com.github.miginmrs.jlogic.relations.NotRelation;
import com.github.miginmrs.jlogic.relations.Relation;
import com.github.miginmrs.jlogic.relations.VariableRelation;
import com.github.miginmrs.jlogic.terms.Term;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelationParser {
    private static final String IDEN_ID = "ident";
    private static final Pattern IDEN_PATTERN = Pattern.compile("\\s*(?<"+IDEN_ID+">¬*[0-9a-zA-Z_]*)\\s*");

    public Relation apply(Parser t, List<String> varNames, List<String> assertionNames) {
        int point = t.mark();
        try {
            Relation relation = parseOne(t, varNames, assertionNames);
            Character c = t.getChar();
            if(c!=null)
                if(c=='→') {
                    t.seek(1);
                    relation = new ImplyRelation(relation, apply(t, varNames, assertionNames));
                }
            return relation;
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }
    private Relation parseOne(Parser t, List<String> varNames, List<String> assertionNames) {
        int point = t.mark();
        try {
            Matcher matcher = t.parseWithPattern(IDEN_PATTERN);
            String name = matcher.group(IDEN_ID);
            if(name.startsWith("¬")){
                t.rewind(matcher.start(IDEN_ID)+1);
                return new NotRelation(parseOne(t, varNames, assertionNames));
            }
            Character c = t.getChar();
            boolean empty = name.isEmpty();
            if(c!=null && !empty && c == '(') {
                TermParser termParser = new TermParser();
                t.seek(1);
                List<Term> args = new LinkedList<>();
                boolean closed = false;
                c = t.getChar();
                while(true) {
                    if(c==')') {
                        t.seek(1);
                        closed = true;
                        break;
                    }
                    Term arg = termParser.apply(t, varNames);
                    c = t.getChar();
                    if(c==',')
                        t.seek(1);
                    else if(c!=')')
                        throw new RuntimeException();
                    args.add(arg);
                }
                if(!closed)
                    throw new RuntimeException();
                return new AtomicRelation(name, args);
            } else if(c!=null && empty? c == '(' : c == '[') {
                t.seek(1);
                List<Relation> args = new LinkedList<>();
                boolean closed = false;
                c = t.getChar();
                while(true) {
                    if(empty? c==')':c==']') {
                        t.seek(1);
                        closed = true;
                        break;
                    }
                    Relation arg = apply(t, varNames, assertionNames);
                    c = t.getChar();
                    if(c==',')
                        t.seek(1);
                    else if(empty? c != ')' : c != ']')
                        throw new RuntimeException();
                    args.add(arg);
                }
                if(!closed)
                    throw new RuntimeException();
                if(empty && args.size()==1)
                    return args.get(0);
                return new ComposedRelation(name, args);
            } else if(Character.isLowerCase(name.charAt(0)))
                return new ComposedRelation(name, null);
            else {
                int index = assertionNames.indexOf(name);
                if(index==-1) {
                    index = assertionNames.size();
                    assertionNames.add(name);
                }
                return new VariableRelation(index);
            }
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }

}
