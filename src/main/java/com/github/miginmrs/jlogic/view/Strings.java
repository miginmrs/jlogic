package com.github.miginmrs.jlogic.view;

public final class Strings {
    public static final String deduce, imply, not;
    private Strings(){}
    static {
        if("UTF-8".equals(System.getProperty("file.encoding"))) {
            deduce = "⊢";
            imply = "→";
            not = "¬";
        } else {
            deduce = "|-";
            imply = "->";
            not = "~";
        }
    }
}
