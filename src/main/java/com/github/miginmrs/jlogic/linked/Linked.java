package com.github.miginmrs.jlogic.linked;

public class Linked<E> extends Next<E> {
    public Next<E> queue = this;

    public void append(E item) {
        next = new Node<>(item, next);
        if (queue == this) {
            queue = next;
        }
    }
    
}
