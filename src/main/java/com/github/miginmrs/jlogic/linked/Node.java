package com.github.miginmrs.jlogic.linked;

public class Node<E> extends Next<E> {
    public final E item;

    public Node(E element, Node<E> next) {
        this.item = element;
        this.next = next;
    }
    
}
