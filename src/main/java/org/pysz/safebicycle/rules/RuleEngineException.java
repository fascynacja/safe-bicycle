package org.pysz.safebicycle.rules;

public class RuleEngineException extends RuntimeException {
    public RuleEngineException(String s) {
        super(s);
    }

    public RuleEngineException(String s, Exception e) {
        super(s, e);
    }
}
