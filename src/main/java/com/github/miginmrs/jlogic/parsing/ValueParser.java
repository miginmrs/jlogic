package com.github.miginmrs.jlogic.parsing;

import com.github.miginmrs.jlogic.values.FunctionValue;
import com.github.miginmrs.jlogic.values.IntegerValue;
import com.github.miginmrs.jlogic.values.Value;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueParser implements Function<Parser, Value>{
    private static final String IDEN_ID = "ident";
    private static final Pattern IDEN_PATTERN = Pattern.compile("\\s*(?<"+IDEN_ID+">[0-9a-zA-Z_]*)\\s*");
    @Override
    public Value apply(Parser t) {
        int point = t.mark();
        try {
            Matcher matcher = t.parseWithPattern(IDEN_PATTERN);
            String name = matcher.group(IDEN_ID);
            if(t.getChar() == '(') {
                t.seek(1);
                List<Value> args = new LinkedList<>();
                boolean closed = false;
                while(true) {
                    char c = t.getChar();
                    if(c==')') {
                        t.seek(1);
                        closed = true;
                        break;
                    }
                    Value arg = apply(t);
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
                return new FunctionValue(name, args);
            } else if(Character.isUpperCase(name.charAt(0)))
                return new FunctionValue(name, null);
            else {
                int n = Integer.parseInt(name);
                return new IntegerValue(n);
            }
        } catch (Exception ex) {
            t.rewind(point);
            throw new RuntimeException(ex);                    
        }
    }

}
