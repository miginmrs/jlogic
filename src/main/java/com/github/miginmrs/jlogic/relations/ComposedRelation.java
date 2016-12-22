package com.github.miginmrs.jlogic.relations;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.assertions.ComposedAssertion;
import com.github.miginmrs.jlogic.values.Value;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ComposedRelation implements Relation {
    private final String name;
    private final List<Relation> args;

    public ComposedRelation(String name, List<Relation> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public boolean unify(Value[] vars, Assertion[] assertions, Assertion assertion) {
        if(!(assertion instanceof ComposedAssertion))
            return false;
        ComposedAssertion composedAssertion = (ComposedAssertion) assertion;
        if(!Objects.equals(composedAssertion.name, name))
            return false;
        if(args == null ? composedAssertion.args != null:args.size()!=composedAssertion.args.size())
            return false;
        Iterator<Relation> myit = args.iterator();
        Iterator<Assertion> otherit = composedAssertion.args.iterator();
        while(myit.hasNext())
            if(!myit.next().unify(vars, assertions, otherit.next()))
                return false;
        return true;
    }

    @Override
    public Assertion toAssertion(Value[] vars, Assertion[] assertions) {
        if(args == null)
            return new ComposedAssertion(name, null);
        List<Assertion> argList = new LinkedList<>();
        for(Relation arg : this.args)
            argList.add(arg.toAssertion(vars, assertions));
        return new ComposedAssertion(name, argList);
    }

    @Override
    public String toString(String[] varNames, String[] assertionNames, int priority) {
        if(args == null)
            return name;
        String string = name+"[";
        Iterator<Relation> it = args.iterator();
        if(it.hasNext())
            string+=it.next().toString(varNames, assertionNames, 0);
        while(it.hasNext())
            string+=","+it.next().toString(varNames, assertionNames, 0);
        return string + "]";
    }
    
}
