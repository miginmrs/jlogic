package com.github.miginmrs.jlogic.parsing;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final Pattern COMMENT_PATTERN = Pattern.compile("#[^\n]*\n");
    private final String sourceDataString;
    private final String dataString;
    private final TreeMap<Integer, Integer> indexesMap;
    private int next = 0;

    public Parser(String dataString) {
        this.sourceDataString = dataString;
        indexesMap = new TreeMap<>();
        StringBuffer sb = new StringBuffer();
        Matcher m = COMMENT_PATTERN.matcher(dataString);
        int index = 0;
        int sourceIndex = 0;
        indexesMap.put(0, 0);
        while (m.find()) {
            m.appendReplacement(sb, "\n");
            index += m.start() + 1 - sourceIndex;
            sourceIndex = m.end();
            indexesMap.put(index, sourceIndex);
        }
        m.appendTail(sb);
        this.dataString = sb.toString();
    }

    public Matcher parseWithPattern(Pattern pattern) throws Exception {
        Matcher matcher = pattern.matcher(dataString);
        if (!matcher.find(next) || matcher.start() != next) {
            int originNext = indexesMap.floorKey(next);
            int sourceNext = indexesMap.get(originNext) + (next - originNext);
            throw new Exception("Syntax Error on position "+next+" near : "+sourceDataString.substring(sourceNext).trim().split("\n", 2)[0]);
        }
        next = matcher.end();
        return matcher;
    }
    
    public void seek(int distance) {
        next += distance;
    }
    
    public Character getChar() {
        if(next < dataString.length())
            return dataString.charAt(next);
        return null;
    }

    public void rewind(int point) {
        next = point;
    }

    public int mark() {
        return next;
    }
}