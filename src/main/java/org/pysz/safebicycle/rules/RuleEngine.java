package org.pysz.safebicycle.rules;

import groovy.lang.Script;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pysz.safebicycle.rules.model.Bicycle;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class RuleEngine {

    private static final String PREMIUM_GROOVY_METHOD = "premium";
    private static final String SUM_INSURED_GROOVY_METHOD = "sumInsured";
    private final Map<String, Script> ruleStorage;

    public BigDecimal premiumForRisk(String riskType, Bicycle bicycle) {
        return callRuleMethodForRisk(riskType, bicycle, PREMIUM_GROOVY_METHOD);
    }

    public BigDecimal sumInsuredForRisk(String riskType, Bicycle bicycle) {
        return callRuleMethodForRisk(riskType, bicycle, SUM_INSURED_GROOVY_METHOD);
    }

    private BigDecimal callRuleMethodForRisk(String riskType, Bicycle bicycle, String methodName) {
        log.trace("running rule for calculation of method: {} for riskType: {} and bicycle: {}", methodName, riskType, bicycle);
        if (!ruleStorage.containsKey(riskType)) {
            log.error("No rules defined for given risk type: " + riskType);
            throw new RuleEngineException("No rules defined for given risk type: " + riskType);
        }
        Script script = ruleStorage.get(riskType.toUpperCase());
        try {
            Object result = script.invokeMethod(methodName, bicycle);
            log.debug("Calculated value: {} for method: {} for riskType: {} and bicycle: {}", result, methodName, riskType, bicycle);
            if (result instanceof BigDecimal) {
                return (BigDecimal) result;
            } else {
                log.error("Calculated value {} is not BigDecimal type:{}", result, result.getClass());
                throw new RuleEngineException("Calculated value is not BigDecimal type: " + result.getClass());
            }
        } catch (Exception groovyException) {
            throw new RuleEngineException("Error happened while running groovy method " + methodName + " for riskType: " + riskType, groovyException);
        }

    }
}
