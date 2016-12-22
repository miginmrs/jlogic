package com.github.miginmrs.jlogic.values;

public class IntegerValue implements Value{
    public final int asInt;

    public IntegerValue(int value) {
        this.asInt = value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.asInt;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IntegerValue)) {
            return false;
        }
        final IntegerValue integerValue = (IntegerValue) obj;
        return this.asInt == integerValue.asInt;
    }

    @Override
    public String toString() {
        return String.valueOf(asInt);
    }

}
