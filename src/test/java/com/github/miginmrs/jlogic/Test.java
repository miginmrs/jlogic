package com.github.miginmrs.jlogic;

import com.github.miginmrs.jlogic.process.Processor;

import java.util.Scanner;

public class Test {
    @org.junit.Test
    public void testProcessing() throws Exception {
        new Processor(System.out).process(new Scanner(ClassLoader.getSystemResourceAsStream("com/github/miginmrs/jlogic/demo"), "UTF-8").useDelimiter("\\Z").next());
    }
}
