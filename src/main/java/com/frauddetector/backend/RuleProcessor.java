package com.frauddetector.backend;

import com.frauddetector.frontend.models.Rule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class RuleProcessor {
    private static final List<Rule> rules = Collections.synchronizedList(new ArrayList<>
            ());

    public static synchronized List<Rule> getActiveRules() {
        List<Rule> activeRules = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.isActive()) {
                activeRules.add(rule);
            }
        }
        return activeRules;
    }

    public static synchronized void addRule(Rule rule) {
        rules.add(rule);
    }

    public static synchronized void removeRule(Rule rule) {
        rules.remove(rule);
    }

    public static synchronized void updateRule(Rule updatedRule) {
        for (int i = 0; i < rules.size(); i++) {
            Rule existingRule = rules.get(i);
            if (existingRule.equals(updatedRule)) {
                rules.set(i, updatedRule);
                break;
            }
        }
    }
}