package com.github.miginmrs.jlogic.terms;

import com.github.miginmrs.jlogic.values.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class FunctionTerm implements Term {
    private final String name;
    private final List<Term> args;

    public FunctionTerm(String name, List<Term> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || ! (obj instanceof FunctionTerm))
            return false;
        FunctionTerm atomicFunction = (FunctionTerm) obj;
        return (name == null? atomicFunction.name == null : name.equals(atomicFunction.name)) 
            && (args == null? atomicFunction.args == null : args.equals(atomicFunction.args));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public String toString() {
        if(args == null)
            return name;
        String string = name+"(";
        Iterator<Term> it = args.iterator();
        if(it.hasNext())
            string+=it.next();
        while(it.hasNext())
            string+=","+it.next();
        return string + ")";
    }

    @Override
    public Value getValue(Value[] vars) {
        if(args == null)
            return new FunctionValue(name, null);
        return new FunctionValue(name, args.stream().map(e->e.getValue(vars)).collect(Collectors.toList()));
    }

    @Override
    public boolean setValue(Value[] vars, Value value) {
        if(value == null || !(value instanceof FunctionValue))
            return false;
        FunctionValue functionValue = (FunctionValue) value;
        if(!Objects.equals(name, functionValue.getName()))
            return false;
        if(args == null)
            return functionValue.getArgs() == null;
        if(functionValue.getArgs()== null || args.size()!=functionValue.getArgs().size())
            return false;
        Iterator<Term> myit = args.iterator();
        Iterator<Value> otherit = functionValue.getArgs().iterator();
        while(myit.hasNext())
            if(!myit.next().setValue(vars, otherit.next()))
                return false;
        return true;
    }

    @Override
    public boolean isVariable() {
        for(Term arg : args)
            if(arg.isVariable())
                return true;
        return false;
    }

    @Override
    public boolean isVariable(Value[] vars) {
        for(Term arg : args)
            if(arg.isVariable(vars))
                return true;
        return false;
    }

    @Override
    public String toString(String[] varNames, int priority) {
        if(args == null)
            return name;
        String string = name+"(";
        Iterator<Term> it = args.iterator();
        if(it.hasNext())
            string+=it.next().toString(varNames, 0);
        while(it.hasNext())
            string+=","+it.next().toString(varNames, 0);
        return string + ")";
    }
}
