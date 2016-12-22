package com.github.miginmrs.jlogic.values;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class FunctionValue implements Value {
    private final String name;
    private final List<Value> args;

    public FunctionValue(String name, List<Value> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || ! (obj instanceof FunctionValue))
            return false;
        FunctionValue atomicFunction = (FunctionValue) obj;
        if(!Objects.equals(name, atomicFunction.name)) 
            return false;
        if(args == null)
            return atomicFunction.args == null;
        Iterator<Value> myit = args.iterator();
        Iterator<Value> otherit = atomicFunction.args.iterator();
        while(myit.hasNext()) {
            Value myarg = myit.next(), otherarg = otherit.next();
            if(myarg == null || !myarg.equals(otherarg))
                return false;
        }
        return true; 
    }

    public String getName() {
        return name;
    }

    public List<Value> getArgs() {
        return args;
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
            string+=it.next();
        while(it.hasNext())
            string+=","+it.next();
        return string + ")";
    }
}
