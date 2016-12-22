package com.github.miginmrs.jlogic.assertions;

import com.github.miginmrs.jlogic.view.Strings;

import java.util.Objects;

public class ImplyAssertion implements Assertion{
    public final Assertion a, b;

    public ImplyAssertion(Assertion a, Assertion b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public String toString(int priority) {
        String string = a.toString(1) + Strings.imply + b.toString(0);
        if(priority == 0)
            return string;
        return "("+string+")";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.a);
        hash = 79 * hash + Objects.hashCode(this.b);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ImplyAssertion implyAssertion = (ImplyAssertion) obj;
        return Objects.equals(this.a, implyAssertion.a) && Objects.equals(this.b, implyAssertion.b);
    }
    
    
}
