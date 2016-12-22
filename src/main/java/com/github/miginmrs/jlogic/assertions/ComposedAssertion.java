package com.github.miginmrs.jlogic.assertions;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class ComposedAssertion implements Assertion{
    public final String name;
    public final List<Assertion> args;

    public ComposedAssertion(String name, List<Assertion> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || ! (obj instanceof ComposedAssertion))
            return false;
        ComposedAssertion atomicFunction = (ComposedAssertion) obj;
        return Objects.equals(name,atomicFunction.name) 
            && Objects.equals(args,atomicFunction.args);
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
        String string = name+"[";
        Iterator<Assertion> it = args.iterator();
        if(it.hasNext())
            string+=it.next().toString();
        while(it.hasNext())
            string+=","+it.next().toString();
        return string + "]";
    }

    @Override
    public String toString(int priority) {
        return toString();
    }
}
