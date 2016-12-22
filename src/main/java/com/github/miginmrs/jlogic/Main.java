package com.github.miginmrs.jlogic;

import com.github.miginmrs.jlogic.process.Processor;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Wrong arguments number, expected 1");
            System.err.println("Usage: jlogic filename");
            System.exit(1);
            return;
        }
        new Processor(System.out).process(new Scanner(new File(args[0]), "UTF-8").useDelimiter("\\Z").next());
    }

}