package com.github.miginmrs.jlogic.relations;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.assertions.AtomicAssertion;
import com.github.miginmrs.jlogic.terms.Term;
import com.github.miginmrs.jlogic.values.Value;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AtomicRelation implements Relation {
    private final String name;
    private final List<Term> args;

    public AtomicRelation(String name, List<Term> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public boolean unify(Value[] vars, Assertion[] assertions, Assertion assertion) {
        if(!(assertion instanceof AtomicAssertion))
            return false;
        AtomicAssertion atomicAssertion = (AtomicAssertion) assertion;
        if(!Objects.equals(atomicAssertion.name, name))
            return false;
        if(args == null ? atomicAssertion.args != null:args.size()!=atomicAssertion.args.size())
            return false;
        Iterator<Term> myit = args.iterator();
        Iterator<Value> otherit = atomicAssertion.args.iterator();
        while(myit.hasNext())
            if(!myit.next().setValue(vars, otherit.next()))
                return false;
        return true;
    }

    @Override
    public Assertion toAssertion(Value[] vars, Assertion[] assertions) {
        List<Value> argList = new LinkedList<>();
        for(Term arg : this.args)
            argList.add(arg.getValue(vars));
        return new AtomicAssertion(name, argList);
    }

    @Override
    public String toString(String[] varNames, String[] assertionNames, int priority) {
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
