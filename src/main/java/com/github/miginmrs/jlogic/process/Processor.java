package com.github.miginmrs.jlogic.process;

import com.github.miginmrs.jlogic.assertions.Assertion;
import com.github.miginmrs.jlogic.facts.Fact;
import com.github.miginmrs.jlogic.linked.Linked;
import com.github.miginmrs.jlogic.linked.Next;
import com.github.miginmrs.jlogic.linked.Node;
import com.github.miginmrs.jlogic.parsing.AssertionParser;
import com.github.miginmrs.jlogic.parsing.Parser;
import com.github.miginmrs.jlogic.parsing.RuleParser;
import com.github.miginmrs.jlogic.rules.FactBuilder;
import com.github.miginmrs.jlogic.rules.Rule;
import com.github.miginmrs.jlogic.rules.ZPRule;

import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor {
    private final Pattern ruleReferencePattern = Pattern.compile("\\s*apply (?<rule>[a-zA-Z0-9_]+)\\s*");
    private final Pattern demonstrationPattern = Pattern.compile("\\s*($|(?<dem>.+?)\\s*demonstration\\s*:\\s*)");
    private final Pattern demEndPattern = Pattern.compile("\\s*end\\s*\\.");
    private final Pattern ruleNamePattern = Pattern.compile("\\s*rule (?<rule>[a-zA-Z0-9_]+)\\s*:\\s*");
    private final AssertionParser assertionParser = new AssertionParser();
    private final PrintStream out;

    public Processor(PrintStream out) {
        this.out = out;
    }

    public void process(String string) throws Exception {
        Map<String,Rule> rules = new HashMap<>();
        RuleParser ruleParser = new RuleParser();
        Parser parser = new Parser(string);
        try{
            while(true) {
                Matcher matcher = parser.parseWithPattern(ruleNamePattern);
                String ruleName = matcher.group("rule");
                if(rules.containsKey(ruleName))
                    throw new Error("Error parsing file: duplicated rule name "+ruleName);
                ZPRule rule = (ZPRule) ruleParser.apply(parser);
                rule.setName(ruleName);
                rules.put(ruleName, rule);
                out.println("adding rule "+ruleName+": "+rule);
            }
        } catch(Exception ignored){
        }
        while(true) {
            int pos = parser.mark();
            Matcher matcher = parser.parseWithPattern(demonstrationPattern);
            String demString = matcher.group("dem");
            if(demString == null)
                break;
            switch(demString) {
                case "manual": demonstration(rules, parser); break;
                case "depth": depthDemonstrate(rules, parser); break;
                case "limited depth": limitedDepthDemonstrate(rules, parser); break;
                case "larger": largeDemonstrate(rules, parser); break;
                default: throw new Exception("unknown demonstration name "+demString+" on position "+pos);
            }
        }
    }
    private Assertion demonstration(Map<String,Rule> rules, Parser parser) throws Exception {
        List<Fact> facts = new LinkedList<>();
        Assertion goal = assertionParser.apply(parser);
        out.println("trying to demonstrate "+goal+" following given instructions");
        long startTime = System.nanoTime();
        try{
            while(true) {
                int pos = parser.mark();
                Matcher matcher = parser.parseWithPattern(ruleReferencePattern);
                String ruleName = matcher.group("rule");
                Rule rule = rules.get(ruleName);
                if(rule == null)
                    throw new Error("rule "+ruleName+" not found");
                Fact conclusion = rule.deduce(facts);
                if(conclusion == null) {
                    parser.rewind(pos);
                    throw new Error("rule "+ruleName+" can not be applied on position "+pos+" near: "+parser.parseWithPattern(Pattern.compile(".*")).group());
                }
                out.println("\t"+conclusion.getAssertion()+" deduced by "+ruleName);
                for(int i=0; i<rule.getPremissesNumber(); i++)
                    facts.remove(0);
                facts.add(0, conclusion);
            }
        } catch(Exception e) {
        }
        try{
            parser.parseWithPattern(demEndPattern);
        } catch (Exception e) {
            throw new Error("demonstration not closed at "+parser.mark());
        }
        if(facts.size()!=1)
            throw new Error("stack size "+facts.size()+" erroned (expected 1) at "+parser.mark());
        if(facts.get(0).getAssertion().equals(goal))
            out.println("goal "+goal+" demonstrated in "+(System.nanoTime() - startTime)/1e6 + "ms");
        else
            throw new Error("demonstration of "+goal+" failed at "+parser.mark()+", "+facts.get(0)+" deduced instead");
        return goal;
    }
    private boolean depthDeduce(Map<String,Rule> rules, List<Fact> facts, Set<Fact> visited, Assertion goal) {
        for(Rule rule:rules.values()) {
            Fact conclusion = rule.deduce(facts);
            List<Fact> temp = new LinkedList<>();
            if(conclusion == null)
                continue;
            if(!visited.add(conclusion))
                continue;
            for(int i=0; i<rule.getPremissesNumber(); i++)
                temp.add(0, facts.remove(0));
            facts.add(0, conclusion);
            if(conclusion.getAssertion().equals(goal))
                return true;
            if(depthDeduce(rules, facts, visited, goal))
                return true;
            facts.remove(0);
            for(int i=0; i<rule.getPremissesNumber(); i++)
                facts.add(0,temp.remove(0));
        }
        return false;
    }
    private Boolean limitedDepthDeduce(Map<String,Rule> rules, List<Fact> facts, Assertion goal, int n) {
        boolean stopped = false;
        for(Rule rule:rules.values()) {
            Fact conclusion = null;
            try{
                conclusion = rule.deduce(facts);
            } catch(Exception e){
            }
            if(conclusion == null)
                continue;
            List<Fact> temp = new LinkedList<>();
            if(n==0) {
                if(conclusion.getAssertion().equals(goal)){
                    for(int i=0; i<rule.getPremissesNumber(); i++)
                        temp.add(0, facts.remove(0));
                    facts.add(0, conclusion);
                    return true;
                }
                stopped = true;
                continue;
            }
            for(int i=0; i<rule.getPremissesNumber(); i++)
                temp.add(0, facts.remove(0));
            facts.add(0, conclusion);
            if(conclusion.getAssertion().equals(goal))
                return true;
            Boolean found = limitedDepthDeduce(rules, facts, goal, n-1);
            if(found == null)
                stopped = true;
            else if(found == true)
                return true;
            facts.remove(0);
            for(int i=0; i<rule.getPremissesNumber(); i++)
                facts.add(0,temp.remove(0));
        }
        if(stopped)
            return null;
        return false;
    }
    private Assertion depthDemonstrate(Map<String,Rule> rules, Parser parser) throws Exception {
        Set<Fact> visited = new HashSet<>();
        List<Fact> facts = new LinkedList<>();
        Assertion goal = assertionParser.apply(parser);
        out.println("trying to demonstrate "+goal+" using depth strategy");
        if(depthDeduce(rules, facts, visited, goal))
            out.println(facts.get(0)+"\ngoal "+facts.get(0).getAssertion()+" demonstrated");
        else
            throw new Error("demonstration of "+goal+" failed at "+parser.mark()+", "+facts.get(0)+" deduced instead");
        return goal;
    }
    private Assertion limitedDepthDemonstrate(Map<String,Rule> rules, Parser parser) throws Exception {
        List<Fact> facts = new LinkedList<>();
        Assertion goal = assertionParser.apply(parser);
        out.println("trying to demonstrate "+goal+" using limited depth strategy");
        Boolean found;
        long startTime = System.nanoTime();
        int n = 0;
        do
            found = limitedDepthDeduce(rules, facts, goal, n++);
        while(found == null);
        if(found)
            out.println(facts.get(0)+"\ngoal "+facts.get(0).getAssertion()+" demonstrated in "+(System.nanoTime() - startTime)/1e6+"ms within "+n+" iteration");
        else {
            String errorMessage = "demonstration of "+goal+" failed at "+parser.mark();
            if(!facts.isEmpty())
                errorMessage+=", "+facts.get(0)+" deduced instead";
            throw new Error(errorMessage);
        }
        return goal;
    }

    private Assertion largeDemonstrate(Map<String,Rule> rules, Parser parser) throws Exception {
        Assertion goal = assertionParser.apply(parser);
        out.println("trying to demonstrate "+goal+" using larger strategy");
        long startTime = System.nanoTime();
        Fact fact = largeDeduce(rules, goal);
        if(fact != null)
            out.println(fact+"\ngoal "+fact.getAssertion()+" demonstrated in "+(System.nanoTime() - startTime)/1e6+"ms");
        else
            throw new Error("demonstration of "+goal+" failed at "+parser.mark());
        return goal;
    }


    private Fact largeDeduce(Map<String,Rule> rules, Assertion goal) {
        Set<Assertion> assertions = new HashSet<>();
        Linked<Fact> oldFacts = new Linked<>(), recentFacts = new Linked<>(), newFacts;
        while(true){
            newFacts = new Linked<>();
            for(Rule rule:rules.values()) {
                FactBuilder ruleContext = rule.getRuleContext();
                Fact fact = largeDeduceRec(rule.getPremissesNumber(), oldFacts, recentFacts, newFacts, ruleContext, assertions, false, goal);
                if(fact != null)
                    return fact;
            }
            if(newFacts.next == null)
                return null;
            recentFacts.queue.next = oldFacts.next;
            recentFacts.queue = oldFacts.queue;
            oldFacts = recentFacts;
            recentFacts = newFacts;
        }
    }

    private Fact largeDeduceRec(int n, Linked<Fact> oldFacts, Linked<Fact> recentFacts, Linked<Fact> newFacts, FactBuilder ruleContext, Set<Assertion> assertions, boolean isNew, Assertion goal) {
        if(n == 0) {
            Fact conclusion = null;
            try{
                conclusion = ruleContext.getResult();
            } catch(Exception e){
            }
            if(conclusion != null) {
                if(assertions.add(conclusion.getAssertion()))
                    newFacts.append(conclusion);
                return conclusion.getAssertion().equals(goal)?conclusion:null;
            }
            return null;
        }
        n = n-1;
        boolean ok = n != 0 || isNew;
        for(Next<Fact> facts = recentFacts; facts != oldFacts || ok; facts = oldFacts) {
            for(Node<Fact> factNode = facts.next; factNode != null; factNode = factNode.next) {
                FactBuilder factBuilder = ruleContext.setPremiss(factNode.item, n);
                if(factBuilder == null)
                    continue;
                Fact fact = largeDeduceRec(n, oldFacts, recentFacts, newFacts, factBuilder, assertions, true, goal);
                if(fact != null)
                    return fact;
            }
            if(facts == oldFacts)
                break;
        }
        return null;
    }}
