package com.github.miginmrs.jlogic.assertions;

import com.github.miginmrs.jlogic.values.Value;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class AtomicAssertion implements Assertion{
    public final String name;
    public final List<Value> args;

    public AtomicAssertion(String name, List<Value> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || ! (obj instanceof AtomicAssertion))
            return false;
        AtomicAssertion atomicFunction = (AtomicAssertion) obj;
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
        String string = name+"(";
        Iterator<Value> it = args.iterator();
        if(it.hasNext())
            string+=it.next().toString();
        while(it.hasNext())
            string+=","+it.next().toString();
        return string + ")";
    }

    @Override
    public String toString(int priority) {
        return toString();
    }
}
